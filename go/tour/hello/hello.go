// Command hello is a bunch of simple snippets from the tour of Go.
package main

import (
	"fmt"
	"math"
	"math/cmplx"
	"math/rand"
	"runtime"
	"time"
)

var c, python, java bool

var (
	c2, python2, java2 bool
	num                int
)

func main() {
	// defer's are executed in LIFO
	defer fmt.Println("The End!")
	defer printSectionSeparator()

	rand.Seed(time.Now().UTC().UnixNano())

	fmt.Println("Hello world")
	fmt.Println("My favorite number is", rand.Intn(10))
	fmt.Println(math.Pi) // Pi is an exported varialbe, pi is not (package private).

	printSection("time formatting")
	timeFormatting()

	printSection("simple functions")
	fmt.Println("add(3, 4) =", add(3, 4))

	s1, s2 := swap("apple", "boy")
	fmt.Println("swap(\"apple\", \"boy\") =", s1, s2)

	fmt.Println("nakedReturn(100) // 100 degree celsius to fahrenheit")
	fmt.Println(nakedReturn(100))

	printSection("variable declaractions")
	var i int
	fmt.Println(i, c, java, python, c2, java2, python2, num)

	c3, python3, java3 := true, false, "no!"
	fmt.Println(c3, java3, python3)

	var c4, python4, java4 = true, false, "no!"
	fmt.Println(c4, java4, python4)

	printSection("different types")

	// bool
	//
	// string
	//
	// int  int8  int16  int32  int64
	// uint uint8 uint16 uint32 uint64 uintptr
	//
	// byte // alias for uint8
	//
	// rune // alias for int32
	//      // represents a Unicode code point
	//
	// float32 float64
	//
	// complex64 complex128
	var (
		ToBe     bool       = false
		MaxInt32 uint64     = 1<<64 - 1
		MaxInt64 uint64     = 1<<32 - 1
		z        complex128 = cmplx.Sqrt(-5 + 12i)
	)
	fmt.Printf("Type: %T Value: %v\n", ToBe, ToBe)
	fmt.Printf("Type: %T Value: %v\n", MaxInt32, MaxInt32)
	fmt.Printf("Type: %T Value: %v\n", MaxInt64, MaxInt64)
	fmt.Printf("Type: %T Value: %v\n", z, z)

	printSection("Type conversions")

	int1 := 42
	float1 := float64(int1)
	uint1 := uint(float1)
	float2 := math.Sqrt(float64(30))
	uint2 := uint(float2)
	fmt.Println(int1, float1, uint1, float2, uint2)

	printSection("Type inference")
	int3 := 42
	float3 := 3.142
	complex3 := 0.867 + 0.5i
	fmt.Printf("Type %T = %v\n", int3, int3)
	fmt.Printf("Type %T = %v\n", float3, float3)
	fmt.Printf("Type %T = %v\n", complex3, complex3)

	const Pi = 3.14
	fmt.Printf("Type %T = %v\n", Pi, Pi)

	printSection("Constant types based on context")
	constantsDependsOnContext()

	printSection("For loop")
	for i := 0; i < 10; i++ {
		fmt.Println(i)
	}

	// while loop
	sum := 0
	for sum < 10 {
		fmt.Println("Sum is", sum)
		sum++
	}

	printSection("If")
	fmt.Println(sqrt(2), sqrt(-4))

	fmt.Println(pow(3, 2, 10), pow(3, 3, 20))

	greetingWithTime()

	printSection("exercise with loops")
	fmt.Println(mySqrt(2))

	printSection("switch")
	fmt.Print("Go runs on ")
	switch os := runtime.GOOS; os {
	case "darwin":
		fmt.Println("OS X.")
	case "linux":
		fmt.Println("Linux.")
	default:
		fmt.Printf("%s.\n", os)
	}

	greetingWithTimeUsingSwitch()

	printSection("Pointers")
	pointersSample()

	printSection("Structs")
	structsSample()
}

// end of main

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

func printSection(header string) {
	fmt.Printf("\n-- %s --\n\n", header)
}

func printSubsectionSeparator() {
	fmt.Println("\n")
}

func printSubsection(subheader string) {
	fmt.Printf("\n%s\n\n", subheader)
}

func add(x, y int) int {
	return x + y
}

func swap(a, b string) (string, string) {
	return b, a
}

// Go's return values may be named. If so, they are treated as variables
// defined at the top of the function.
//
// These names should be used to document the meaning of the return values.
//
// A return statement without arguments returns the named return values. This
// is known as a "naked" return.
//
// Naked return statements should be used only in short functions, as with the
// example shown here. They can harm readability in longer functions.
func nakedReturn(c int) (cdegree, fdegree int) {
	cdegree = c
	fdegree = c*9/5 + 32
	return
}

func constantsDependsOnContext() {
	const (
		// Create a huge number by shifting a 1 bit left 100 places.
		// In other words, the binary number that is 1 followed by 100 zeroes.
		Big = 1 << 100
		// Shift it right again 99 places, so we end up with 1<<1, or 2.
		Small = Big >> 99
	)
	fmt.Println(needInt(Small))
	fmt.Println(needFloat(Small))
	fmt.Println(needFloat(Big))
	fmt.Printf("Type %T = %v\n", Small, Small)
	// Error if uncomment the following:
	// fmt.Printf("Type %T = %v\n", Big, Big)
}
func needInt(x int) int { return x*10 + 1 }
func needFloat(x float64) float64 {
	return x * 0.1
}

func sqrt(x float64) string {
	if x < 0 {
		return sqrt(-x) + "i"
	}
	return fmt.Sprint(math.Sqrt(x))
}

// if with a short statement
func pow(x, n, lim float64) float64 {
	if v := math.Pow(x, n); v < lim {
		// v is only in scope until the end of if/else
		return v
	} else {
		fmt.Printf("%g >= %g\n", v, lim)
	}
	return lim
}

func mySqrt(x float64) float64 {
	z := 1.0
	for i := 0; i < 10; i++ {
		fmt.Println(z)
		z -= (z*z - x) / (2 * z)
	}
	return z
}

func greetingWithTime() {
	if t := time.Now(); t.Hour() < 12 {
		fmt.Println("Good morning!")
	} else if t.Hour() < 17 {
		fmt.Println("Good afternoon!")
	} else {
		fmt.Println("Good evening!")
	}
}

func greetingWithTimeUsingSwitch() {
	t := time.Now()
	switch {
	case t.Hour() < 12:
		fmt.Println("Good morning!")
	case t.Hour() < 17:
		fmt.Println("Good afternoon!")
	default:
		fmt.Println("Good evening!")
	}
}

func pointersSample() {
	var p *int
	i := 42

	p = &i
	fmt.Println(p)
	fmt.Println(*p)

	*p = 21
	fmt.Println("After assigning *p:", *p)

	fmt.Println("Unlike C, Go has no pointer arithmetic.")
}

func structsSample() {
	v := Vertex{1, 2}
	fmt.Println(v)
	fmt.Printf("Type %T: %v\n", v, v)
	fmt.Printf("X = %d \n", v.X)

	p := &v
	// p.X is a shortcut for (*p).X
	fmt.Printf("p.X = %d\n", p.X)
	p.X = 101
	fmt.Printf("After changing p.X, v = %v\n", v)

	printSubsection("struct literals")
	v1 := Vertex{X: 102}
	v2 := Vertex{}
	p2 := &Vertex{5, 7}
	fmt.Println(v1, v2, p2)
}

type Vertex struct {
	X, Y int
}
