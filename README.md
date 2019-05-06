# Froley

About          | Current Release
---------------|-----------------------
Version        | v0.1.0
Date           | May 5, 2019
Output Targets | Rogue, Java

## Overview

* Froley is a "compiler compiler" for creating virtual machine-based tokenizers and parsers.
* A developer programs their tokenizer and parser in two different domain-specific languages: Froley Tokenizer Language (FTL) and Froley Parser Language (FPL).
* The Froley compiler turns the Tokenizer and Parser programs into bytecode. It also generates Tokenizer and Parser VMs to run the bytecode along with supporting compiler framework in a specified target language - currently Rogue and Java target languages are supported.
* Froley can update a previously-generated compiler framework. It will rewrite certain enumerations and add new command node (AST node) class definitions but it will not overwrite any custom modifications that have been made.
* Consequently the ".froley" source file serves as the master definition for a custom language's syntax, grammar, tokenizer, and parser. Changes and additions made to the language can be easily propagated to various compiler implementations in a variety of host languages.

## Installation
1. Clone this Froley project
2. Install [Rogue](https://github.com/AbePralle/Rogue)
3. Run `rogo` in the base folder to compile the Froley compiler. A launcher will be installed at `/usr/local/bin/froley`.

## Demos
The `Demos/` folder contains a number of examples that can be adapted to your own projects. Execute the following Rogo commands to try the different examples:

* `rogo simple`
* `rogo csq`
* `rogo json`
* `rogo asm`

## Documentation
Refer to the [Wiki](https://github.com/AbePralle/Froley/wiki).

