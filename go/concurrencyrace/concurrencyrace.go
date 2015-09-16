// A demo of concurrency using math/rand, sync.Once, time.Tick, time.After,
// goroutines and channels.
package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

var randomGen = rand.New(rand.NewSource(time.Now().UnixNano()))

var createGoodFood sync.Once

// getFood gives out good food for the first time, then dogfood subsequently
func getFood() string {
	food := "dogfood"
	createGoodFood.Do(
		func() {
			food = "an apple"
		})
	return food
}

type eater func(out chan<- string, eatSignal <-chan bool, done chan<- struct{})

// makeEater makes an eater. Each eater first gets the food, then eats it
// every time he/she is signaled to do so by the eatSignal returning true.
// When eatSignal returns false, the eater stops eating and puts an empty
// struct in the done channel.
func makeEater(name string) eater {
	var food string
	var selectFood sync.Once

	return func(out chan<- string, eatSignal <-chan bool, done chan<- struct{}) {
		selectFood.Do(
			func() {
				food = getFood()
			})
		fmt.Printf("%s gets %s.\n", name, food)
		for {
			canEat := <-eatSignal
			if canEat {
				out <- fmt.Sprintf("%s eats %s.", name, food)
			} else {
				done <- struct{}{}
				break
			}
		}
		fmt.Println(name + " is done.")
	}
}

func main() {
	history := make(chan string)
	go func() {
		for s := range history {
			fmt.Println(s)
		}
		fmt.Println("history is done!!!")
	}()

	eaters := []eater{makeEater("peter"), makeEater("paul"), makeEater("mary")}
	eatSignals := [](chan bool){}
	eaterDoneChannels := [](chan struct{}){}

	for _, eater := range eaters {
		eaterChannel := make(chan bool)
		eaterDoneChannel := make(chan struct{})

		go eater(history, eaterChannel, eaterDoneChannel)

		eatSignals = append(eatSignals, eaterChannel)
		eaterDoneChannels = append(eaterDoneChannels, eaterDoneChannel)
	}

	tick := time.Tick(500 * time.Millisecond)
	done := time.After(10 * time.Second)
	keepEating := true

	for keepEating {
		select {
		case <-tick:
			eatSignals[randomGen.Intn(3)] <- true
		case <-done:
			fmt.Println("Tell eaters they should be done eating.")
			for _, c := range eatSignals {
				c <- false
			}
			for _, c := range eaterDoneChannels {
				<-c
			}
			fmt.Println("close history")
			close(history)
			keepEating = false
		default:
			fmt.Println(".")
			time.Sleep(100 * time.Millisecond)
		}
	}
	fmt.Println("main is done!!!")
}
