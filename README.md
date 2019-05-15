# Froley

About          | Current Release
---------------|-----------------------
Version        | v1.0.0
Date           | May 5, 2019
Output Targets | Rogue, Java

## Overview
1. Froley is a compiler-compiler that accepts a `.froley` definition file as input.
2. Tokenizers and Parsers are expressed in Domain-Specific Languages and compiled to VM bytecode.
3. Froley creates or updates a compiler framework in an arbitrary target language (currently Rogue and Java are supported).
4. VM bytecode is compact, portable, and dynamically updatable.

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

