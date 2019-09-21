// Package commonutil contains utilities for console like printing a header, etc.
package commonutil

import "fmt"

// PrintSectionSeparator prints a header with "-".
func PrintSectionSeparator() {
	fmt.Println("\n-----\n")
}

// PrintSection prints a header like PrintSectionSeparator but with a string.
func PrintSection(header string) {
	fmt.Printf("\n-- %s --\n\n", header)
}

// PrintSubsectionSeparator prints a subheader.
func PrintSubsectionSeparator() {
	fmt.Println("\n")
}

// PrintSubsection prints a subheader like PrintSubsectionSeparator with a string.
func PrintSubsection(subheader string) {
	fmt.Printf("\n%s\n\n", subheader)
}

// PrintTypeAndValue prints the type of v and its value.
func PrintTypeAndValue(v interface{}) {
	fmt.Printf("Type %T: %v\n", v, v)
}
