//
//  ContentView.swift
//  Shared
//
//  Created by Herman (Heung) Tai on 10/21/21.
//

import SwiftUI

enum EmojiTheme {
    case vehicles, animals, food
}

let Emojis = [
    EmojiTheme.animals:
        ["🐶","🐱","🐭","🐹",
         "🐰","🦊","🐻","🐼",
         "🐻‍❄️","🐨","🐯","🦁",
         "🐮","🐷","🐸","🐵"],
    EmojiTheme.food:
        ["🍎","🍐","🍊","🍋",
         "🍌","🍉","🍇","🍓",
         "🫐","🍈","🍒","🍑",
         "🥭","🍍","🥥","🥝",
         "🍅","🍆","🥑","🥦"],
    EmojiTheme.vehicles:
        ["🚗","🚕","🚙","🚌",
         "🚎","🏎","🚓","🚑",
         "🚒","🚐","🛻","🚚",
         "🚛","🚜","🚀","🚁",
         "⛵️","🚤","🦼","🛴",
         "🚍","✈️","🚂","🚊"],
]
struct ContentView: View {
    @State var emoijs = Emojis[EmojiTheme.vehicles]!
    @State var emoijsCount = 4
    let gridColumns: [GridItem] =
                 Array(repeating: .init(.flexible()), count: 4)
    let defaultGridColumns = [GridItem(.adaptive(minimum: 65))]
    
    var body: some View {
        VStack {
            Text("Memorize!").font(.largeTitle)
            
            ScrollView {
                LazyVGrid(columns: defaultGridColumns) {
                    /*
                    ForEach(emoijs[..<emoijsCount], id: \.self) { emoij in
                        CardView(content: emoij)
                            .aspectRatio(1/1.618 /* golden ratio */, contentMode: .fit)
                    }
                    */
                    
                    ForEach(emoijs, id: \.self) { emoij in
                        CardView(content: emoij)
                            .aspectRatio(1/1.618 /* golden ratio */, contentMode: .fit)
                    }
                }
            }.foregroundColor(.red)
            
            Spacer()
            
            HStack {
                Spacer()
                
                /*
                LazyVGrid(columns: [GridItem(.adaptive(minimum: 60))]) {
                    ThemeButton(imageSystemName: "plus.circle", imageTitle: "animals")
                    ThemeButton(imageSystemName: "plus.circle", imageTitle: "animals")
                }
                 */
                
                EmojiThemeButton(imageSystemName: "hare", imageTitle: "animals")
                    .onTapGesture {
                        changeTheme(emojiTheme: .animals)
                    }
                Spacer()
                EmojiThemeButton(imageSystemName: "leaf", imageTitle: "food")
                    .onTapGesture {
                        changeTheme(emojiTheme: .food)
                    }
                Spacer()
                EmojiThemeButton(imageSystemName: "car", imageTitle: "vehicles")
                    .onTapGesture {
                        changeTheme(emojiTheme: .vehicles)
                    }
                Spacer()
            }.padding(.vertical)
        }
        .padding(.horizontal)
    }
    
    var defaultControl: some View {
        HStack {
            removeCard
            Spacer()
            addCard
        }
        .font(.largeTitle)
        .padding(.horizontal)
    }
    
    var addCard: some View {
        Button {
            if emoijsCount < emoijs.count {
                emoijsCount += 1
            }
        } label: {
            Image(systemName: "plus.circle")
        }
    }
    
    var removeCard: some View {
        Button {
            if emoijsCount > 1 {
                emoijsCount -= 1
            }
        } label: {
            Image(systemName: "minus.circle")
        }
    }
    
    func changeTheme(emojiTheme: EmojiTheme) {
        if let emojis = Emojis[emojiTheme] {
            self.emoijs = emojis.shuffled()
        }
    }
}

struct CardView: View {
    var content: String
    @State var isFaceUp: Bool = true
    
    var body: some View {
        ZStack {
            let background = RoundedRectangle(cornerRadius: 20)
            if isFaceUp {
                background
                    .fill()
                    .foregroundColor(.white)
                    
                background
                    .strokeBorder(lineWidth: 3)
                
                Text(content).font(.largeTitle)
            } else {
                background.fill()
            }
        }.onTapGesture {
            isFaceUp = !isFaceUp
        }
    }
}

struct EmojiThemeButton: View {
    var imageSystemName: String
    var imageTitle: String
    
    var body: some View {
        VStack {
            Image(systemName: imageSystemName).font(.largeTitle)
            
            Text(imageTitle)
                .font(.caption)
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ContentView()
                .preferredColorScheme(.light)
            ContentView()
                .preferredColorScheme(.dark)
        }
    }
}
