// Command hello is a bunch of simple snippets from the tour of Go.
package main

import (
	"fmt"
	"github.com/hermantai/samples/go/commonutil"
	"math"
	"math/cmplx"
	"math/rand"
	"runtime"
	"strings"
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
	defer commonutil.PrintSectionSeparator()

	rand.Seed(time.Now().UTC().UnixNano())

	fmt.Println("Hello world")
	fmt.Println("My favorite number is", rand.Intn(10))
	fmt.Println(math.Pi) // Pi is an exported varialbe, pi is not (package private).

	commonutil.PrintSection("time formatting")
	timeFormatting()

	commonutil.PrintSection("simple functions")
	fmt.Println("add(3, 4) =", add(3, 4))

	s1, s2 := swap("apple", "boy")
	fmt.Println("swap(\"apple\", \"boy\") =", s1, s2)

	fmt.Println("nakedReturn(100) // 100 degree celsius to fahrenheit")
	fmt.Println(nakedReturn(100))

	commonutil.PrintSection("variable declaractions")
	var i int
	fmt.Println(i, c, java, python, c2, java2, python2, num)

	c3, python3, java3 := true, false, "no!"
	fmt.Println(c3, java3, python3)

	var c4, python4, java4 = true, false, "no!"
	fmt.Println(c4, java4, python4)

	commonutil.PrintSection("different types")

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

	commonutil.PrintSection("Type conversions")

	int1 := 42
	float1 := float64(int1)
	uint1 := uint(float1)
	float2 := math.Sqrt(float64(30))
	uint2 := uint(float2)
	fmt.Println(int1, float1, uint1, float2, uint2)

	commonutil.PrintSection("Type inference")
	int3 := 42
	float3 := 3.142
	complex3 := 0.867 + 0.5i
	fmt.Printf("Type %T = %v\n", int3, int3)
	fmt.Printf("Type %T = %v\n", float3, float3)
	fmt.Printf("Type %T = %v\n", complex3, complex3)

	const Pi = 3.14
	fmt.Printf("Type %T = %v\n", Pi, Pi)

	commonutil.PrintSection("Constant types based on context")
	constantsDependsOnContext()

	commonutil.PrintSection("For loop")
	for i := 0; i < 10; i++ {
		fmt.Println(i)
	}

	// while loop
	sum := 0
	for sum < 10 {
		fmt.Println("Sum is", sum)
		sum++
	}

	commonutil.PrintSection("If")
	fmt.Println(sqrt(2), sqrt(-4))

	fmt.Println(pow(3, 2, 10), pow(3, 3, 20))

	greetingWithTime()

	commonutil.PrintSection("exercise with loops")
	fmt.Println(mySqrt(2))

	commonutil.PrintSection("switch")
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

	commonutil.PrintSection("Pointers")
	pointersSample()

	commonutil.PrintSection("Structs")
	structsSample()

	commonutil.PrintSection("Arrays")
	arraysSample()

	commonutil.PrintSection("Slices")
	slicesSample()

	commonutil.PrintSection("Maps")
	mapsSample()

	commonutil.PrintSection("Function as values")
	functionAsValuesSample()
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

	commonutil.PrintSubsectionSeparator()

	fmt.Println("Show time's fields:")
	fmt.Printf("%d-%02d-%02dT%02d:%02d:%02d\n",
		t.Year(), t.Month(), t.Day(),
		t.Hour(), t.Minute(), t.Second())

	commonutil.PrintSubsectionSeparator()

	fmt.Println("Error is like this:")
	ansic := "Mon Jan _2 15:04:05 2006"
	_, e := time.Parse(ansic, "8:41PM")
	p(e)
}

func printTimeFormatAndFormattedTime(t time.Time, format string) {
	fmt.Println(format, "=>", t.Format(format))
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

	commonutil.PrintSubsection("struct literals")
	v1 := Vertex{X: 102}
	v2 := Vertex{}
	p2 := &Vertex{5, 7}
	fmt.Println(v1, v2, p2)
}

type Vertex struct {
	X, Y int
}

func arraysSample() {
	var a [2]string
	a[0] = "1st-element"
	a[1] = "2nd-element"
	fmt.Println(a[0], a[1])
	fmt.Println("Whole array", a)

	primes := [6]int{2, 3, 5, 7, 11, 13}
	commonutil.PrintTypeAndValue(primes)

	// primes2 is actually a slice
	primes2 := []int{2, 3, 5, 7, 11, 13}
	commonutil.PrintTypeAndValue(primes2)

	fmt.Println("Arrays cannot be resized.")
}

func slicesSample() {
	nums := [6]int{0, 1, 2, 3, 4, 5}

	var s []int = nums[1:4]
	fmt.Print("nums[1:4] = ")
	commonutil.PrintTypeAndValue(s)

	s2 := nums[3:5]
	fmt.Println("s, s2:", s, s2)
	s2[0] = 101
	fmt.Println("after s[0] = 101, s, s2:", s, s2)

	s3 := s[:]
	printSliceWithName("s2", s2)
	printSliceWithName("s3", s3)

	slicesOfStructs := []struct {
		i int
		b bool
	}{
		{2, true},
		{3, false},
		{5, true},
		{7, true},
		{11, false},
		{13, true},
	}
	fmt.Println("slicesOfStructs:")
	commonutil.PrintTypeAndValue(slicesOfStructs)

	// The length of a slice is the number of elements it contains.
	// The capacity of a slice is the number of elements in the underlying
	// array, counting from the first element in the slice.
	fmt.Println("different printing of s4")
	s4 := []int{2, 3, 5, 7, 11, 13}
	printSlice(s4)

	// Slice the slice to give it zero length.
	s4 = s4[:0]
	printSlice(s4)

	// Extend its length.
	s4 = s4[:4]
	printSlice(s4)

	// Drop its first two values.
	s4 = s4[2:]
	printSlice(s4)

	fmt.Println("zero value of a slice is nil. E.g. s5")
	var s5 []int
	fmt.Println(s5, len(s5), cap(s5))
	if s5 == nil {
		fmt.Println("s5 is nil")
	}

	// This is how you create dynamically-sized array.
	a := make([]int, 5)
	printSliceWithName("a", a)

	b := make([]int, 0, 5)
	printSliceWithName("b", b)

	// Create a tic-tac-toe board
	board := [][]string{
		[]string{"_", "_", "_"},
		[]string{"_", "_", "_"},
		[]string{"_", "_", "_"},
	}

	// The players take turns.
	board[0][0] = "X"
	board[2][2] = "O"
	board[1][2] = "X"
	board[1][0] = "O"
	board[0][2] = "X"

	commonutil.PrintSubsection("Tic-tac-toe")
	for i := 0; i < len(board); i++ {
		fmt.Printf("%s\n", strings.Join(board[i], " "))
	}

	var growingSlice []int
	printSliceWithName("growingSlice", growingSlice)

	growingSlice = append(growingSlice, 12)
	printSliceWithName("growingSlice", growingSlice)

	growingSlice = append(growingSlice, 13, 14, 15)
	printSliceWithName("growingSlice", growingSlice)

	fmt.Println("Print a slice with a for loop")
	for i, v := range growingSlice {
		fmt.Printf("%d: %d\n", i, v)
	}

	fmt.Println("Only index")
	for i := range growingSlice {
		fmt.Println(i)
	}
	fmt.Println("Only value")
	for _, v := range growingSlice {
		fmt.Println(v)
	}
}

type Vertex2 struct {
	Lat, Long float64
}

func mapsSample() {
	// m is nil with no keys. No keys can be added, either.
	var m map[string]Vertex2

	m = make(map[string]Vertex2) // now m is ready
	m["Bell labs"] = Vertex2{
		12.123, 22.23,
	}
	fmt.Println(m)
	fmt.Println(m["Bell labs"])

	fmt.Println("map literal")
	m2 := map[string]Vertex2{
		"Bell labs": Vertex2{
			40.68433, -74.39967,
		},
		"Google": Vertex2{
			37.42202, -122.08408,
		},
	}
	fmt.Println(m2)

	fmt.Println("skip the Vertex2 type name")
	var m3 = map[string]Vertex2{
		"Bell labs": {
			40.68433, -74.39967,
		},
		"Google": {
			37.42202, -122.08408,
		},
	}
	fmt.Println(m3)

	fmt.Println("map mutations")
	m4 := make(map[string]int)
	m4["Answer"] = 42
	fmt.Println("The value:", m4["Answer"])
	m4["Answer"] = 48
	fmt.Println("The value:", m4["Answer"])
	delete(m4, "Answer")
	fmt.Println("The value:", m4["Answer"])
	v, ok := m4["Answer"]
	fmt.Println("The value:", v, "Present?", ok)
}

func functionAsValuesSample() {
	hypot := func(x, y float64) float64 {
		return math.Sqrt(x*x + y*y)
	}
	fmt.Println(compute(hypot))

	pos, neg := adder(), adder()

	for i := 0; i < 10; i++ {
		fmt.Println(pos(i), neg(-2*i))
	}

	fmt.Println("fibonacci sequence:")
	fib := fibonacci()
	for i := 0; i < 10; i++ {
		fmt.Println(fib())
	}
}

func compute(fn func(float64, float64) float64) int {
	return int(fn(3, 4))
}

func adder() func(int) int {
	sum := 0
	return func(x int) int {
		sum += x
		return sum
	}
}

func fibonacci() func() int {
	i1, i2 := 0, 1
	return func() (r int) {
		r = i1
		i1, i2 = i2, i1+i2
		return
	}
}

// end of samples

func printSlice(s []int) {
	fmt.Printf("len=%d cap=%d %v\n", len(s), cap(s), s)
}

func printSliceWithName(s string, x []int) {
	fmt.Printf("%s len=%d cap =%d %v\n", s, len(x), cap(x), x)
}
