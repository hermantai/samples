package main

import (
	"golang.org/x/tour/pic"
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

func main() {
	pic.Show(Pic)
}
