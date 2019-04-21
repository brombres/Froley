# Froley

## Overview
Froley is a "compiler compiler" for creating virtual machine-based tokenizers and parsers.

A single `.froley` definition file is compiled to a `.json` file containing token type definitions and compressed bytecode. Froley can automatically generate parser infrastructure for supported host languages as well (currently: Rogue).

## Installation
1. Clone this Froley project
2. Install [Rogue](https://github.com/AbePralle/Rogue)
3. Run `rogo` in the base folder to compile the Froley compiler. A launcher will be installed at `/usr/local/bin/froley`.

## Demos
The `Demos/` folder contains a number of examples that can be adapted to your own projects. Execute the following Rogo commands to try the different examples:

* `rogo simple`
* `rogo csq`
* `rogo asm`

## Documentation
Refer to the [https://github.com/AbePralle/Froley/wiki](Wiki).

