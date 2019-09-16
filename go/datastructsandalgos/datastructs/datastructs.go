// Samples for data structures, like lists, tuples, heaps, etc.
package main

import (
	"container/list"
	"fmt"
)

func main() {
	printSection("List")
	listSamples()

	printSection("Tuples")
	tupleSamples()
}

// end of main

func listSamples() {
	var intList list.List
	intList.PushBack(11)
	intList.PushBack(23)
	intList.PushBack(34)

	for element := intList.Front(); element != nil; element = element.Next() {
		fmt.Println(element.Value.(int))
	}
}

func tupleSamples() {
	square, cube := powerSeries(3)
	fmt.Println("Square", square, "Cube", cube)
}

// end of samples

func powerSeries(a int) (int, int) {
	return a * a, a * a * a
}

func printSection(header string) {
	fmt.Printf("\n-- %s --\n\n", header)
}
