# Rust Playground
This program is created by `cargo new rust-playground`.

This Playground comes from reading "The Book": https://doc.rust-lang.org/book

Bookmark: https://doc.rust-lang.org/book/ch10-02-traits.html

## Describe the organization of crates
https://doc.rust-lang.org/book/ch07-02-defining-modules-to-control-scope-and-privacy.html

Looking at the contents of Cargo.toml, there’s no mention of src/main.rs
because Cargo follows a convention that src/main.rs is the crate root of a
binary crate with the same name as the package. Likewise, Cargo knows that if
the package directory contains src/lib.rs, the package contains a library crate
with the same name as the package, and src/lib.rs is its crate root.
(ref: https://doc.rust-lang.org/book/ch07-01-packages-and-crates.html)

If a package contains src/main.rs and src/lib.rs, it has two crates: a binary
and a library, both with the same name as the package. A package can have
multiple binary crates by placing files in the src/bin directory: each file
will be a separate binary crate.

src/main.rs and src/lib.rs are called crate roots. The reason for their name is
that the contents of either of these two files form a module named crate at the
root of the crate’s module structure, known as the module tree.

## Dev
Commands are run the top-level directory, i.e. "rust-playground".

Use `cargo doc` to generate docs in the _target_ directory.
The document can be viewed by opening
_rust-playground/target/doc/rust_playground/index.html_.

Use `cargo doc --open` to generate and open the doc.

Use `cargo build` to build the program. The program is stored as
_target/debug/rust-playground_.

Use `cargo run` to build and run the program in one command. (Only works if we have only one binary)

* `cargo run input_array_index` to run the function that gets an array index
  from stdin to print out something.

`cargo run --bin rust-playground` to run the rust-playground binary

Use `cargo fmt` to format the files for this module.

`cargo check` is to make sure your code compiles but it doesn't create an
executable.

`cargo new --lib restaurant` creates a new library called restaurant.
## Run
Three binaries in this playground.

`cargo run --bin rust-playground`
`cargo run --bin other`
`cargo run --bin my_prog`

## Deploy
`cargo build --release` to build an optimized binary in _target/release_.
