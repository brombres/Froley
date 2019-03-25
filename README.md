# Froley

## Overview
Froley is a "compiler compiler" for creating virtual machine-based tokenizers and parsers.

A single `.froley` definition file is compiled to a `.json` file containing token type definitions and compressed bytecode. A build tool like `rogo` can be used to perform the compilation and then generate source code files in an aribtrary language from the resulting JSON.

The `Demos/` folder contains full Froley tokenizer and parser virtual machines written in a variety of languages (TODO) that can be easily adapted to your own projects.

## Installation
1. Install [Rogue](https://github.com/AbePralle/Rogue)
2. Clone this Froley project
3. Run `rogo` in the base folder to compile the Froley compiler. A launcher will be installed at `/usr/local/bin/froley`.

## Usage
1. Write a .froley definition file for a new language.
2. `froley filename.froley` will compile and write `filename.json`.
3. Examine e.g. `Demos/Simple` for an example of generating source code definition files from the JSON (see `Build.rogue`) as well as implementations of tokenizer and parser virtual machines (`TokenizerVM.rogue`, `ParserVM.rogue`).

## Documentation

## .froley Files
A Froley definition file has any number of _sections_. A section begins with `[<section-name>]`. There are three pre-defined section types:

1. `[attributes]` - defines token type attributes
2. `[tokenizer]` - contains tokenizer source code
3. `[parser]` - contains parser source code

Other sections with arbitrary names may be defined. These sections implicitly contain token type definitions that will be referenced from both the `[tokenizer]` and the `[parser]` source code. Examples include `[tokens]`, `[keywords]`, and `[symbols]`.

### Comments
Any part of a line from `#` onwards is treated as a comment and is ignored.

### `[attributes]`
An [attributes] section contains a list of arbitrary token type attribute tags. For example, you might want to give certain tokens a `structural` attribute to indicate they are not typical expressions. You can do that with an [attributes] section, like this:

    [attributes]
    structural

## Token Definitions
Token type definition sections start with an arbitrary name such as `[tokens]`, `[keywords]`, or `[symbols]`. Each following line has the following format:

    TOKEN_TYPE_NAME symbol [optional-attributes]

The symbol can be a literal sequence of characters or a descriptive name. It can optionally be delimited by double quotes (a necessity for hash marks, which are comments otherwise) and may escape characters within the quotes using a backslash (a necessity for a double-quote token type). A quoted symbol may contain standard control character escapes such as `\n` and `\t`.

For example:
    [tokens]
    EOL               "\n"
    STRING            string
    INT64             integer
    INT32             integer

    [keywords]
    KEYWORD_CLASS     class     [structural]
    KEYWORD_END_CLASS endClass  [structural]

    [symbols]
    SYMBOL_PLUS         +
    SYMBOL_PLUS_PLUS    ++
    SYMBOL_HASH         "#"
    SYMBOL_DOUBLE_QUOTE "\""


## `[tokenizer]` & Froley Tokenizer Language
The `[tokenizer]` section contains tokenizer source code written in "Froley Tokenizer Language". _FTL_ is a domain-specific language that can be thought of as a mid-level hybrid of assembly and high-level language.

A tokenizer VM will repeatedly re-run the FTL program to obtain each next token until the program either signals that no further tokens are available or generates an error. The program begins at the first command by default.

### FTL Labels
Standard labels are defined with single angle brackets: `<label>`. They can be used as a subroutine call or a `goto` target.

_Entry Point_ labels are defined with double angle brackets: `<<label>>`. They act like regular labels and can also be used as entry points into the tokenizer program, which can effectively be used to put the tokenizer into different states. The entry point can be dynamically changed, either externally in the VM or internally using the `mode` command.

### FTL Registers

FTL operations utilize a handful of predefined variables called _registers_.

Register  | Data Type    | Usage    | Description
----------|--------------|----------|-----------------------------
`ch`      |Int32         | Direct   | Use to store character codes; prints as a symbol.
`count`   |Int32         | Direct   | Use as a counter or to store characters; prints as an integer.
`buffer`  |StringBuilder | Direct   | Builds a string of characters to be tokenized, printed, or shown as an error message.
`result`  |Int32         | Mixed    | Stores the result of various operations.
`stack`   |Int32[]       | Implicit | Used as both call stack and data stack.
`ip`      |Int32         | Implicit | Instruction Pointer - address of next instruction.

### FTL Command Reference
While many of the FTL commands are written in a high-level, general style, only the specific variations shown here may be used. This is due to the limited and speed-optimized bytecode instruction set. For example, `ch += count` is a valid command but `count += ch` is not.

Note: while angle brackets are necessary to define labels, in the following commands they are not used literally and instead they are used to indicate placeholders for actual values.

#### FTL Command: halt
##### Syntax
    halt

##### Description
Call when `hasAnother` returns false and no input remains to be tokenized.  Stops the program and indicates that tokenization is complete.

Command Reference        | Description
-------------------------|--------------------------------------------------------------------
`halt`                   | Tokenization complete
`restart`                | Restart program from current entry point
`restart <label-name>`   | Change entry point and restart
`mode <label-name>`      | Change entry point
`error buffer`           | Throw error using text in `buffer` as message
`error "message"`        | Copy message to buffer and throw error
`markSourcePosition`     | Record current source position for next token or error
`accept <TYPE-NAME>`     | Create a new token and restart program
`goto <label>`           | Jump to label
`if/elseIf/else/endIf`   | `if` statement
`while/endWhile`         | `while` loop
`<label-name>` (call)    | Call subroutine
`return`                 | Return from subroutine
`hasAnother`             | Sets `result` to indicate whether or not another input character is available
`ch = peek`              | Sets ch to be the next input character without removing it
`ch = peek(<lookahead>)` | Peek any number of characters ahead, where a lookahead of 0 is the next available character
`ch = peek(count)`       | Peek `count` number of characters ahead
`ch = read`              | Read the next character of input
`consume(<character>)`   | Read and discard the specified character if possible, setting `result` to indicate success
`ch = scanDigits <n>`    | Attempt to parse a number with exactly _n_ base-10 digits, setting `result` to indicate success
`ch = scanDigits <n> base <x>` | Attempt to parse a number with exactly _n_ base-<i>x</i> digits, setting `result` to indicate success
`ch = scanDigits <min>..<max>` | Attempt to parse a number with between _min_ and _max_ base-10 digits, setting `result` to indicate success
`ch = scanDigits <min>..<max> base <x>` | Attempt to parse a number with between _min_ and _max_ base-<i>x</i> digits, setting `result` to indicate success
`scanIdentifier`                        | Clear `buffer` and attempt to parse an identifier, setting `result` to indicate success
`clear buffer`                          | Clears the character buffer
`collect ch`                            | Add the character code `ch` to the character buffer
`collect <character>`                   | Add the specified character to the character buffer
`collect "<string>"`                    | Add the specified string to the character buffer
`print buffer`                          | Log the current contents of `buffer`
`print ch`                              | Log the character represented by `ch`
`print count`                           | Log the integer value of `count`
`print <character>`                     | Log the specified character
`print "<string>"`                      | Log the specified string
`ch == <character-or-integer>`          | Compares

Command               | Description
----------------------|--------------------------------------------------------------------
`halt`                | Stop the VM and signal that all tokens have been read.
`restart`             | Restart tokenization from the current entry point.
`restart <label>`     | Changes the entry point and restarts the program.
`mode <label>`        | Changes the entry point without restarting the program.
`error buffer`        | Throws an error using the text in `buffer` as the error message.
`error "message"`     | Throws an error using the given literal message. Note: internally the message is first copied into the buffer.
`markSourcePosition`  | Record the current source position to be used for the next token or error message.
`accept <TYPE_NAME>`  | Create a new token with the given TYPE_NAME and restart. Example: `accept SYMBOL_PLUS`.
`goto <label>`        | Control moves to the the given label.
`if/elseIf/else/endIf`| See [[FTL `if` Statement]] below.
`while`               | See [[FTL `while` Statement]] below.

### FTL Conditionals
`if` and `while` both operate on conditionals. Conditionals have limited forms of expression and can be any of the following:

Conditional           | Description
----------------------|--------------------------------------------------------------------
<call>                | Calls the subroutine and evaluates to true if `result` is set non-zero when the subroutine returns. For example, if you have defined `<<my_routine>>` then

### FTL `if` Statement

### FTL `while` Statement

## [parser] & Froley Parser Language

### FTL Registers

Froley Tokenizer Language directly accesses the following registers.

Name      | Data Type    | Usage    | Description
----------|--------------|----------|-----------------------------
`ch`      |Int32         | Direct   | Use to store characters; prints as a symbol.
`count`   |Int32         | Direct   | Use as a counter or to store characters; prints as an integer.
`buffer`  |StringBuilder | Direct   | Builds a string of characters to be tokenized, printed, or shown as an error message.
`result`  |Int32         | Implicit | Stores the result of various calculations; cannot be directly manipulated but is used indirectly by conditionals.
`stack`   |Int32[]       | Implicit | Used as both call stack (subroutine call, `return`) and data stack (`push`, `pop`).
`ip`      |Int32         | Implicit | Instruction Pointer - address of next instruction.
`start_ip`|Int32         | Implicit | `ip` is reset to this entry point each time the program is re-run. It is set to the first command by default and can be changed with a `restart` or `mode` command.
