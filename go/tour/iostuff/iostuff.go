package main

import (
	"fmt"
	"github.com/hermantai/samples/go/commonutil"
	"image"
	"io"
	"strings"
)

func main() {
	// defer's are executed in LIFO
	defer fmt.Println("The End!")
	defer commonutil.PrintSectionSeparator()

	commonutil.PrintSection("Reader")
	readerSample()

	commonutil.PrintSection("image")
	imageSample()
}

// end of main

func readerSample() {
	r := strings.NewReader("Hello, Reader!")

	b := make([]byte, 8)
	for {
		// only n characters are read
		n, err := r.Read(b)
		fmt.Printf("b[:n] = %q\n", b[:n])
		if err == io.EOF {
			break
		}
	}
}

func imageSample() {
	m := image.NewRGBA(image.Rect(0, 0, 100, 100))
	fmt.Println(m.Bounds())
	fmt.Println(m.At(0, 0).RGBA())
}

// end of samples
