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
3. Examine e.g. `Demos/Simple` for an example of generating source code definition files from the JSON (`Build.rogue`) as well as implementations of tokenizer and parser virtual machines (`TokenizerVM.rogue`, `ParserVM.rogue`).

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


## `[tokenizer]`
The `[tokenizer]` section contains tokenizer source code written in "Froley Tokenizer Language". _FTL_ is a domain-specific language that can be thought of as a mid-level hybrid of assembly and high-level language.

A tokenizer VM will repeatedly re-run the FTL program to obtain each next token until the program either signals that no further tokens are available or generates an error. The program begins at the first command by default but a different label (code position) can be designated as an entry point, either externally in the VM or internally using the `mode` command.

### Registers
Froley Tokenizer Language uses the following registers.

Name   | Data Type    | Usage    | Description
-------|--------------|----------|-----------------------------
ch     |Int32         | Direct   | Use to store characters; prints as a symbol
count  |Int32         | Direct   | Use as a counter or to store characters; prints as an integer
buffer |StringBuilder | Direct   | Builds a string of characters to be tokenized, printed, or shown as an error message
result |Int32         | Implicit | Stores the result of various calculations; cannot be directly manipulated but is used indirectly by conditionals
ip     |Int32         | Implicit | Instruction Pointer - address of next instruction
stack  |Int32[]       | Implicit | Used as both call stack (subroutine call, `return`) and data stack (`push`, `pop`)

### Labels


## [parser]
