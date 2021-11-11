//
//  CardMatchingApp.swift
//  CardMatching
//
//  Created by Herman (Heung) Tai on 10/25/21.
//

import SwiftUI

@main
struct CardMatchingApp: App {
    @StateObject var gameThemeStore = GameThemeStore(named: "Default")
        
    var body: some Scene {
        WindowGroup {
            ThemeManagerView().environmentObject(gameThemeStore)
        }
    }
}
