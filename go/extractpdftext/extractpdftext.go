package main

import (
	"fmt"
	"os"

	"github.com/unidoc/unipdf/v3/common"
	"github.com/unidoc/unipdf/v3/core"
	"github.com/unidoc/unipdf/v3/model"
)

func main() {
	if len(os.Args) < 2 {
		fmt.Printf("Usage: go run pdf_text_extract.go input.pdf\n")
		return
	}

	inputPath := os.Args[1]

	common.Log.SetLevel(common.LogLevelDebug)

	pdfReader, err := model.NewPdfReader(inputPath)
	if err != nil {
		fmt.Printf("Error: %v\n", err)
		return
	}

	isEncrypted, err := pdfReader.IsEncrypted()
	if err != nil {
		fmt.Printf("Error checking if encrypted: %v\n", err)
		return
	}

	if isEncrypted {
		fmt.Printf("Encrypted documents are not supported\n")
		return
	}

	numPages, err := pdfReader.GetNumPages()
	if err != nil {
		fmt.Printf("Error getting number of pages: %v\n", err)
		return
	}

	fmt.Printf("Number of pages: %d\n", numPages)

	for i := 0; i < numPages; i++ {
		pageNum := i + 1

		page, err := pdfReader.GetPage(pageNum)
		if err != nil {
			fmt.Printf("Error getting page %d: %v\n", pageNum, err)
			return
		}

		text, err := model.GetTextFromPage(page)
		if err != nil {
			fmt.Printf("Error extracting text from page %d: %v\n", pageNum, err)
			return
		}

		fmt.Printf("Page %d:\n", pageNum)
		fmt.Printf("%s\n", text)
	}
}
