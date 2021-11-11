//
//  GameThemeStore.swift
//  CardMatching
//
//  Created by Herman (Heung) Tai on 11/7/21.
//

import Foundation


class GameThemeStore: ObservableObject {
    @Published var gameThemes = [GameTheme]() {
        didSet {
            storeInUserDefaults()
        }
    }
    
    let name: String
    
    private var userDefaultsKey: String {
        "GameThemeStore:" + name
    }
    
    private func storeInUserDefaults() {
        UserDefaults.standard.set(try? JSONEncoder().encode(gameThemes), forKey: userDefaultsKey)
    }
    
    private func restoreFromUserDefaults() {
        if let jsonData = UserDefaults.standard.data(forKey: userDefaultsKey),
           let decodedPalettes = try? JSONDecoder().decode(Array<GameTheme>.self, from: jsonData) {
            gameThemes = decodedPalettes
        }
    }
    
    init(named name: String) {
        self.name = name
        restoreFromUserDefaults()
        if gameThemes.isEmpty {
            insertTheme(named: "Animals",
                        emojis: "🐶🐱🐭🐹🐰🦊🐻🐼🐻‍❄️🐨🐯🦁🐮🐷🐸🐵",
                        color: RGBAColor(color: .red))
            insertTheme(
                named: "Food",
                emojis: "🍎🍐🍊🍋🍌🍉🍇🍓🫐🍈🍒🍑🥭🍍🥥🥝🍅🍆🥑🥦",
                color: RGBAColor(color: .orange))
            insertTheme(
                named: "Vehicles",
                emojis: "🚗🚕🚙🚌🚎🏎🚓🚑🚒🚐🛻🚚🚛🚜🚀🚁⛵️🚤🦼🛴🚍✈️🚂🚊",
                color: RGBAColor(color: .yellow))
            insertTheme(
                named: "Halloween",
                emojis: "👻🎃🕷",
                color: RGBAColor(color: .gray))
        }
    }
    
    // MARK: - Intent
    
    func gameTheme(at index: Int) -> GameTheme {
        let safeIndex = min(max(index, 0), gameThemes.count - 1)
        return gameThemes[safeIndex]
    }
    
    @discardableResult
    func removeTheme(at index: Int) -> Int {
        if gameThemes.count > 1, gameThemes.indices.contains(index) {
            gameThemes.remove(at: index)
        }
        return index % gameThemes.count
    }
    
    func insertTheme(named name: String, emojis: String, color: RGBAColor, at index: Int = 0) {
        let unique = (gameThemes.max(by: { $0.id < $1.id })?.id ?? 0) + 1
        let gameTheme = GameTheme(name: name, emojis: emojis, color: color, id: unique)
        let safeIndex = min(max(index, 0), gameThemes.count)
        gameThemes.insert(gameTheme, at: safeIndex)
    }

}

///  A Theme consists of a name
///  for the theme, a set of emoji to use, a number of pairs of cards to show, and an
///  appropriate color to use to draw the cards.
struct GameTheme: Identifiable, Codable, Hashable {
    let name: String
    let emojis: String
    let color: RGBAColor
    let id: Int
}

struct RGBAColor: Codable, Equatable, Hashable {
    let red: Double
    let green: Double
    let blue: Double
    let alpha: Double
}
