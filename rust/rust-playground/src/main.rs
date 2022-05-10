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
use std::env;
use std::fmt;
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
}
// end of main

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
