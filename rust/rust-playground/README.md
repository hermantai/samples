# Rust Playground
This program is created by `cargo new rust-playground`.

This Playground comes from reading "The Book": https://doc.rust-lang.org/book

Bookmark: https://doc.rust-lang.org/book/ch03-04-comments.html (remember to read Chapter 2 after this)

## Dev
Commands are run the top-level directory, i.e. "rust-playground".

Use `cargo doc` to generate docs in the _target_ directory.
The document can be viewed by opening
_rust-playground/target/doc/rust_playground/index.html_.

Use `cargo build` to build the program. The program is stored as
_target/debug/rust-playground_.

Use `cargo run` to build and run the program in one command.

* `cargo run input_array_index` to run the function that gets an array index
  from stdin to print out something.

Use `cargo fmt` to format the files for this module.

`cargo check` is to make sure your code compiles but it doesn't create an
executable.

## Deploy
`cargo build --release` to build an optimized binary in _target/release_.
