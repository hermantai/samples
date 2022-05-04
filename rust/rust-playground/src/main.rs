//! This program is created by `cargo new rust-playground`.
//! Use `cargo doc` in the top-level directory to generate docs in the _target_
//! directory.
//!
//! Convention: Rust code uses snake case as the conventional style for function and variable
//! names, in which all letters are lowercase and underscores separate words
//!
//! Each small lesson is enclosed in a function named "play_xxx" where xxx
//! is the lession.

use std::env;
use std::io;
use std::process;

// This is the main function
/// Generate library docs for the following item. Which is a fancy main
/// function! Support <b>HTML</b>.
fn main() {
    // Statements here are executed when the compiled binary is called
    print_header("play_process_input");
    play_process_input();

    print_header("play_print_stuff");
    play_print_stuff();

    print_header("play_string_interpolation");
    play_string_interpolation();

    print_header("play_variable_stuff");
    play_variable_stuff();

    print_header("play_data_types");
    play_data_types();

    print_header("play_functions");
    play_functions();
}

fn play_process_input() {
    let args: Vec<String> = env::args().collect();
    println!("Input parameters: {:?}, len = {}", args, args.len());

    // args[0] is the script itself, so the first parameter is at args[1].
    if args.len() >= 2 {
        let operation = &args[1];
        if operation == "input_array_index" {
            let ar = [1, 2, 3, 4, 5];
            println!("array is {:?}. Please enter an index:", ar);
            let mut index = String::new();
            io::stdin()
                .read_line(&mut index)
                .expect("Failed to read line");

            let index: usize = index
                .trim()
                .parse()
                .expect("Index entered was not a number");
            let element = ar[index];
            println!(
                "The value of the element at index {} is : {}",
                index, element
            );

            process::exit(0);
        }
    }
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

    let ar = [1, 2, 3];
    println!("print with a debugging formatter: {:?}", ar);
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
 * Plays with variable assignments.
 */
fn play_variable_stuff() {
    // Mutable variable.
    let mut x = 5;
    println!("Number is {}", x);
    x = 6;
    println!("Number is {}", x);

    print_subsector_divider();
    // Constants are always immutable and a type has to be specified.
    // Rust’s naming convention for constants is to use all uppercase with underscores between
    // words
    const THREE_HOURS_IN_SECONDS: u32 = 60 * 60 * 3;
    println!("Constant is {}", THREE_HOURS_IN_SECONDS);

    print_subsector_divider();
    let s = 5;
    let s = s + 1; // now s is 6
    println!("s is {}", s);
    {
        let s = s * 2;
        println!("s inside a block is {}. It's shadowing", s);
        let s = "abcde";
        println!("Shadowing can change the type. s inside a block is {}.", s);
    }
    println!("s outside the block is still {}", s);

    print_subsector_divider();
    let y = {
        let x = 3;
        x + 1
    };
    println!("A variable can be assigned with a block. Notice that the end of the block is an expression. y = {}", y);
}

/**
 * Play some data types.
 */
fn play_data_types() {
    let dec = 1.123;
    let byte = b'A';
    let c = 'A';

    print_subsector_divider();
    println!("dec = {0}, byte = {1}, c = {2}", dec, byte, c);

    let tup: (i32, f64, u8) = (500, 6.4, 1);
    let (a, b, c) = tup;
    print_subsector_divider();
    println!("tuple: first = {}, b = {}, last = {}", tup.0, b, tup.2);
    println!("pattern matching to deconstruct a tuple for assignment: a = {}, c = {}", a, c);

    // array has fixed length, and all elements have to ahve the same type.
    // An array is allocated on stack.
    let ar = [1, 2, 3];

    // Array of i32, 5 elements
    let ar_with_type: [i32; 5] = [1, 2, 3, 4, 5];
    println!("ar: {:?}, first value: {}", ar, ar[0]);
    println!("ar_with_type: {:?}", ar_with_type);

    // an array with initial value of 3, with 5 elements
    let ar_with_initial_value = [3; 5];
    println!("ar_with_initial_value: {:?}", ar_with_initial_value);

    // A String is stored as a vector of bytes (Vec<u8>), but guaranteed to always be a valid UTF-8
    // sequence. String is heap allocated, growable and not null terminated.
    // &str is a slice (&[u8]) that always points to a valid UTF-8 sequence, and can be used to view into
    // a String, just like &[T] is a view into Vec<T>.
    // See https://doc.rust-lang.org/rust-by-example/std/str.html

    print_subsector_divider();
    // (all the type annotations are superfluous)
    // A reference to a string allocated in read only memory
    let pangram: &'static str = "the quick brown fox jumps over the lazy dog";
    println!("pangram: {}", pangram);
}

/**
 * Plas functions: https://doc.rust-lang.org/book/ch03-03-how-functions-work.html.
 *
 * Expressions do not include ending semicolons. If you add a semicolon to the end of an
 * expression, you turn it into a statement, and it will then not return a value.
 */
fn play_functions() {
    println!("This value is returned from a function: {}", five());
}

/**
 * Return type has to be specified. The last expression of a function is
 * the returned value. It can be returned with the statement "return 5;" as well.
 */
fn five() -> i32 {
    5
}

/**
 * Prints a header.
 */
fn print_header(header: &str) {
    println!("\n=====\n===== {header} =====\n=====\n", header = header)
}

fn print_subsector_divider() {
    println!("\n-\n");
}
