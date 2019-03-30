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
Refer to the [https://github.com/AbePralle/Froley/wiki](Wiki) for documentation.

