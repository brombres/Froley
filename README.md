# Froley

About          | Current Release
---------------|-----------------------
Version        | v3.0
Date           | November 22, 2021
Platforms      | macOS, Linux, Windows
Output Targets | Rogue
Author         | Abe Pralle

# Overview
1. Froley is a "compiler compiler" that creates and maintains a compiler or interpreter framework.
2. A `.froley` definition file contains token definitions as well as Scanner and Parser logic written in the domain-specific Froley language. The Froley language is imperative with many declarative-style features.
3. Froley outputs a working compiler framework which includes a Scanner, a Parser, "Cmd" node definitions, and "Visitor" infrastructure for inspecting and processing the AST (Abstract Syntax Tree) output of the Parser.
that accepts a `.froley` definition file as input.
4. If the `.froley` definition is updated and Froley is re-run on an existing project, it will make necessary changes without overwriting developer modifications.
5. Froley currently only outputs compilers written in the Rogue language, which in turn is compatible with C++. Other language targets may be added in the future.
6. Froley is self-hosting - the Froley compiler framework is written in Froley.

# Installation
1. Install [morlock.sh](https://morlock.sh)
2. `morlock install abepralle/froley`

# Documentation
Refer to the [Wiki](wiki) for documentation.

# Examples
The `Examples/` folder contains a number of examples of increasing scope. Run `rogo` in an example folder to compile and run the example.

# Usage

    froley [OPTIONS] language-definition.froley output-folder/

## Options

- `--help`, `-h`, `-?`<br>
    Display help text.

- `--main`, `-m`<br>
    Creates a main application file that demonstrates how to use the scanner and parser.
    Does not create or modify a main file if it already exists. If the language definition is named
    `XYZ.froley` then the main application file will be `XYZ.rogue`.

