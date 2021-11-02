//
//  Cardify.swift
//  CardMatching
//
//  Created by Herman (Heung) Tai on 10/30/21.
//

import SwiftUI

struct Cardify: AnimatableModifier {
    
    var rotationDegrees: Double
        
    /// Used with the Animatable protocol to allow for changing the rotationDegrees gradually during animation.
    var animatableData: Double {
        // Used to tell the framework that a value has changed.
        get { rotationDegrees }
        // Used by the framework to tell us the actual vaue to use.
        set { rotationDegrees = newValue }
    }

    var game: EmojiMemoryGame?
    var card: EmojiMemoryGame.Card?
    init(isFaceUp: Bool, card: EmojiMemoryGame.Card? = nil, game: EmojiMemoryGame? = nil) {
        rotationDegrees = isFaceUp ? 0 : 180
        self.card = card
        self.game = game
    }
    
    func body(content: Content) -> some View {
        ZStack {
            let background = RoundedRectangle(cornerRadius: DrawingConstants.cornerRadius)
            if rotationDegrees < 90 {
                background.fill().foregroundColor(.white)
                background.strokeBorder(lineWidth: DrawingConstants.lineWidth)
            } else {
                background.fill()
                background.strokeBorder(lineWidth: DrawingConstants.lineWidth).foregroundColor(.yellow)
            }
            // The content always exists and the visibility is controlled by rotationDegrees because
            // it allows for animations within content. If the content only exists when
            // rotationDegrees < 90 (isFaceUp == true), an animation that happens only during isFaceUp cannot
            // happen, e.g. a rotation of the content inside.
            content.opacity(rotationDegrees < 90 ? 1 : 0)
        }
        // A 3D rotation along the Y axis
        .rotation3DEffect(Angle(degrees: rotationDegrees), axis: (0, 1, 0))
    }
        
    private struct DrawingConstants {
        static let cornerRadius: CGFloat = 15
        static let lineWidth: CGFloat = 3
    }
}

extension View {
    func cardify(isFaceUp: Bool, card: EmojiMemoryGame.Card? = nil, game: EmojiMemoryGame? = nil) -> some View {
        self.modifier(Cardify(isFaceUp: isFaceUp, card: card, game: game))
    }
}
