//! This program is created by `cargo new rust-playground`.
//! Use `cargo doc` in the top-level directory to generate docs in the _target_
//! directory.

// This is the main function
/// Generate library docs for the following item. Which is a fancy main
/// function! Support <b>HTML</b>.
fn main() {
    // Statements here are executed when the compiled binary is called

    print_header();
    play_print_stuff();

    print_header();
    play_string_interpolation();
}

/**
 * Plays printing or formatting some stuff.
 */
fn play_print_stuff() {
    // Print text to the console
    println!("Hello World!");
    // formatting
    println!("{} days", 31);

    eprintln!("This prints to error");

    eprint!("This prints to error without new line, so...");
    eprint!("yes, same line...");
    let adj = "new";
    let x = format!("this is a {} line", adj);
    eprintln!("{}", x);

    println!("Named arguments works: {action}", action = "see");
    println!(
        "Positionl argment is fine as well, zero based: {1} {0}",
        "pos0", "pos1"
    );

    // You can right-align text with a specified width. This will output
    // "     1". 5 white spaces and a "1".
    println!(
        "Right aligned text: {number:>width$}",
        number = 1,
        width = 6
    );

    // You can pad numbers with extra zeroes. This will output "000001".
    println!("{number:0>width$}", number = 1, width = 6);

    //
    // Examples of named arguments:
    //
}

/**
 * Plays string interpolation.
 */
fn play_string_interpolation() {
    //! Generate library docs for the enclosing item. A fancy string
    //! interpolation function. <b>support HTML</b>
    // string interpolation
    let x = 5 + /* 90 + */ 5;
    println!("Is `x` 10 or 100? x = {}", x);
}

/**
 * Prints a header.
 */
fn print_header() {
    println!("\n===== =====\n")
}
