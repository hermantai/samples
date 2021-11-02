//
//  AnimationLifeCycleAnimatableModifier.swift
//  CardMatching
//
//  An AnimatableModifier that allows for checking if we are animating, so that the completion
//  callback is called only at the end of the animation.
//
//
//  Only the following should be used outside of ths file:
//    - AnimationLifeCycleObserver
//    - View.withLifeCycleObserver
//    - View.onAnimationCompleted
//
//  Other variables should be treated as fileprivate even not marked as such.
//
//  Created by Herman (Heung) Tai on 10/31/21.
//

import SwiftUI

/// Allows for checking if we are animating, so that the completion
/// callback is called only at the end of the animation. View.withLifeCycleObserver should be used instead of using this class directly.
///
/// See withLifeCycleObserver for usage.
struct AnimationLifeCycleAnimatableModifier: AnimatableModifier {
    private var completion: () -> Void;
    
    /// Used with the Animatable protocol to allow for switching the observer.isAnimating from true to false gradually.
    var animatableData: Double {
        didSet {
            notifyCompletionIfFinished()
        }
    }
    
    /// - Parameters:
    ///   - observer: a switch that triggers the animation
    ///
    ///   - completion: a callback that takes the switchToggle as the argument. It's expected that this callback should call observer.stop().
    fileprivate init(_ observer: IsAnimatingObserver, completion: @escaping () -> Void) {
        animatableData = observer.isAnimating ? 1 : 0
        self.completion = completion
    }
    
    func body(content: Content) -> some View {
        content
    }
    
    /// Verifies whether the current animation is finished and calls the completion callback if true.
    private func notifyCompletionIfFinished() {
        guard (animatableData == 1) else { return }

        /// Dispatching is needed to take the next runloop for the completion callback.
        /// This prevents errors like "Modifying state during view update, this will cause undefined behavior."
        DispatchQueue.main.async {
            self.completion()
        }
    }
    
    /// An object to hold the isAnimating variable. Due to the restriction of AnimatableModifier cannot be used as a type,
    /// AnimationLifeCycleObserver in the upper level is created to ease the use of this class.
    class _AnimationLifeCycleObserver<MyAnimatableModifier: AnimatableModifier>: ObservableObject, IsAnimatingObserver {
        @Published private(set) var isAnimating = false
        
        func start() {
            isAnimating = true
        }
        
        func stop() {
            isAnimating = false
        }
        
        func createAnimatableModifier(completion: @escaping () -> Void = {}) -> MyAnimatableModifier {
            // return self.createAnimatableModifierFunc(self)
            AnimationLifeCycleAnimatableModifier(self) {
                self.stop()
                completion()
            } as! MyAnimatableModifier
        }
    }
}

/// An object to hold the isAnimating variable. See View.withLifeCycleObserver for usage.
typealias AnimationLifeCycleObserver = AnimationLifeCycleAnimatableModifier._AnimationLifeCycleObserver<AnimationLifeCycleAnimatableModifier>

fileprivate protocol IsAnimatingObserver {
    var isAnimating: Bool { get }
}

extension View {
    /// Allows for checking if we are animating, so that the completion
    /// callback is called only at the end of the animation.
    ///
    /// - Parameters:
    ///   - observer: a switch that triggers the animation
    ///
    ///   - completion: a callback that takes the switchToggle as the argument. It's expected that this callback should call observer.stop().
    ///
    /// An example of usage is in the following. It allows for show/hide buttons to be disabled
    /// during the animation of the rectangle, and only enabled after the animation completes:
    ///
    ///     @State var showRect = true
    ///     @ObservedObject private var rectAnimationObserver = AnimationLifeCycleObserver();
    ///
    ///     var myTestButton: some View {
    ///         HStack {
    ///             Rectangle().fill()
    ///                 .opacity(showRect ? 1 : 0)
    ///             Button("Show") {
    ///                 withAnimation(.linear(duration: 2)) {
    ///                     showRect = true
    ///                     rectAnimationObserver.start()
    ///                 }
    ///             }
    ///             .withLifeCycleObserver(rectAnimationObserver) {
    ///                 print("rect animation done from Show button")
    ///             }.disabled(showRect || rectAnimationObserver.isAnimating)
    ///
    ///             Button("Hide") {
    ///                 withAnimation(.linear(duration: 2)) {
    ///                     showRect = false
    ///                     rectAnimationObserver.start()
    ///                 }
    ///             }
    ///             .withLifeCycleObserver(rectAnimationObserver) {
    ///                 print("rect animation done from Hide button")
    ///             }.disabled(!showRect || rectAnimationObserver.isAnimating)
    ///         }
    ///     }
    func withLifeCycleObserver(
        _ observer: AnimationLifeCycleObserver,
        completion: @escaping () -> Void = {}) -> some View {
            return self.modifier(observer.createAnimatableModifier(completion: completion))
    }
}

/// An animatable modifier that is used for observing animations for a given animatable value.
/// From: https://www.avanderlee.com/swiftui/withanimation-completion-callback/
struct AnimationCompletionObserverModifier<Value>: AnimatableModifier where Value: VectorArithmetic {

    /// While animating, SwiftUI changes the old input value to the new target value using this property. This value is set to the old value until the animation completes.
    var animatableData: Value {
        didSet {
            notifyCompletionIfFinished()
        }
    }

    /// The target value for which we're observing. This value is directly set once the animation starts. During animation, `animatableData` will hold the oldValue and is only updated to the target value once the animation completes.
    private var targetValue: Value

    /// The completion callback which is called once the animation completes.
    private var completion: () -> Void

    init(observedValue: Value, completion: @escaping () -> Void) {
        self.completion = completion
        self.animatableData = observedValue
        targetValue = observedValue
    }

    /// Verifies whether the current animation is finished and calls the completion callback if true.
    private func notifyCompletionIfFinished() {
        guard animatableData == targetValue else { return }

        /// Dispatching is needed to take the next runloop for the completion callback.
        /// This prevents errors like "Modifying state during view update, this will cause undefined behavior."
        DispatchQueue.main.async {
            self.completion()
        }
    }

    func body(content: Content) -> some View {
        /// We're not really modifying the view so we can directly return the original input value.
        return content
    }
}

extension View {
    /// Calls the completion handler whenever an animation on the given value completes.
    ///
    ///      Text("Welcome to SwiftLee")
    ///          .opacity(introTextOpacity)
    ///          .onAnimationCompleted(for: introTextOpacity) {
    ///              print("Intro text animated in!")
    ///           }
    ///
    /// - Parameters:
    ///   - value: The value to observe for animations.
    ///   - completion: The completion callback to call once the animation completes.
    /// - Returns: A modified `View` instance with the observer attached.
    func onAnimationCompleted<Value: VectorArithmetic>(for value: Value, completion: @escaping () -> Void) -> ModifiedContent<Self, AnimationCompletionObserverModifier<Value>> {
        return modifier(AnimationCompletionObserverModifier(observedValue: value, completion: completion))
    }
}
