# Froley

About          | Current Release
---------------|-----------------------
Version        | v2.2.1
Date           | October 21, 2021
Platforms      | macOS, Linux, Windows
Output Targets | Rogue

## Overview
1. Froley is a compiler-compiler that accepts a `.froley` definition file as input.
2. Tokenizers and Parsers are expressed in Domain-Specific Languages and compiled to VM bytecode.
3. Froley creates or updates a compiler framework in an arbitrary target language (currently only Rogue is supported).
4. VM bytecode is compact, portable, and dynamically updatable.

## Installation
1. Clone this Froley project
2. Install [Rogue](https://github.com/AbePralle/Rogue)
3. Run `rogo` in this folder to compile Froley.
    - On macOS and Linux a launcher will be created here: `/usr/local/bin/froley`.
    - On Windows the build process will print the necessary folder to add to the system PATH environment variable.

## Demos
The `Demos/` folder contains a number of examples that can be adapted to your own projects. Execute the following Rogo commands to try the different examples:

* `rogo simple`
* `rogo bad basic`
* `rogo json`
* `rogo asm`

## Documentation
Refer to the [Wiki](https://github.com/AbePralle/Froley/wiki).

