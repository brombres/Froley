# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

Froley is a "compiler compiler": it reads a `.froley` language definition (tokens + Scanner/Parser logic in the Froley DSL) and emits a Rogue-language compiler framework (Scanner, Parser, `Cmd` AST nodes, `Visitor` infrastructure). Rogue is the only output target. Froley is self-hosting — `Source/Froley.froley` defines Froley's own language, and the corresponding generated files live in `Source/`.

Everything is written in [Rogue](https://github.com/brombres/Rogue); the build tool is Rogo (`Build.rogue`). A local clone of the Rogue language repo (with its own `CLAUDE.md`) is at `~/Projects/Rogue` — consult it for Rogue syntax/library questions.

Documentation lives on the GitHub wiki, cloned locally into the gitignored `Wiki/` folder (`git clone https://github.com/brombres/Froley.wiki.git Wiki` if missing; `git -C Wiki pull` to refresh). Key pages: `Concepts.md`, `Token-Type-Definitions.md`, `Scanner-Command-Reference.md`, `Parser-Command-Reference.md`, `Visitor-API.md`, `Native-Types.md`.

## Commands

All commands are Rogo routines defined in `Build.rogue` (`rogo help` lists them). Requires `roguec`/`rogo` installed.

- `rogo` — build (Rogue → C → exe) and run `Build/Froley-<OS>`
- `rogo build` / `rogo rebuild` — incremental / forced build (release mode)
- `rogo debug` / `rogo build_debug` — debug build (`--debug` to roguec, `-O0`)
- `rogo clean` — delete `Build/` and `.rogo/`
- `rogo install` — build and create `/usr/local/bin/froley` launcher; `rogo link` uses Morlock's binpath instead
- `rogo froley` — regenerate `Source/` from `Source/Froley.froley` using the *installed* `froley` (self-hosting step). This is intentionally commented out of `rogo build`; run it manually after editing `Froley.froley`, then `rogo build`.
- `rogo t` (in `BuildLocal.rogue`, gitignored) — smoke test: creates `Test/`, runs `froley -cm && rogo` in it, deletes it. There is no unit-test suite.
- `rogo commit <ver>` / `rogo publish <ver>` — bump `VERSION`/`DATE` in `Source/Froley.rogue` and `README.md`, commit as `[vX.Y]`, publish a GitHub release (merges into `master`). Work happens on `develop`; `master` is the release branch.
- Examples: `cd Examples/<name> && rogo` builds and runs one; `cd Examples && rogo` runs them all. Each example's `rogo build` re-runs `froley` when its `.froley` file is newer than the exe.

Local overrides (e.g. `BUILD_MODE = debug`) go in a `Local.settings` file.

## Architecture

### Compilation pipeline (`Source/Program.rogue`, `Program.compile`)

`Froley.rogue` parses args (`--create/-c`, `--main/-m`, `--project=/-p`), then either scaffolds a project (`BuildfileGenerator` + `StarterFroleyGenerator`) or compiles a `.froley` file:

1. `Parser(file).parse` → `ProgramDef` AST (Scanner/Parser are themselves Froley-generated).
2. Visitor passes over the AST, in order: `Collector` → `TokenOrganizer` → `Organizer` → `Resolver` → `Validator`. `Program` is the global singleton holding types (`Type`/`FType`), routines, token/scan patterns, and user-call sets that these passes populate.
3. `RogueGenerator` (`Source/Rogue/RogueGenerator.rogue`) walks the AST and writes the output folder. `ScanTableBuilder`/`ScanTable` compile scanner match patterns into tables; `BranchAnalyzer` determines control-flow so the generator can detect unreachable/fall-through code.

### Code generation and the three overwrite policies

`RogueGenerator` delegates each output file to an `R*` class in `Source/Rogue/` (`RCmd`, `RParserCore`, `RScannerCore`, `RTokenType`, `RVisitor`, …), all subclasses of `RogueSourceWriter`. Each file uses one of three policies — check the header comment of any generated file before editing it:

| Header | Policy | Writer method |
|---|---|---|
| `WARNING: WILL BE OVERWRITTEN` (`ParserCore`, `ScannerCore`, `TokenType`) | Fully regenerated every run. Never hand-edit; change the `.froley` or the generator instead. | `overwrite` |
| `Generated and updated by Froley. Custom changes will not be overwritten` (`Cmd`, `Visitor`, `InspectionVisitor`) | Merged: generator scans existing content and inserts missing node types/methods while preserving user code. | `update` / `update_content` |
| `Will not be overwritten` (`Scanner`, `Parser`, `Token`, `CompileError`, `SyntaxError`, `FrameStack`, main file) | Created once; user owns it thereafter. | `create_missing` |

This applies equally to Froley's own `Source/` — e.g. `Source/ParserCore.rogue` is regenerated from `Froley.froley`, whereas `Source/Parser.rogue` is hand-maintained. Files with no header (`Program`, `Resolver`, `Organizer`, `Type`, …) are ordinary hand-written source.

### Generated output structure (what users of Froley get)

`Scanner : ScannerCore` and `Parser : ParserCore` — the `*Core` classes contain the generated state machines; the subclasses are the user's customization layer. `Cmd` subclasses are AST nodes; `Visitor` provides `on_visit`/`on_visit_node` double-dispatch with per-type overloads, which is the same mechanism Froley's own passes (`Collector`, `Resolver`, `RogueGenerator`, …) use.

### Version pinning

`Morlock/froley.rogue` declares the Rogue version dependency for Morlock installs; `Build.rogue` has `$requireRogue "2.0"`. Rogue syntax evolves (recent commits track renames like `module`→`library`), so generated code must match the pinned Rogue version.
