//
//  CardMatchingApp.swift
//  CardMatching
//
//  Created by Herman (Heung) Tai on 10/25/21.
//

import SwiftUI

@main
struct CardMatchingApp: App {
    var body: some Scene {
        WindowGroup {
            EmojiMemoryGameContentView(game: EmojiMemoryGame()/*.startNewGame()*/)
        }
    }
}
