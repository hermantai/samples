//
//  EmojiMemoryGame.swift
//  CardMatching
//
//  Created by Herman (Heung) Tai on 10/25/21.
//

import SwiftUI


class EmojiMemoryGame: ObservableObject {

    typealias Card = MemoryGame<String>.Card
    
    // The chosen theme of the game.
    private(set) var gameTheme: GameTheme
    
    // The model of the game.
    @Published private var model: MemoryGame<String>
    
    var cards: [Card] { model.cards }
    
    var score: Int { model.scores.score }
    
    @Published private(set) var dealt = Set<Int>()
            
    init(gameTheme: GameTheme) {
        self.gameTheme = gameTheme
        model = EmojiMemoryGame.createMemoryGame(gameTheme: gameTheme)
    }
    
    private static func createMemoryGame(gameTheme: GameTheme) -> MemoryGame<String> {
        let numberOfPairs = min(gameTheme.emojis.count, gameTheme.emojis.count)
        let emojis = gameTheme.emojis.shuffled()[0..<numberOfPairs]
        
        return MemoryGame<String>(
            numberOfPairsOfCards: numberOfPairs) {
                index in String(emojis[emojis.index(emojis.startIndex, offsetBy: index)])
            }
    }
    
    // Intent(s)
    
    func deal(_ card: EmojiMemoryGame.Card) {
        dealt.insert(card.id)
    }
    
    func isUndealt(_ card: EmojiMemoryGame.Card) -> Bool {
        !dealt.contains(card.id)
    }

    // The user chooses a card in the game.
    func choose(_ card: Card) {
        model.choose(card)
    }
        
    // Starts a new game.
    @discardableResult func startNewGame() -> EmojiMemoryGame {
        model = EmojiMemoryGame.createMemoryGame(gameTheme: self.gameTheme)
        dealt = []
//        if let gameTheme = gameThemes.randomElement() {
//            self.gameTheme = gameTheme
//            model = EmojiMemoryGame.createMemoryGame(gameTheme: self.gameTheme)
//        } else {
//            self.gameTheme = gameThemes[0]
//            model = EmojiMemoryGame.createMemoryGame(gameTheme: self.gameTheme)
//        }
        return self
    }
    
    func pauseGame() {
        model.pauseGame()
    }
    
    func resumeGame() {
        model.resumeGame()
    }
}
