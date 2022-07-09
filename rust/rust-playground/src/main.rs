//! This program is created by `cargo new rust-playground`.
//! Use `cargo doc` in the top-level directory to generate docs in the _target_
//! directory.
//! Use `cargo doc --open` to generate and open the doc.
//!
//! Convention: Rust code uses snake case as the conventional style for function and variable
//! names, in which all letters are lowercase and underscores separate words
//!
//! Each small lesson is enclosed in a function named "play_xxx" where xxx
//! is the lession.

// Rng is a trait in the rand library crate. A trait defines methods.
use rand::Rng;
use std::cmp::Ordering;
use std::collections::HashMap;
use std::fs::{self, File};
use std::env;
use std::fmt;
use std::fmt::Display;
use std::io::{self, ErrorKind, Write, Read};
use std::process;
// https://crates.io/crates/unicode-segmentation
use unicode_segmentation::UnicodeSegmentation;

// If using the following, we only need to call eat_at_restaurant()
// use rust_playground::eat_at_restaurant;

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

    print_header("play_control_flows");
    play_control_flows();

    print_header("play_random_number");
    play_random_number();

    print_header("play_ownerships");
    play_ownerships();

    print_header("play_slices");
    play_slices();

    print_header("play_structs");
    play_structs();

    print_header("play_enums");
    play_enums();

    print_header("play_crates");
    play_crates();

    print_header("play_vector");
    play_vector();

    print_header("play_string");
    play_string();

    print_header("play_hash_map");
    play_hash_map();

    print_header("play_panic");
    play_panic();

    print_header("play_generics");
    play_generics();

    print_header("play_traits");
    play_traits();

    print_header("play_lifetimes");
    play_lifetimes();
}
// end of main

fn play_process_input() {
    let args: Vec<String> = env::args().collect();
    println!("Use input_array_index argument to try entering index from standard in. Input parameters: {:?}, len = {}", args, args.len());

    // args[0] is the script itself, so the first parameter is at args[1].
    if args.len() >= 2 {
        let operation = &args[1];
        if operation == "input_array_index" {
            let ar = [1, 2, 3, 4, 5];
            println!("array is {:?}. Please enter an index:", ar);
            let mut index = String::new();
            // read_line returns a Result, that has a method `expect`
            // to print a message then crash the program if Result is the
            // variant (value) Err. If Result is Ok, expect just returns
            // the value that Ok is holding, which is the number of bytes in
            // the user input.
            io::stdin()
                .read_line(&mut index)
                .expect("Failed to read line");

            // parse can return any number types, so we need to specify it.
            let index: usize = index
                .trim()
                .parse()
                .expect("Index entered was not a number");
            // An alternative is
            // let index: usize = match index.trim().parse() {
            //     Ok(num) => num,
            //     Err(_) => do something,
            // };
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
    println!(
        "pattern matching to deconstruct a tuple for assignment: a = {}, c = {}",
        a, c
    );

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
 * Plays functions: https://doc.rust-lang.org/book/ch03-03-how-functions-work.html.
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

fn play_control_flows() {
    let number = 3;
    if number % 4 == 0 {
        println!("number divisible by 4");
    } else if number < 5 {
        println!("number less than 5");
    } else {
        println!("condition was false");
    }

    print_subsector_divider();
    // Notice that the arms in the expression have the be the same types.
    let x = if number == 3 { 2 } else { 5 };
    println!("let with if expression: {}", x);

    print_subsector_divider();
    let mut count = 0;
    // counting_up is a label for the loop, so it can be "break" or "continue"
    // to.
    'counting_up: loop {
        let mut remaining = 3;
        loop {
            println!("remaining = {}", remaining);
            if remaining == 1 {
                break;
            }
            if count == 2 {
                break 'counting_up;
            }
            remaining -= 1;
        }
        count += 1;
    }
    println!("ending count: {}", count);

    print_subsector_divider();
    count = 0;
    // You an even assign a value returned by a loop to a variable. If that's
    // the case, add ";" to the end of a loop because "let" is a statement
    // and "loop" is an expression.
    let result = 'counting_up_with_return_value: loop {
        let mut remaining = 3;
        loop {
            println!("remaining = {}", remaining);
            if remaining == 1 {
                break;
            }
            if count == 2 {
                break 'counting_up_with_return_value count * 10;
            }
            remaining -= 1;
        }
        count += 1;
    };
    println!("ending count: {}", count);
    println!("ending result: {}", result);

    print_subsector_divider();
    let mut number = 3;

    while number != 0 {
        println!("{}!", number);

        number -= 1;
    }
    println!("while loop, ended number with: {}", number);

    let ar = [1, 2, 3, 4, 5];
    for val in ar {
        println!("for-loop to loop through an array: {}", val);
    }

    // Start is inclusive, end is exclusive.
    for number in (1..4).rev() {
        println!("count down using for-loop: {}", number);
    }
}

/**
 * Plays random number generation.
 */
fn play_random_number() {
    // 1..101 means starting from 1 inclusive, ending at 101 exclusive. It's
    // equivalent to 1..=100.
    //
    // thread_rng returns a random number generator that is local to the
    // current thread and seeded by the operating system.
    let secret_number = rand::thread_rng().gen_range(1..101);
    println!("The secret number is {}", secret_number);

    let guess = 50;
    match guess.cmp(&secret_number) {
        Ordering::Less => println!("The secret number is less than 50"),
        Ordering::Greater => println!("The secret number is greater than 50"),
        Ordering::Equal => println!("The secret number is 50!"),
    }
}

/**
 * Plays ownerships.
 *
 * See https://doc.rust-lang.org/book/ch04-02-references-and-borrowing.html
 */
fn play_ownerships() {
    // This converts a string literal (immutable) to a String, which is mutable.
    // Notice the "mut" keyword. It's needed. Otherwise, we get
    // "cannot borrow `s` as mutable, as it is not declared as mutable" when
    // we do `s.push_str` below.
    let mut s = String::from("hello");

    // append a string to s
    s.push_str(", world!");
    println!("{}", s);

    // String is allocated on heap, so the behavior is different than scale
    // types in which assignment makes a copy implicitly. It has len() and
    // capacity() methods.
    //
    // s is no longer valid after the assignment to s2, which prevents double
    // free error, so accessing s after this assignment is invalid at
    // compile time. The assignment is actually a "move" instead of a copy
    // by pointer.
    // let s2 = s;

    // This is a real copy that allows s2 and s to exist after this line.
    let s2 = s.clone();
    println!("s = {}, s2 = {}", s, s2);

    takes_ownership(s2);
    // Now s2 cannot be used either, because it's freed when it is out of
    // scoped inside take_ownerships.

    let s3 = gives_ownerships();
    let s4 = takes_and_gives_ownership(s3);
    // s3 cannot be used, but we can use s4;
    println!(
        "s3 cannot be used, but s4 can be because the ownership s back: {}",
        s4
    );

    // Let calculate_length to borrow the variable s4.
    let len = calculate_length(&s4);
    println!("s4 = {s4}, length is {len}", s4 = s4, len = len);

    let mut s5 = String::from("i am a mutable string");
    change_string(&mut s5);
    println!("s5 is mutated to: {}", s5);

    // Mutable references have one big restriction: you can have only one mutable reference to a
    // particular piece of data at a time.
    // let r1 = &mut s5;
    // The following fails:
    // let r2 = &mut s5;
    //
    // A similar rule happens when combing mutable and immutable references.
    // let r1 = &s5;
    // let r2 = &s5; // ok, because immutable reference
    // let r3 = &mut s5; // not ok
    // ...use r1, r2, r3...
    //
    // The following is ok because the compiler can tell that the immutable
    // reference, r1, is done using, called Non-Lexical Lifetimes (NLL for short),
    // https://blog.rust-lang.org/2018/12/06/Rust-1.31-and-rust-2018.html#non-lexical-lifetimes
    let r1 = &s5;
    println!(
        "r1 is an immutable reference, and it's not used after this line = {}",
        r1
    );
    let r2 = &mut s5;
    println!("r2 is a mutable reference = {}", r2);
    // This is okay as well!
    let r3 = &mut s5;
    println!("r3 is a mutable reference = {}", r3);
    // This is not!
    // println!("r2 is a mutable reference = {}", r2);

    // If you have a reference to some data, the compiler will ensure that the data will not go out
    // of scope before the reference to the data does.
    // See the dangle() method below.
}

fn takes_ownership(s: String) {
    println!("string \"{}\" ownership got taken", s);
}

/**
 * Gives the ownership of a string to the caller.
 */
fn gives_ownerships() -> String {
    let s = String::from("ownership granted");
    s
}

/**
 * Takes the ownership of the string from the caller, but gives it back.
 */
fn takes_and_gives_ownership(s: String) -> String {
    s
}

/**
 * Gets the length of a string. This function only borrows the string
 * from the caller, so the ownership is not transferred.
 *
 * s is a reference to a string. Unlike a pointer, a reference always points
 * to a valid value.
 */
fn calculate_length(s: &String) -> usize {
    s.len()
}

fn change_string(s: &mut String) {
    s.push_str(": changed!");
}

// fn dangle() -> &String { // dangle returns a reference to a String
//
//     let s = String::from("hello"); // s is a new String
//
//     &s // we return a reference to the String, s
// } // Here, s goes out of scope, and is dropped. Its memory goes away.
//   // Danger

/**
 * Plays slices.
 */
fn play_slices() {
    let s = "a slice";
    let r1 = &s[1..4];
    let r2 = &s[2..5];
    let r3 = &s[..3];
    let r4 = &s[3..];
    // Indices cannot be out of bound.
    // let r5 = &s[3..10];
    println!("r1 = {}, r2 = {}, r3 = {}, r4 = {}", r1, r2, r3, r4);
    // Range indices must occur at valid utf-8 character boundaries.
    let fw = first_word(&s);
    println!("First word of \"{}\" is {}", s, fw);
    let s2 = "abcde";
    let fw = first_word(&s2);
    println!("First word of \"{}\" is {}", s2, fw);

    let ar = [1, 2, 3, 4, 5];
    println!("a slice of {:?} is {:?}", ar, &ar[1..3]);
}

/**
 * Returns the first word (words delimited by spaces) from a string.
 */
fn first_word(s: &str) -> &str {
    for (i, &b) in s.as_bytes().iter().enumerate() {
        if b == b' ' {
            return &s[0..i];
        }
    }
    return &s[..];
}

/**
 * Plays structs.
 *
 * https://doc.rust-lang.org/book/ch05-00-structs.html
 */
fn play_structs() {
    let user = User {
        email: String::from("email1"),
        username: String::from("user1"),
        active: true,
        sign_in_count: 1,
    };
    println!("user = {:?}", user);

    let user1 = build_user(String::from("email1"), String::from("user1"));
    let mut user2 = User {
        username: String::from("user2"),
        // This is called update syntax. The rest of the fields of user2 is
        // updated from user1.
        // Notice that this is just like an assignment, since the field
        // "email" is moved from user1 to user2, user1 is invalid now.
        ..user1
    };
    println!("user2 = {:?}", user2);

    // user2 is declared with mut, so we can mutate its fields. All fields
    // are mutable with the mut key word.
    user2.sign_in_count = 4;
    println!("user2 is mutated = {:?}", user2);

    let c = Color(1, 2, 3);
    let Color(d, e, f) = c;
    println!("struct tuple: {}, {}, {}, {}", d, e, f, c.0);

    let rect1 = Rectangle {
        width: 12,
        height: 20,
    };
    println!("print debug rect1 = {:?}", rect1);
    println!("pretty print debug rect1 = {:#?}", rect1);
    println!("area = {}", area(&rect1));
    println!("rect.area = {}", rect1.area());

    let scale = 2;
    // dbg! takes ownership of an expression, evaluates the expression, then
    // returns the ownership of the value.
    let rect2 = Rectangle {
        width: dbg!(12 * scale),
        height: 20,
    };
    // dbg! takes ownership of the parameter, use & to avoid the ownership being taken.
    dbg!(&rect2);
    println!(
        "rect2 is here after dbg!, because ownership not taken: {:?}",
        rect2
    );

    let a1 = AlwaysEqual;
    let a2 = AlwaysEqual;
    println!("AlwaysEqual == AlwaysEqual? {}", a1 == a2);

    let rect2 = Rectangle {
        width: 30,
        height: 50,
    };
    let rect3 = Rectangle {
        width: 10,
        height: 40,
    };
    let rect4 = Rectangle {
        width: 60,
        height: 45,
    };
    // Notice that & is optional for rect2 because Rust automatically adds &,
    // &mut, or * when calling a method. The feature is called
    // automatic referencing and dereferencing.
    println!("can rect2 hold rect3?: {}", &rect2.can_hold(&rect3));
    println!("can rect2 hold rect4?: {}", rect2.can_hold(&rect4));
    println!("square: {:?}", Rectangle::square(10));
}

fn print_message(m: &Message) {
    println!("message = {:?}", m);
}

fn build_user(email: String, username: String) -> User {
    User {
        // Because the field names are the same as parameters, we can use
        // a shortcut here.
        email,
        username,
        active: true,
        sign_in_count: 1, // trailing commas are ok for fields
    }
}

/**
 * A sample User struct. Usually a struct should own its fields to make sure
 * the lifetime of the field lives as long as the struct, so String is used
 * instead of &String.
 */
struct User {
    username: String,
    email: String,
    active: bool,
    sign_in_count: u64, // trailing commas are ok for fields
}

/**
 * User implemented the Debug trait.
 *
 * This allows for println!("{:?}", user);
 */
impl fmt::Debug for User {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(
            f,
            "User(email: {}, username: {}, active: {}, sign_in_count: {}",
            self.email, self.username, self.active, self.sign_in_count
        )
    }
}

struct Color(i32, i32, i32);

#[derive(PartialEq)]
struct AlwaysEqual;

// This is an outer attribute to opt in Debug, so Rust can print out the debug
// information for this struct.
#[derive(Debug)]
struct Rectangle {
    width: u32,
    height: u32,
}

// Implementation block for Rectangle.
// Functions are called associated functions.
impl Rectangle {
    // &self is short for "self: &Self". Methods must have this first parameter.
    fn area(&self) -> u32 {
        self.width * self.height
    }

    // A method without &self parameter is class method.
    fn square(size: u32) -> Rectangle {
        Rectangle {
            width: size,
            height: size,
        }
    }
}
// Can have multiple impl blocks
impl Rectangle {
    fn can_hold(&self, other: &Rectangle) -> bool {
        self.width > other.width && self.height > other.height
    }
}

fn area(rect: &Rectangle) -> u32 {
    rect.height * rect.width
}

/**
 * Play enums.
 *
 * https://doc.rust-lang.org/book/ch06-00-enums.html
 */
fn play_enums() {
    let m1 = Message::Quit;
    let m2 = Message::Move { x: 1, y: 2 };
    let m3 = Message::Write(String::from("write message"));
    print_message(&m1);
    print_message(&m2);
    print_message(&m3);

    if let Message::Move { x, y } = m2 {
        println!("Use the move x = {}, y = {}", x, y);
    }
    if let Message::Write(m) = m3 {
        println!("Write = {}", m);
    }

    m1.print();

    // Some and None (variants of the enum Option) instead of nulls.
    println!("plus_one with 5: {:?}", plus_one(Some(5)));
    println!("plus_one with none: {:?}", plus_one(None));

    let dice_roll = 9;
    match dice_roll {
        3 => add_fancy_hat(),
        7 => remove_fancy_hat(),
        other => move_player(other),
    }
    match dice_roll {
        3 => add_fancy_hat(),
        _ => println!("match all"),
    }
    match dice_roll {
        3 => add_fancy_hat(),
        // do nothing
        _ => (),
    }
}

#[derive(Debug)]
enum Message {
    Quit,
    Move { x: i32, y: i32 },
    Write(String),
}

// Enum can have methods like struct
impl Message {
    fn print(&self) {
        match self {
            // If more than statements, need {}
            Message::Quit => {
                let m = "quit";
                println!("message print: {}", m);
            }
            // Otherwise, just one statement with comma
            Message::Move { x, y } => println!("x = {}, y = {}", x, y),
            Message::Write(m) => println!("m = {}", m),
        }
    }
}

fn plus_one(x: Option<i32>) -> Option<i32> {
    match x {
        None => None,
        Some(i) => Some(i + 1),
    }
}

fn add_fancy_hat() {}
fn remove_fancy_hat() {}
fn move_player(num_spaces: u8) {
    println!("move player: {}", num_spaces);
}

/**
 * Play crates.
 *
 * https://doc.rust-lang.org/book/ch07-02-defining-modules-to-control-scope-and-privacy.html
 */
fn play_crates() {
    // use rust_playground::eat_at_restaurant() is not used,
    // so need the fully qualify name.
    rust_playground::eat_at_restaurant();

    println!("Call the re-exported add_to_waitlist method");
    rust_playground::add_to_waitlist();
}

/**
 * Play collections: Vector, String, Hash map
 *
 * https://doc.rust-lang.org/book/ch08-01-vectors.html
 */
fn play_vector() {
    let mut v: Vec<i32> = Vec::new();

    let v2 = vec![1, 2, 3];

    v.push(10);

    println!("v = {:?}", v);
    println!("v2 = {:?}", v2);

    // Two ways to get an element from a vector: &[] or get
    // The returned value is a reference so the Vector can still
    // own the data. Reference an element only borrows a value.

    let third = &v2[2];
    println!("Third of v2 is {}", third);

    match v.get(2) {
        Some(third) => println!("Third of v is {}", third),
        None => println!("Third of v does not exist"),
    }

    // You can’t have mutable and immutable references in the same scope. This
    // applies to vector as well.
    let v_val = v[0];
    println!("v_val is {} before mutation", v_val);
    v.push(4);

    // can't use "v_val" anymore
    // println!("v_val is {}", v_val);

    for i in &v {
        println!("v element {}", i);
    }

    // iterate over mutable references to change the value
    for i in &mut v {
        // This is deferencing i to mutate the value that i points to.
        *i += 50;
    }
    println!("After mutation, v = {:?}", v);

    println!("pop out a value from v: {:?}", v.pop());

    let mut vec1 = vec![1, 2, 3];
    push_int_to_vec(&mut vec1);
    println!("vec1 mutated: {:?}", vec1);
}

fn push_int_to_vec(vec: &mut Vec<i32>) {
    vec.push(4);
    vec[0] = 101;
}

/**
 * String
 *
 * https://doc.rust-lang.org/book/ch08-02-strings.html
 */
fn play_string() {
    let data = "initial contents";
    // s1 and s2 are just two different ways to create String
    let s1 = data.to_string();
    let s2 = String::from("s2 content");
    println!("{}, {}", s1, s2);

    let chinese_hello = String::from("你好");
    println!(
        "Utf-8 encoded strings can be used as well: {}",
        chinese_hello
    );

    let mut updatable_s = String::from("updatable string");
    updatable_s.push_str(": updated");

    let s101 = String::from("tic");
    let s102 = String::from("tac");
    let s103 = String::from("toc");

    // + is defined by the following signature.
    // Notice self is not a reference, so s101 is gone after this line, while
    // s102 and s103 stays.
    // fn add(self, s: &str) -> String
    let s = s101 + "-" + &s102 + "-" + &s103;
    println!("s is from +: {}", s);
    println!("s101 is gone after the + above: {}, {}", s102, s103);

    let s201 = String::from("tic");
    let s202 = String::from("tac");
    let s203 = String::from("toc");
    let s = format!("{}-{}-{}", s201, s202, s203);
    println!("s is from format!: {}", s);
    println!(
        "ownerships not taken away from the variables: {}, {}, {}",
        s201, s202, s203
    );

    println!(
        "For {}, len is {}, chars count is {}",
        chinese_hello,
        chinese_hello.len(),
        chinese_hello.chars().count()
    );

    // Cannot index a string, but can slice a string. The slice can only
    // valid characters or it will panic. In chinese_hello, first three
    // bytes make a valid character and thus a valid slice.
    let s_slice = &chinese_hello[0..3];
    println!("chinese hello slice: {}", s_slice);
    for b in chinese_hello.bytes() {
        println!("b = {}", b);
    }
    for c in chinese_hello.chars() {
        println!("c = {}", c);
    }
    let hindu_s = "नमस्ते";
    println!(
        "This is hindu: {}, len = {}, chars count is {}",
        hindu_s,
        hindu_s.len(),
        hindu_s.chars().count()
    );
    for c in hindu_s.chars() {
        println!("{}", c);
    }

    // The following depends on UnicodeSegmentation imported above
    let g = hindu_s.graphemes(true);
    println!("iterate over graphemes cluster boundries");
    // g moved due to this implicit call to `.into_iter()
    for s_in_g in g {
        println!("{}", s_in_g);
    }
    // cannot reuse g because the iteration above already used g
    let gvec = hindu_s.graphemes(true).collect::<Vec<&str>>();
    println!("It has {} characters", gvec.len());
}

/**
 * Hash map
 *
 * https://doc.rust-lang.org/book/ch08-03-hash-maps.html
 */
fn play_hash_map() {
    // No built-in macro to support hashmaps, unfortunately.
    // Also we need to import the HashMap above.
    let mut mutable_scores = HashMap::new();
    // Notice that for owned values like String, the value are moved and the HashMap
    // will be the owners of the values (key and value). &str implements
    // the Copy trait, so it's not a problem. It can be a problem if the
    // keys (or values) are created from: let s = String::from("key1")
    mutable_scores.insert("blue", 10);
    mutable_scores.insert("yellow", 50);

    println!("{:?}", mutable_scores);

    // Another way to create a map is by calling `collect`
    let teams = vec!["blue", "yellow"];
    let initial_scores = vec![10, 20];
    let scores: HashMap<_, _> = teams.into_iter().zip(initial_scores.into_iter()).collect();
    println!("scores from collect: {:?}", scores);

    // retrieve from a HashMap with get
    let team_name = String::from("blue");
    // Without the casting, get would interpret &team_name with the type &String,
    // but it needs &str, so we need to type cast it.
    // See https://stackoverflow.com/questions/65549983/trait-borrowstring-is-not-implemented-for-str
    // The type declartion of score is superfluous.
    let score: Option<&i32> = scores.get(&team_name as &str);

    if let Some(s) = score {
        println!("Score of {} is {}", team_name, s);
    }

    let mut type_printed = false;
    for (key, value) in &scores {
        if !type_printed {
            print!("Types of key and value of scores: ");
            print_type_of(key);
            print!(" => ");
            print_type_of(value);
            println!("");
            type_printed = true;
        }
        println!("{} => {}", key, value);
    }

    // Overrides blue with the value 99
    mutable_scores.insert("blue", 99);
    // Does nothing because "blue" is already in the map
    mutable_scores.entry("blue").or_insert(100);
    // Inserts green into the map because it does not exist before.
    mutable_scores.entry("green").or_insert(100);
    println!("mutable_scores after udpate: {:?}", mutable_scores);

    let text = "hello world wonderful world";
    let mut map = HashMap::new();
    for word in text.split_whitespace() {
        // or_insert returns a mutable reference that can be
        // used to mutate the entry.
        // The reference goes out of scope at the end of the iteration,
        // so it's permitted by the borrowing rules:
        // https://web.mit.edu/rust-lang_v1.25/arch/amd64_ubuntu1404/share/doc/rust/html/book/first-edition/references-and-borrowing.html#the-rules
        let count = map.entry(word).or_insert(0);
        *count += 1;
    }
    println!("count map: {:?}", map);
}

/**
 * Play with panicking or Result.
 *
 * https://doc.rust-lang.org/book/ch09-02-recoverable-errors-with-result.html
 */
fn play_panic() {
    // args[0] is the script itself, so the first parameter is at args[1].
    let args: Vec<String> = env::args().collect();
    println!("Use \"panic\" argument to panic out. Input parameters: {:?}, len = {}", args, args.len());
    if args.len() >= 2 {
        let operation = &args[1];
        if operation == "panic" {
            panic!("crash and burn");
        }
    }

    let f = File::open("/tmp/2022-05-30-hello.txt");
    let mut fname = "/tmp/2022-05-30-hello.txt";
    let mut f = match f {
        Ok(file) => file,
        Err(error) => match error.kind() {
            ErrorKind::NotFound => match File::create("/tmp/2022-05-30-hello2.txt") {
                Ok(fc) => {
                    fname = "/tmp/2022-05-30-hello2.txt";
                    fc
                },
                Err(e) => panic!("Problem creating the file: {:?}", e),
            },
            other_error => {
                panic!("Problem opening the file: {:?}", other_error);
            }
        }
    };

    if let Err(e) = f.write_all(b"hello world\n") {
        println!("error writing to a file: {:?}", e);
    } else {
        println!("{} is written", fname);
    }

    // Another way to write the code above using closure
    fname = "/tmp/2022-05-30-hello.txt";
    let mut f = File::open(&fname).unwrap_or_else(|error| {
        if error.kind() == ErrorKind::NotFound {
            fname = "/tmp/2022-05-30-hello2.txt";
            File::create("/tmp/2022-05-30-hello2.txt").unwrap_or_else(|error| {
                panic!("Problem creating the file: {:?}", error);
            })
        } else {
            panic!("Problem opening the file: {:?}", error);
        }
    });
    if let Err(e) = f.write_all(b"hello world\n") {
        println!("error writing to a file: {:?}", e);
    } else {
        println!("{} is written", fname);
    }

    println!("read_username_from_file: {:?}", read_username_from_file());
    println!("read_username_from_file2: {:?}", read_username_from_file2());
    println!("read_username_from_file3: {:?}", read_username_from_file3());
    println!("read_username_from_file4: {:?}", read_username_from_file4());
    let s = "\nabc";
    println!("last_char_of_first_line({}) = {:?}", s, last_char_of_first_line(s));
    let s = "abc\nabc";
    println!("last_char_of_first_line({}) = {:?}", s, last_char_of_first_line(s));

    // This converts Option to Result<char, Err(i32)>
    // https://doc.rust-lang.org/std/option/enum.Option.html#method.ok_or
    let option_to_result = last_char_of_first_line(s).ok_or(0);
    // This converts Result to Option
    // https://doc.rust-lang.org/std/result/enum.Result.html#method.ok
    let result_to_option = option_to_result.ok();
    println!("after converting to result then back to option: {:?}", result_to_option);

    // more in src/bin/main_return_box_error.rs
}

/// Read a username from a file.
/// From https://doc.rust-lang.org/book/ch09-02-recoverable-errors-with-result.html#propagating-errors
fn read_username_from_file() -> Result<String, io::Error> {
    let f = File::open("/tmp/2022-05-30-file-with-username");
    let mut f = match f {
        Ok(file) => file,
        Err(e) => return Err(e),
    };

    let mut s = String::new();

    match f.read_to_string(&mut s) {
        Ok(_) => Ok(s),
        Err(e) => Err(e),
    }
}

/// This is similar to read_username_from_file with "?" being used, so
/// that Err is propagated to the caller.
fn read_username_from_file2() -> Result<String, io::Error> {
    let mut f = File::open("/tmp/2022-05-30-file-with-username")?;

    let mut s = String::new();
    f.read_to_string(&mut s)?;
    return Ok(s);
}

/// Simplify read_username_from_file2 by chaining ?
fn read_username_from_file3() -> Result<String, io::Error> {
    let mut s = String::new();
    File::open("/tmp/2022-05-30-file-with-username")?.read_to_string(&mut s)?;
    return Ok(s);
}

/// read_username_from_file3 by using fs::read_to_string
fn read_username_from_file4() -> Result<String, io::Error> {
    fs::read_to_string("/tmp/2022-05-30-file-with-username")
}

/// Gets the last character of the first line of the given text.
/// From https://doc.rust-lang.org/book/ch09-02-recoverable-errors-with-result.html#where-the--operator-can-be-used
fn last_char_of_first_line(text: &str) -> Option<char> {
    text.lines().next()?.chars().last()
}

/**
 * Play generics.
 */
fn play_generics() {
    let nums = vec![2, 1, 5, 4];
    println!("largest of {:?} is {:?}", nums, largest(&nums));

    let p1 = Point{x: 3.0, y:4.0};
    println!("{:?} has a x value: {}", p1, p1.x());
    println!("Its distance to origin is {}", p1.distance_from_origin());

    let p2 = Point{x: "i am x", y: "i am y"};
    let p3 = p1.mixup(p2);
    // // Notice the following is invalid it would use moved values (they
    // // were moved to mixup and be cleaned up).
    // println!("p1: {:?}, p2: {:?}", p1, p2);
    println!("p3 is mixed: {:?}", p3);
}

/// A generic method to get the largest element from a vector.
fn largest<T: std::cmp::PartialOrd>(list: &[T]) -> &T{
    let mut largest = &list[0];
    for item in list {
        if item > largest {
            largest = item;
        }
    }
    return largest;
}

/// Point has x and y, which can be two differen types.
///
/// Using generic types won't make your run any slower than it would with concrete types.  Rust
/// accomplishes this by performing monomorphization of the code using generics at compile time.
/// Monomorphization is the process of turning generic code into specific code by filling in the
/// concrete types that are used when compiled.
/// Source: https://doc.rust-lang.org/book/ch10-01-syntax.html
#[derive(Debug)]
struct Point<X1,Y1> {
    x: X1,
    y: Y1,
}

impl<X1, Y1> Point<X1, Y1> {
    fn x(&self) -> &X1{
        &self.x
    }

    /// Returns a new Point that has the x of self, and the y of other.
    /// Both self and other have their ownerships transferred.
    fn mixup<X2, Y2>(self, other: Point<X2, Y2>) -> Point<X1, Y2> {
        Point {
            x: self.x,
            y: other.y,
        }
    }
}

/// Only implements a concrete type for Point
impl Point<f32, f32> {
    fn distance_from_origin(&self) -> f32 {
        (self.x.powi(2) + self.y.powi(2)).sqrt()
    }
}

/**
 * Play traits.
 */
fn play_traits() {
    let t = Tweet {
        username: String::from("user1"),
        content: String::from("content1"),
    };

    let n = NewArticle {
        content: String::from("new article content"),
    };

    notify(&t);
    notify_with_default(&n);

    let chars_vec = vec!['t', 'b', 'v'];
    println!("largest_for_primitive({:?}) = {}", chars_vec, largest_for_primitive(&chars_vec));

    let t2 = Tweet {
        username: String::from("user2"),
        content: String::from("content2"),
    };
    println!("{}", summary_and_themselves(&t, &t2));

    println!("new summary: {}", new_summary().summarize());
}

/// Define a trait bound (shortcut) for generic types
fn notify(item: &impl Summary) {
    println!("notify: {}", item.summarize());
}

/// Another way to define a trait bound
fn notify_with_default<T: Summary>(item: &T) {
    println!("notify with default: {}", item.summarize_with_default());
}

/// Only accepts types which implement PartialOrd and Copy.
/// Copy allows us to get list[0] instead of &list[0] for largest because we
/// can get a copy of list[0].
fn largest_for_primitive<T: std::cmp::PartialOrd + Copy>(list: &[T]) -> T {
    let mut largest = list[0];
    for &item in list {
        if item > largest {
            largest = item;
        }
    }
    return largest;
}

/// Use where to put trait bounds at the end of a method signature.
fn summary_and_themselves<T, U>(item1: &T, item2: &U) -> String where T:Summary + std::fmt::Debug, U:Summary + std::fmt::Debug{
    format!("{:?} summary is {}, {:?} summary is {}", item1, item1.summarize(), item2, item2.summarize())
}

/// A return type with a trait bound. Notice that we can only return one
/// concrete type. See
/// https://doc.rust-lang.org/book/ch17-02-trait-objects.html#using-trait-objects-that-allow-for-values-of-different-types
/// for how to work around that.
fn new_summary() -> impl Summary {
    Tweet {
        username: String::from("new_user"),
        content: String::from("new_content"),
    }
    // Some condition to return NewArticle is not allowed because generics become
    // concrete types at compile time. A return type with a trait bound does
    // not let the function to return differen types
}

trait Summary {
    fn summarize(&self) -> String;

    fn summarize_with_default(&self) -> String {
        format!("default, non-default={}", self.summarize())
    }
}

#[derive(Debug)]
struct Tweet {
    username: String,
    content: String,
}

struct NewArticle {
    content: String,
}

impl Summary for Tweet {
    fn summarize(&self) -> String {
        format!("{}: {}", self.username, self.content)
    }
}

impl Summary for NewArticle {
    fn summarize(&self) -> String {
        format!("{}", self.content)
    }

    fn summarize_with_default(&self) -> String {
        format!("override the default to ignore summarize in summarize_with_default")
    }
}

/**
 * Play lifetimes
 *
 * https://doc.rust-lang.org/book/ch10-03-lifetime-syntax.html
 */
fn play_lifetimes() {
    let string1 = String::from("abcd");
    let string2 = "xyz";

    let result = longest(string1.as_str(), string2);
    println!("among \"{}\" and \"{}\", \"{}\" is longer", string1, string2, result);

    // let result4;
    {
        let string3 = String::from("xyz");
        let result2 = longest(string1.as_str(), string3.as_str());
        println!("This works because result2 has the same lifetime as string3: {}", result2);
        // The following does not work because result4 has a longer lifetime
        // than result2. The longest function requires the return value to
        // have the shorter lifetime of the parameters.
        // result4 = longest(string1.as_str(), string3.as_str());
    }

    longest_with_an_announcement(string1.as_str(), string2, "announcement is here!");

    let novel = String::from("Call me Ishmael. Some years ago...");
    let first_sentence = novel.split('.').next().expect("Could not find a '.'");
    let imp = ImportantExcerpt {
        part: first_sentence,
    };
    println!("first sentence: {}", imp.announce_and_return_part("with method announcement"));
}

// Lifetime annotations do not change the lifetime of the variables. It just
// helps the compiler to know the relationships of lifetimes between the
// variables.
//
// &i32        // a reference
// &'a i32     // a reference with an explicit lifetime
// &'a mut i32 // a mutable reference with an explicit lifetime
//
// The use of lifetime annotations co-exist with generic lifetime parameters
// for functions. Generic lifetime parameters are inside angle brackets between
// the function name and the parameter list.
//
// In the following, the lifetime annotations indicate that the lifetime of
// the returned value is the same as the function inputs, whichever is shorter (
// because that's the lowest common denominator).
fn longest<'a>(s1: &'a str, s2: &'a str) -> &'a str {
    if s1.len() > s2.len() {
        s1
    } else {
        s2
    }
}

// the syntax of specifying generic type parameters, trait bounds, and lifetimes all in one function
fn longest_with_an_announcement<'a, T>(
    x: &'a str,
    y: &'a str,
    ann: T,
    ) -> &'a str
where
    T: Display,
{
    println!("Announcement! {}", ann);
    if x.len() > y.len() {
        x
    } else {
        y
    }
}

// If a struct has references, we need to use lifetime annotations.
struct ImportantExcerpt<'a> {
    part: &'a str,
}

impl<'a> ImportantExcerpt<'a> {
    fn announce_and_return_part(&self, announcement: &str) -> &str {
        println!("announce within method: {}", announcement);
        self.part
    }
}


// end of playing methods

/**
 * Prints a header.
 */
fn print_header(header: &str) {
    println!("\n=====\n===== {header} =====\n=====\n", header = header)
}

fn print_subsector_divider() {
    println!("\n-\n");
}

// This is from https://stackoverflow.com/a/58119924/3321334
// and looks like the only general way to print a type of a variable.
fn print_type_of<T>(_: &T) {
    print!("{}", std::any::type_name::<T>())
}
