mod front_of_house {
    pub mod hosting {
        pub fn add_to_waitlist() {
            println!("add_to_waitlist");
        }

        pub fn seat_at_table() {
            println!("seat_at_table");
        }
        pub fn call_serving() {
            println!("call_serving");
            super::serving::do_all();
        }
    }
    mod serving {
        fn take_order() {
            println!("take_order");
            // super means the parent of the serving module here,
            // which is front_of_house.
            super::hosting::add_to_waitlist();
            // We can call serve_order directly because this function and serve_order are in the
            // same module.
            println!("calling serve_order in side take_order");
            serve_order();
        }

        fn serve_order() {
            println!("serve_order");
        }

        fn take_payment() {
            println!("take_payment");
        }

        pub fn do_all() {
            println!("do_all");
            take_order();
            serve_order();
            take_payment();
        }
    }
}

mod back_of_house {
    #[derive(Debug)]
    pub struct Breakfast {
        // Fields inside a pub class are private by default.
        pub toast: String,
        seasonal_fruit: String,
    }

    impl Breakfast {
        // Because the struct has a private field, we need a
        // public method to create it.
        pub fn summer(toast: &str) -> Breakfast {
            Breakfast {
                toast: String::from(toast),
                seasonal_fruit: String::from("peaches"),
            }
        }

        pub fn my_fruit(&self) -> &str {
            return &self.seasonal_fruit;
        }
    }

    // If an enum is pubic, the variants are public as well.
    #[derive(Debug)]
    pub enum Appetizer {
        Soup,
        Salad,
    }
}

// Bring the module into the scope of eat_at_restaurant.
use crate::front_of_house::hosting;
// relative path also works
use self::back_of_house:: Appetizer;

// It's a convention to bring the module in then call module::function().
//
// On the other hand, when bringing in structs, enums, and other items with use, it’s idiomatic to specify the full path
// 
// If it needs the parent module disambiguate, bring in the parent module, then
// do parent_module::Class in code. Or we can do:
// use parent_module::Class as Class2.

// Re-exporting the add_to_waitlist function inside hosting module, so external modules can call hosting without front_of_house.
pub use crate::front_of_house::hosting::add_to_waitlist;
// Use the following to re-export two functions from the same module.
//   pub use crate::front_of_house::{add_to_waitlist, seat_at_table}
//
// "self" works as well, so
//   use std::io::{self, Write};
// brings in std::io and std::io::Write.
//
// glob operator works as well:
//   use std::collections::*;


/// All items (functions, methods, structs, enums, modules, and constants) are private by default.
/// Items in a parent module can’t use the private items inside child modules, but items in child
/// modules can use the items in their ancestor modules.
pub fn eat_at_restaurant() {
    // Absolute path
    crate::front_of_house::hosting::add_to_waitlist();

    // Relative path
    front_of_house::hosting::seat_at_table();

    println!("{:?}", back_of_house::Appetizer::Soup);
    println!("{:?}", back_of_house::Appetizer::Salad);
    println!("using the use that brings Appetizer into scope: {:?}", Appetizer::Salad);

    let breakfast = back_of_house::Breakfast::summer("my toast");
    println!("breakfast toast: {:?}, fruit: {}", breakfast, breakfast.my_fruit());

    hosting::call_serving();

    extra_module::extra_module_fn();
}

// This tells Rust to load the content from the file with the same name in
// the same directory as this.
//
// The module tree is as-of the module is defined here.
mod extra_module;
