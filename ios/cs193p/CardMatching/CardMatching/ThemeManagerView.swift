//
//  ThemeManagerView.swift
//  CardMatching
//
//  Created by Herman (Heung) Tai on 11/7/21.
//

import SwiftUI

struct ThemeManagerView: View {
    @EnvironmentObject var gameThemeStore: GameThemeStore
    
    @State var games: [GameTheme: EmojiMemoryGame] = [:]
    
    // we inject a Binding to this in the environment for the List and EditButton
    // using the \.editMode in EnvironmentValues
    @State private var editMode: EditMode = .inactive
    
    var body: some View {
        NavigationView {
            List {
                ForEach(gameThemeStore.gameThemes) { gameTheme in
                    NavigationLink(destination: EmojiMemoryGameView(games: $games, gameTheme: gameTheme)) {
                        VStack(alignment: .leading) {
                            Text(gameTheme.name)
                            Text(gameTheme.emojis)
                        }
                        // tapping when NOT in editMode will follow the NavigationLink
                        // (that's why gesture is set to nil in that case)
    //                        .gesture(editMode == .active ? tap : nil)
                    }
//                // teach the ForEach how to delete items
//                // at the indices in indexSet from its array
//                .onDelete { indexSet in
//                    store.palettes.remove(atOffsets: indexSet)
//                }
//                // teach the ForEach how to move items
//                // at the indices in indexSet to a newOffset in its array
//                .onMove { indexSet, newOffset in
//                    store.palettes.move(fromOffsets: indexSet, toOffset: newOffset)
                }
            }
            .listStyle(PlainListStyle())
            .navigationTitle("Card Matching")
            // add an EditButton on the trailing side of our NavigationView
            // and a Close button on the leading side
            // notice we are adding this .toolbar to the List
            // (not to the NavigationView)
            // (NavigationView looks at the View it is currently showing for toolbar info)
            // (ditto title and titledisplaymode above)
            .toolbar {
                ToolbarItem { EditButton() }
//                ToolbarItem(placement: .navigationBarLeading) {
//                    if presentationMode.wrappedValue.isPresented,
//                       UIDevice.current.userInterfaceIdiom != .pad {
//                        Button("Close") {
//                            presentationMode.wrappedValue.dismiss()
//                        }
//                    }
//                }
            }
            .environment(\.editMode, $editMode)
        }
        .navigationViewStyle(.stack)
    }
}

struct ThemeManagerView_Previews: PreviewProvider {
    static var previews: some View {
        ThemeManagerView()
    }
}
