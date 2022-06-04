//! From https://stackoverflow.com/questions/32682876/is-there-any-way-to-return-a-reference-to-a-variable-created-in-a-function
use std::{
    fs::{OpenOptions, File},
    io::{self, Write},
};

fn trycreate() -> io::Result<File> {
    return OpenOptions::new()
        .create(true)
        .write(true)
        .append(true)
        .open("/tmp/2022-06-03-hello-text.txt");
}

fn main() -> io::Result<()> {
    let mut f = trycreate()?;

    f.write_all(b"test1\n")?;
    f.write_all(b"test2\n")?;

    Ok(())
}

