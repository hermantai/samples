// Command hello is a bunch of simple snippets from the tour of Go.
package main

import (
	"fmt"
	"math"
	"math/rand"
	"time"
)

func main() {
	rand.Seed(time.Now().UTC().UnixNano())

	fmt.Println("Hello world")
	fmt.Println("My favorite number is", rand.Intn(10))
	fmt.Println(math.Pi) // Pi is an exported varialbe, pi is not (package private).

	printSectionSeparator()
	timeFormatting()
}

// From: https://gobyexample.com/time-formatting-parsing
func timeFormatting() {
	p := fmt.Println

	t := time.Now()
	p("Current time:", t.Format(time.RFC3339))

	p("Using this format to format time: Mon Jan 2 15:04:05 MST 2006")

	printTimeFormatAndFormattedTime(t, "3:04PM")
	printTimeFormatAndFormattedTime(t, "Mon Jan _2 15:04:05 2006")
	printTimeFormatAndFormattedTime(t, "2006-01-02T15:04:05.999999-07:00")

	printSubsectionSeparator()

	fmt.Println("Show time's fields:")
	fmt.Printf("%d-%02d-%02dT%02d:%02d:%02d\n",
		t.Year(), t.Month(), t.Day(),
		t.Hour(), t.Minute(), t.Second())

	printSubsectionSeparator()

	fmt.Println("Error is like this:")
	ansic := "Mon Jan _2 15:04:05 2006"
	_, e := time.Parse(ansic, "8:41PM")
	p(e)
}

func printTimeFormatAndFormattedTime(t time.Time, format string) {
	fmt.Println(format, "=>", t.Format(format))
}

func printSectionSeparator() {
	fmt.Println("\n-----\n")
}

func printSubsectionSeparator() {
	fmt.Println("\n")
}
