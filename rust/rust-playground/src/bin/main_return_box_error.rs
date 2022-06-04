use std::error::Error;
use std::fs::File;

/// This is from https://doc.rust-lang.org/book/ch09-02-recoverable-errors-with-result.html
/// Box<dyn Error> means any error, so this
/// allows one to use the "?" operator inside the main function directly by
/// making main returns a Result.
fn main() -> Result<(), Box<dyn Error>> {
    File::open("hello.txt")?;

    Ok(())
}
