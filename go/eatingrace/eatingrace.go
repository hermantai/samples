// Copyright 2015 Herman Tai
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// eatingrace is a demo of concurrency in Go using math/rand, sync.Once,
// time.Tick, time.After, goroutines and channels.
package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

var createGoodFoodOnce sync.Once

// getFood gives out good food for the first time, then dogfood subsequently
func getFood() string {
	food := "dogfood"
	createGoodFoodOnce.Do(
		func() {
			food = "an apple!!!"
		})
	return food
}

type eater func(out chan<- string, eatSignal <-chan bool, done chan<- struct{})

// makeEater makes an eater. Each eater first gets the food, then eats it
// every time he/she is signaled to do so by the eatSignal returning true.
// When eatSignal returns false, the eater stops eating and puts an empty
// struct in the done channel.
func makeEater(name string) eater {
	return func(out chan<- string, eatSignal <-chan bool, done chan<- struct{}) {
		// Wait a random time before getting the food, so each eater has a
		// chance to get the good food for each running of this race.
		time.Sleep(time.Duration(rand.Intn(10)*100) * time.Millisecond)
		food := getFood()
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
	rand.Seed(time.Now().UnixNano())

	history := make(chan string)
	historyDone := make(chan struct{})
	go func() {
		for s := range history {
			fmt.Println(s)
		}
		fmt.Println("history is done!!!")
		historyDone <- struct{}{}
	}()

	eaters := []eater{
		makeEater("Peter"),
		makeEater("Paul"),
		makeEater("Mary"),
		makeEater("Tom"),
		makeEater("Betty"),
		makeEater("Amy"),
	}
	eatSignals := [](chan bool){}
	eaterDoneChannels := [](chan struct{}){}

	for _, eater := range eaters {
		// a buffered channel to prevent the ticking getting blocked by a slow
		// eater
		eaterChannel := make(chan bool, 3)
		eaterDoneChannel := make(chan struct{})

		go eater(history, eaterChannel, eaterDoneChannel)

		eatSignals = append(eatSignals, eaterChannel)
		eaterDoneChannels = append(eaterDoneChannels, eaterDoneChannel)
	}

	tick := time.Tick(500 * time.Millisecond)
	done := time.After(15 * time.Second)
	keepEating := true

	for keepEating {
		select {
		case <-tick:
			eatSignals[rand.Intn(len(eaters))] <- true
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
			time.Sleep(200 * time.Millisecond)
		}
	}

	<-historyDone
	fmt.Println("main is done!!!")
}
