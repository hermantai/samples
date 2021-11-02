//
//  MemoryGame.swift
//  CardMatching
//
//  Created by Herman (Heung) Tai on 10/25/21.
//

import Foundation

struct MemoryGame<CardContent: Equatable> {
    private(set) var cards: [Card] = []
    
    // Track the scores of the game.
    private(set) var scores = Scores()

    private var indexOfTheOneAndOnlyFaceUpCard: Int? {
        get { cards.indices.filter{cards[$0].isFaceUp}.oneAndOnly }
        set { cards.indices.forEach{ cards[$0].isFaceUp = $0 == newValue } }
    }

    // Track the cards the user has seen, e.g. the card is flipped up, then down because of an unmatch
    private var indicesForCardsSeen = Set<Int>()
    
    init(numberOfPairsOfCards: Int, createCardContent: (Int) -> CardContent) {
        for pairIndex in 0..<numberOfPairsOfCards {
            let cardContent = createCardContent(pairIndex)
            cards.append(Card(content: cardContent, id: pairIndex * 2))
            cards.append(Card(content: cardContent, id: pairIndex * 2 + 1))
        }
        cards.shuffle()
    }
    
    mutating func removeCards() {
        cards.removeAll()
    }
    
    /// The user chooses a card in the game.
    mutating func choose(_ card: Card) {
        // Notice that most of the mutation has to be done with cards[index]... without being assigned
        // to any variables because Card is a struct.
        if let chosenCardIndex = cards.firstIndex(where: {$0.id == card.id}),
            !cards[chosenCardIndex].isFaceUp && !cards[chosenCardIndex].isMatched {
            if let potentialMatchIndex = indexOfTheOneAndOnlyFaceUpCard {
                if cards[potentialMatchIndex].content == cards[chosenCardIndex].content {
                    cards[potentialMatchIndex].isMatched = true
                    cards[chosenCardIndex].isMatched = true
                    scores.matches += 1
                }
                cards[chosenCardIndex].isFaceUp = true
            } else {
                for cardIndex in cards.indices {
                    if cards[cardIndex].isFaceUp {
                        if !cards[cardIndex].isMatched && indicesForCardsSeen.contains(cardIndex) {
                            scores.numberOfTimesSeenForUnmatchedCards += 1
                        } else if !indicesForCardsSeen.contains(cardIndex) {
                            indicesForCardsSeen.insert(cardIndex)
                        }
                    }
                }
                // This assignment sets the chosen card's isFaceUp to be true.
                indexOfTheOneAndOnlyFaceUpCard = chosenCardIndex
            }
        }
    }
    
    struct Card: Identifiable {
        var isFaceUp = false {
            didSet {
                if isFaceUp {
                    faceUpCountDownTimer.start()
                } else {
                    faceUpCountDownTimer.pause()
                }
            }
        }
        var isMatched = false {
            didSet {
                faceUpCountDownTimer.pause()
            }
        }
        var content: CardContent
        var id: Int
        /// Count down for the time period that the card is faced up and is not matched.
        private(set) var faceUpCountDownTimer = CountDownTimer()
        
        // whether we are currently face up, unmatched and have not yet used up the bonus window
        var isConsumingBonusTime: Bool {
            return isFaceUp && !isMatched && faceUpCountDownTimer.isCountingDown
        }
    }
    
    struct Scores {
        var matches = 0
        var numberOfTimesSeenForUnmatchedCards = 0
        var score: Int {
            matches * 2 - numberOfTimesSeenForUnmatchedCards
        }
    }
}

/// A timer that counts down from a given duration.
struct CountDownTimer {
    /// The duration the timer counts down from.
    var countDownDuration: TimeInterval = 6
    
    /// The countdown time remaining.
    var countDownRemaining: TimeInterval {
        return max(0, countDownDuration - lapsedTime)
    }
 
    /// Returns true if the timer is still counting time, i.e. there is time left.
    var isCountingDown: Bool {
        countDownRemaining > 0
    }
    
    /// Fraction of the countdown remaining, between 0 to 1.
    var fractionOfCountDownRemaining: Double {
        (countDownDuration > 0 && countDownRemaining > 0) ? countDownRemaining/countDownDuration : 0
    }
    
    // how long this card has ever been face up
    private var lapsedTime: TimeInterval {
        if let lastStartedDate = self.lastStartedDate {
            return pastLapsedTime + Date().timeIntervalSince(lastStartedDate)
        } else {
            return pastLapsedTime
        }
    }
    private var lastStartedDate: Date?
    private var pastLapsedTime: TimeInterval = 0
    
    /// Start/resume the count down. After the countdown is finished, this does not do anything anymore.
    mutating func start() {
        if isCountingDown, lastStartedDate == nil {
            lastStartedDate = Date()
        }
    }
    
    /// Pause the count down.
    mutating func pause() {
        pastLapsedTime = lapsedTime
        self.lastStartedDate = nil
    }
}

extension Array {
    var oneAndOnly: Element? {
        if count == 1 {
            return first
        } else {
            return nil
        }
    }
}
