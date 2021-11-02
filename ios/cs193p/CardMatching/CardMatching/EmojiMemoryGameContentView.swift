//
//  EmojiMemoryGameContentView.swift
//  Shared
//
//  Created by Herman (Heung) Tai on 10/21/21.
//

import SwiftUI



struct EmojiMemoryGameContentView: View {
    @ObservedObject var game: EmojiMemoryGame
        
    @Namespace private var dealingNamespace;
    
    var body: some View {
        VStack {
            GeometryReader { geometry in
                ZStack(alignment: .bottom) {
                    gameBody
                    
                    deckBody(withSize: geometry.size)
                }
            }
            
            controls
        }
        .padding(.horizontal)
    }
    
    @State private var dealt = Set<Int>()
        
    func deal(_ card: EmojiMemoryGame.Card) {
        dealt.insert(card.id)
    }
    
    func isUndealt(_ card: EmojiMemoryGame.Card) -> Bool {
        !dealt.contains(card.id)
    }
    
    var gameBody: some View {
        VStack {
            Text(game.gameTheme.name).font(.largeTitle)
        
            AspectVGrid(items: game.cards, aspectRatio: DrawingConstants.cardAspectRatio) { card in
                if !isUndealt(card) {
                    CardView(card: card)
                        // This is used with deckBody to produce a "deal" animation
                        .matchedGeometryEffect(id: card.id, in: dealingNamespace)
                        // padding in here instead of inside AspectVGrid to make it easier for
                        // AspectVGrid to calculate the number of cards needed based on the size given.
                        .padding(DrawingConstants.cardPadding)
                        .onTapGesture {
                            withAnimation() {
                                game.choose(card)
                            }
                        }
                }
            }.foregroundColor(getColor(themeColor: game.gameTheme.color))
        }
    }
    
    func deckBody(withSize: CGSize) -> some View {
        let cardWidth = widthThatFits(itemCount: game.cards.count, in:withSize, itemAspectRatio: DrawingConstants.cardAspectRatio)
        return ZStack {
            ForEach(game.cards.filter(isUndealt)) { card in
                CardView(card: card)
                    // This is used with the actual card position to produce a "deal" animation
                    .matchedGeometryEffect(id: card.id, in: dealingNamespace)
                // Because of matchedGeometryEffect, we should totally remove the .transition call below, otherwise, continuously
                // pressing "New Game" may cause phantom cards issues.
//                    // removal: .identity here because removal of this CardView
//                    // is actually going to be animated by the matchedGeometryEffect
//                    // so we don't want it to ALSO fade or scale out
//                    // (since transitions and matchedGeometryEffects are not mutually exclusive)
//                    .transition(AnyTransition.asymmetric(insertion: .opacity, removal: .identity))
                    // Need to set the correct zIndex for cards so that the cards are dealt from the top.
                    .zIndex(zIndex(of: card))
            }
            .frame(width: cardWidth, height: cardWidth/DrawingConstants.cardAspectRatio)
        }
        .foregroundColor(getColor(themeColor: game.gameTheme.color))
        .onTapGesture {
            // trigger the animation of dealing cards
            for (index, card) in game.cards.enumerated() {
                withAnimation(dealAnimation(for: card)) {
                    if index == game.cards.count - 1 {
                        dealAnimationObserver.start()
                    }
                    deal(card)
                }
            }
        }
    }
    
    @StateObject private var dealAnimationObserver = AnimationLifeCycleObserver();
    
    var controls: some View {
        HStack {
            startNewGameButton.disabled(dealAnimationObserver.isAnimating)
                .withLifeCycleObserver(dealAnimationObserver)
            
            Spacer()
            
            // (TODO) Remove this after https://stackoverflow.com/questions/69783683/how-to-set-a-property-or-any-callbacks-upon-the-finishing-of-an-animation
            // is answered
            // myTestButton
            Text("Score: \(game.score)")
        }
        .padding(.horizontal)
    }
    
    @State var showRect = true
    @ObservedObject private var rectAnimationObserver = AnimationLifeCycleObserver();
    var myTestButton: some View {
        HStack {
            Rectangle().fill()
                .opacity(showRect ? 1 : 0)
            Button("Show") {
                withAnimation(.linear(duration: 2)) {
                    showRect = true
                    rectAnimationObserver.start()
                }
            }
            .withLifeCycleObserver(rectAnimationObserver) {
                print("rect animation done from Show button")
            }
            .disabled(showRect || rectAnimationObserver.isAnimating)
            
            Button("Hide") {
                withAnimation(.linear(duration: 2)) {
                    showRect = false
                    rectAnimationObserver.start()
                }
            }
            .withLifeCycleObserver(rectAnimationObserver) {
                print("rect animation done from Hide button")
            }
            .disabled(!showRect || rectAnimationObserver.isAnimating)
        }
    }
    
    var startNewGameButton: some View {
        Button("New Game") {
            dealt = []
            game.startNewGame()
        }
    }
    
    func zIndex(of card: EmojiMemoryGame.Card) -> Double {
        -Double(game.cards.firstIndex(where: { $0.id == card.id }) ?? 0)
    }
    
    func dealAnimation(for card: EmojiMemoryGame.Card) -> Animation {
        var delay = 0.0
        if let index = game.cards.firstIndex(where: { $0.id == card.id }) {
            // The card that is dealt first should have a shorter delay.
            delay = Double(index) * (DrawingConstants.totalDealDuration / Double(game.cards.count))
        }
        return Animation.easeInOut(duration: DrawingConstants.dealDuration).delay(delay)
    }
    
    func getColor(themeColor: String) ->  Color {
        switch(themeColor) {
        case "red":
            return Color.red
        case "orange":
            return Color.orange
        case "yellow":
            return Color.yellow
        case "grey":
            return Color.black
        default:
            return Color.red
        }
    }
    
    // The following scroll view was replaced by the AspectVGrid.
    var oldScrollView: some View {
        ScrollView {
            LazyVGrid(columns: [GridItem(.adaptive(minimum: 65))]) {
                ForEach(game.cards) { card in
                    CardView(card: card)
                        .aspectRatio(1/1.618 /* golden ratio */, contentMode: .fit)
                        .onTapGesture {
                            game.choose(card)
                        }
                }
            }
        }.foregroundColor(getColor(themeColor: game.gameTheme.color))
    }
    
    private struct DrawingConstants {
        static let cardPadding: CGFloat = 3
        // Width to height ratio
        static let cardAspectRatio: CGFloat = 1/1.618 /* golden ratio */
        static let dealDuration: Double = 0.5
        static let totalDealDuration: Double = 2
        static let undealtHeight: CGFloat = 90
        static let undealtWidth = undealtHeight * cardAspectRatio
    }
}

struct CardView: View {
    let card: MemoryGame<String>.Card
    
    @State private var animatedBonusRemaining: Double = 0
    
    var body: some View {
        if card.isMatched && !card.isFaceUp {
            Color.clear
        } else {
            GeometryReader { geometry in
                ZStack {
                    Group {
                        // card.isConsumingBonusTime is changed for isFaceUp, isMatched, and time lapsed.
                        //
                        // When card.isConsumingBonusTime is true, the upper Pie shows up. At appear,
                        // animatedBonusRemaining is set to the fraction of count down remaining, then
                        // we trigger an animation with changing animatedBonusRemaining back to 0.
                        //
                        // Since animatedBonusRemaining is being changed and it's used in the upper Pie,
                        // the upper Pie will be animated (it's an AnimableModifer because of being a Shape).
                        // At some point, card.isConsumingBonusTime is false because there is not more count down
                        // remaining. The upper pie will then be removed and be replaced with the lower pie. The animation
                        // will be stopped as well.
                        if card.isConsumingBonusTime {
                            Pie(startAngle: Angle(degrees: 0-90), endAngle: Angle(degrees: (1 - animatedBonusRemaining) * 360-90))
                                .onAppear {
                                    animatedBonusRemaining = card.faceUpCountDownTimer.fractionOfCountDownRemaining
                                    
                                    withAnimation(.linear(duration: card.faceUpCountDownTimer.countDownRemaining)) {
                                        animatedBonusRemaining = 0
                                    }
                                }
                        } else {
                            Pie(startAngle: Angle(degrees: 0-90), endAngle: Angle(degrees: (1-card.faceUpCountDownTimer.fractionOfCountDownRemaining)*360-90))
                        }
                    }
                    .padding(2)
                    .opacity(0.5)
                    
                    Text(card.content)
                        .rotationEffect(Angle(degrees: card.isMatched ? 360 : 0))
                        .animation(.linear(duration: 1).repeatForever(autoreverses: false))
                        // There is a quirk in SwiftUI that the font cannot change after .animation, so
                        // font(in) does not work (a rotation can change the geometry, and thus the font size).
                        // A fixed font + scaleEffect is a workaround.
                        .font(Font.system(size: CardDrawingConstants.fontSize))
                        .scaleEffect(scale(in: geometry.size))
                }.cardify(isFaceUp: card.isFaceUp, card: card)
            }
        }
    }
    
    // Not used in favor of using a fixed font, then multiplied by scale(in).
    func font(in size: CGSize) -> Font {
        Font.system(size: min(size.width, size.height) * CardDrawingConstants.fontScale)
    }
    
    func scale(in size: CGSize) -> CGFloat {
        min(size.width, size.height) * CardDrawingConstants.fontScale / CardDrawingConstants.fontSize
    }
    
    private struct CardDrawingConstants {
        static let fontScale: CGFloat = 0.75
        static let fontSize: CGFloat = 32
    }
}

struct EmojiMemoryGameContentView_Previews: PreviewProvider {
    static var previews: some View {
        let game = EmojiMemoryGame()
        
        Group {
            EmojiMemoryGameContentView(game: game)
                .preferredColorScheme(.light)
            EmojiMemoryGameContentView(game: game)
                .preferredColorScheme(.dark)
        }
    }
}
