package main

import (
	"fmt"
	"github.com/hermantai/samples/go/commonutil"
	"time"
)

func main() {
	commonutil.PrintSection("Goroutines")
	goroutinesSample()

	commonutil.PrintSection("Channels")
	channelsSample()
}

// end of main

func goroutinesSample() {
	go say("world")
	say("hello")
}

func say(s string) {
	for i := 0; i < 5; i++ {
		time.Sleep(100 * time.Millisecond)
		fmt.Println(s)
	}
}

func channelsSample() {
	s := []int{7, 2, 8, -9, 4, 0}

	c := make(chan int)
	go sum(s[:len(s)/2], c)
	go sum(s[len(s)/2:], c)

	x, y := <-c, <-c

	fmt.Println(x, y, x+y)
}

func sum(s []int, c chan int) {
	sum := 0
	for _, v := range s {
		sum += v
	}
	c <- sum
}

// end of samples
