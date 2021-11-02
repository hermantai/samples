//
//  EmojiMemoryGame.swift
//  CardMatching
//
//  Created by Herman (Heung) Tai on 10/25/21.
//

import SwiftUI


///  A Theme consists of a name
//for the theme, a set of emoji to use, a number of pairs of cards to show, and an
//appropriate color to use to draw the cards.
        
struct GameTheme {
    let name: String
    let emojis: [String]
    let numberOfPairs: Int
    let color: String
}

let gameThemes = [
    GameTheme(
        name: "Animals",
        emojis: ["🐶","🐱","🐭","🐹",
                "🐰","🦊","🐻","🐼",
                "🐻‍❄️","🐨","🐯","🦁",
                "🐮","🐷","🐸","🐵"],
        numberOfPairs: 16, color: "red"),
    GameTheme(
        name: "Food",
        emojis: ["🍎","🍐","🍊","🍋",
                 "🍌","🍉","🍇","🍓",
                 "🫐","🍈","🍒","🍑",
                 "🥭","🍍","🥥","🥝",
                 "🍅","🍆","🥑","🥦"],
        numberOfPairs: 12, color: "orange"),
    GameTheme(
        name: "Vehicles",
        emojis: ["🚗","🚕","🚙","🚌",
                 "🚎","🏎","🚓","🚑",
                 "🚒","🚐","🛻","🚚",
                 "🚛","🚜","🚀","🚁",
                 "⛵️","🚤","🦼","🛴",
                 "🚍","✈️","🚂","🚊"],
        numberOfPairs: 24, color: "yellow"),
    GameTheme(
        name: "Halloween",
        emojis: ["👻","🎃","🕷"],
        numberOfPairs: 8, color: "grey"),
]

class EmojiMemoryGame: ObservableObject {

    typealias Card = MemoryGame<String>.Card
    
    // The chosen theme of the game.
    private(set) var gameTheme: GameTheme
    
    // The model of the game.
    @Published private var model: MemoryGame<String>
    
    var cards: [Card] { model.cards }
    
    var score: Int { model.scores.score }
    
    init(gameTheme: GameTheme = gameThemes[3]) {
        self.gameTheme = gameTheme
        model = EmojiMemoryGame.createMemoryGame(gameTheme: gameTheme)
    }
    
    private static func createMemoryGame(gameTheme: GameTheme) -> MemoryGame<String> {
        let numberOfPairs = min(gameTheme.emojis.count, gameTheme.numberOfPairs)
        let emojis = gameTheme.emojis.shuffled()[0..<numberOfPairs]
        
        return MemoryGame<String>(
            numberOfPairsOfCards: numberOfPairs) {
                index in emojis[index]
            }
    }
    
    // Intent(s)
    
    // The user chooses a card in the game.
    func choose(_ card: Card) {
        model.choose(card)
    }
        
    // Starts a new game.
    @discardableResult func startNewGame() -> EmojiMemoryGame {
        if let gameTheme = gameThemes.randomElement() {
            self.gameTheme = gameTheme
            model = EmojiMemoryGame.createMemoryGame(gameTheme: self.gameTheme)
        } else {
            self.gameTheme = gameThemes[0]
            model = EmojiMemoryGame.createMemoryGame(gameTheme: self.gameTheme)
        }
        return self
    }
}
