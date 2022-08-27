// A sample of unit tests.
// This can be run by "go test unittests_test.go"
// The "go.mod" file can be updated by running a "go get <package>" command if
// <package> is needed by this file.
// e.g. go get github.com/google/go-cmp/cmp
package main

import (
	"testing"

	"github.com/google/go-cmp/cmp"
)

type Pair struct {
	x int
	y int
}

func TestCmpWithDiff(t *testing.T) {
	want := map[string]Pair{
		"a": Pair{1, 2},
		"b": Pair{2, 3},
		"c": Pair{3, 4},
	}
	got := map[string]Pair{
		"a": Pair{1, 2},
		"b": Pair{2, 3},
		"d": Pair{101, 102},
	}

	if diff := cmp.Diff(want, got, cmp.AllowUnexported(Pair{})); diff != "" {
		t.Errorf("Results differed: (-want +got)\n%v.", diff)
	}
}
