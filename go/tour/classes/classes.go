package main

import (
	"fmt"
	"github.com/hermantai/samples/go/commonutil"
	"math"
	"strconv"
	"strings"
)

func main() {
	// defer's are executed in LIFO
	defer fmt.Println("The End!")
	defer commonutil.PrintSectionSeparator()

	commonutil.PrintSection("Methods")
	commonutil.PrintSubsection("You can only declare a method with a receiver whose type is defined in the same package as the method.")
	methodsSample()

	commonutil.PrintSection("Interfaces")
	interfacesSample()

	commonutil.PrintSection("interface assertions")
	interfaceAssertionsSample()

	commonutil.PrintSection("Interact with standard library interfaces")
	interactWithStandardLibrariesExample()
}

// end of main

func methodsSample() {
	commonutil.PrintSubsection("Vertext.Abs")
	fmt.Println(Vertex{3, 4}.Abs())

	fmt.Println("Scale by 2")
	v := Vertex{3, 4}
	// This is actually a shortcut of (&v).Scale(2). That means even the method
	// has a pointer receiver, it can take a value as well.
	v.Scale(2)
	fmt.Println(v)
	fmt.Println("Scale by 2 again")
	ScaleFunc(&v, 2)
	fmt.Println(v)
	fmt.Println("Print v.Abs() and p.Abs()")
	p := &v
	// methods with value receivers can take either a value or a pointer
	fmt.Println(v.Abs(), p.Abs())
	// The following won't compile
	// ScaleFunc(v, 2)

	commonutil.PrintSubsection("MyFloat")
	f := MyFloat(-math.Sqrt2)
	fmt.Println(f.Abs())
	fmt.Println(f.Plus1().Plus1())
}

type Vertex struct {
	X, Y float64
}

func (v Vertex) Abs() float64 {
	return math.Sqrt(v.X*v.X + v.Y*v.Y)
}

func (v *Vertex) Scale(f float64) {
	v.X *= f
	v.Y *= f
}

func ScaleFunc(v *Vertex, f float64) {
	v.X *= f
	v.Y *= f
}

type MyFloat float64

func (f MyFloat) Abs() float64 {
	if f < 0 {
		return -float64(f)
	}
	return float64(f)
}

func (f MyFloat) Plus1() MyFloat {
	return f + 1
}

func interfacesSample() {
	var a Abser
	f := MyFloat(-math.Sqrt2)
	v := Vertex2{3, 4}

	a = f
	a = &v

	// In the following line, v is a Vertex2 (not *Vertex2)
	// and does NOT implement Abser.
	// a = v
	fmt.Println(a)
	fmt.Println(a.Abs())

	commonutil.PrintSubsection("Describe interfaces")

	fmt.Println("Under the hood, interface can be thought of" +
		" as a tuple of a value and a concrete type (value, type)")
	var myInterface MyInterface

	myInterface = &MyString{"mystring"}
	describe(myInterface)
	myInterface.M1()

	myInterface = MyFloat(math.Pi)
	describe(myInterface)
	myInterface.M1()

	var p *MyString
	// A pointer to nil is not nil
	myInterface = p
	describe(myInterface)

	var myInterface2 MyInterface
	describe(myInterface2)
}

func interfaceAssertionsSample() {
	var myInterface MyInterface

	myInterface = &MyString{"mystring1"}

	fmt.Println("assertion that's ok")
	t := myInterface.(*MyString)
	fmt.Println(t)

	fmt.Println("assertion that's not ok")
	myInterface = MyFloat(32)
	t2, ok := myInterface.(*MyString)
	fmt.Println(t2, ok)

	commonutil.PrintSubsection("type switches")
	switch myInterface.(type) {
	case *MyString:
		fmt.Println("It's a *MyString")
	case MyFloat:
		fmt.Println("It's a MyFloat")
	default:
		fmt.Println("no matched types")
	}
}

func interactWithStandardLibrariesExample() {
	p1 := Person{"peter", 123}
	p2 := Person{"tom", 23}
	fmt.Println(p1, p2)

	commonutil.PrintSubsection("IPAddr exercise")
	hosts := map[string]IPAddr{
		"loopback":  {127, 0, 0, 1},
		"googleDNS": {8, 8, 8, 8},
	}
	for name, ip := range hosts {
		fmt.Printf("%v: %v\n", name, ip)
	}

	commonutil.PrintSubsection("square error exercise")

	v, err := mySqrt(9)
	fmt.Println(v, err)
	v, err = mySqrt(-9)
	fmt.Println(v, err)
}

// end of samples

type Abser interface {
	Abs() float64
}

type Vertex2 struct {
	X, Y float64
}

func (v *Vertex2) Abs() float64 {
	return math.Sqrt(v.X*v.X + v.Y*v.Y)
}

type MyInterface interface {
	M1()
}

type MyString struct {
	S string
}

func (ms *MyString) M1() {
	fmt.Println(ms.S)
}

func (f MyFloat) M1() {
	fmt.Println(f)
}

type Person struct {
	Name string
	Age  int
}

// This implements Stringer interface from package fmt.
func (p Person) String() string {
	return fmt.Sprintf("%v (%v years)", p.Name, p.Age)
}

type IPAddr [4]byte

func (ip IPAddr) String() string {
	ipStrings := make([]string, 0)
	for _, n := range ip {
		ipStrings = append(ipStrings, strconv.Itoa(int(n)))
	}
	return strings.Join(ipStrings, ".")
}

type ErrNegativeSqrt float64

func (e ErrNegativeSqrt) Error() string {
	// Sprintf calls Error of the argument, so without
	// the conversion float64, there would be an infinite loop.
	return fmt.Sprintf("cannot Sqrt negative number: %v", float64(e))
}

func mySqrt(x float64) (float64, error) {
	if x < 0 {
		return 0, ErrNegativeSqrt(x)
	}
	z := 1.0
	for i := 0; i < 10; i++ {
		z -= (z*z - x) / (2 * z)
	}
	return z, nil
}

func describe(i interface{}) {
	fmt.Printf("(%v, %T)\n", i, i)
}
