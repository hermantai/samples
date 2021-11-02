//
//  EmojiMemoryGame.swift
//  CardMatching
//
//  Created by Herman (Heung) Tai on 10/25/21.
//

import SwiftUI

class EmojiMemoryGame {
    static let emojis =  ["🚗","🚕","🚙","🚌",
                      "🚎","🏎","🚓","🚑",
                      "🚒","🚐","🛻","🚚",
                      "🚛","🚜","🚀","🚁",
                      "⛵️","🚤","🦼","🛴",
                      "🚍","✈️","🚂","🚊"]
    
    static func createMemoryGame() -> MemoryGame<String> {
        MemoryGame<String>(numberOfPairsOfCards: 4) { index in emojis[index] }
    }
    
    private var model: MemoryGame<String> = createMemoryGame()
    
    var cards: [MemoryGame<String>.Card] {
        model.cards
    }
}
