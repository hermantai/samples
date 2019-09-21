package main

import (
	"fmt"
	"github.com/hermantai/samples/go/commonutil"
	"math"
)

func main() {
	// defer's are executed in LIFO
	defer fmt.Println("The End!")
	defer commonutil.PrintSectionSeparator()

	commonutil.PrintSection("Methods")
	commonutil.PrintSubsection("You can only declare a method with a receiver whose type is defined in the same package as the method.")
	methodsSample()
}

// end of main

func methodsSample() {
	commonutil.PrintSubsection("Vertext.Abs")
	fmt.Println(Vertex{3, 4}.Abs())

	fmt.Println("Scale by 2")
	v := Vertex{3, 4}
	v.Scale(2)
	fmt.Println(v)

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

// end of samples
