//
//  MemoryGame.swift
//  CardMatching
//
//  Created by Herman (Heung) Tai on 10/25/21.
//

import Foundation

struct MemoryGame<CardContent> {
    private(set) var cards: [Card] = []
    
    init(numberOfPairsOfCards: Int, createCardContent: (Int) -> CardContent) {
        for pairIndex in 0..<numberOfPairsOfCards {
            let cardContent = createCardContent(pairIndex)
            cards.append(Card(content: cardContent))
            cards.append(Card(content: cardContent))
        }
    }
    
    func choose(_ card: Card) {
        
    }
    
    struct Card {
        var isFaceUp = false
        var isMatched = false
        var content: CardContent
    }
}
