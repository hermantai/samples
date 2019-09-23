package main

import (
	"fmt"
	"golang.org/x/tour/pic"
	"image"
	"image/color"
)

func Pic(dx, dy int) [][]uint8 {
	var rows [][]uint8
	for y := 0; y < dy; y++ {
		var cols []uint8
		for x := 0; x < dx; x++ {
			cols = append(cols, uint8((x+y)/2))
		}
		rows = append(rows, cols)
	}
	return rows
}

type Image struct{}

func (i Image) ColorModel() color.Model {
	return color.RGBAModel
}

func (i Image) Bounds() image.Rectangle {
	return image.Rect(0, 0, 100, 200)
}

func (i Image) At(x, y int) color.Color {
	v := uint8((x + y) / 2)
	return color.RGBA{v, v, 255, 255}
}

func main() {
	pic.Show(Pic)

	fmt.Println()
	m := Image{}
	pic.ShowImage(m)
}
