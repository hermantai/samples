//
//  EmojiArtDocumentView.swift
//  EmojiArt
//
//  Created by CS193p Instructor on 4/26/21.
//  Copyright © 2021 Stanford University. All rights reserved.
//

import SwiftUI

struct EmojiArtDocumentView: View {
    @ObservedObject var document: EmojiArtDocument
    
    let defaultEmojiFontSize: CGFloat = 40
    
    var body: some View {
        VStack(spacing: 0) {
            documentBody
            palette
        }
    }
    
    @State var selectedEmojis: Set<Int> = []
    /*
     A way to export a rect for a view, then be used in somewhere else.
     
    @State var rect: CGRect?
    func rectReader() -> some View {
        return GeometryReader { (geometry) -> AnyView in
            rect = geometry.frame(in: .global)
            return AnyView(Rectangle().fill(Color.clear))
        }
    }
     
     Text(..)
     // now the rect of this view is exported to `rect`
     .background(rectReader())
*/
    
    var documentBody: some View {
        GeometryReader { geometry in
            ZStack {
                Color.white.overlay(
                    OptionalImage(uiImage: document.backgroundImage)
                        .scaleEffect(zoomScale)
                        .position(convertFromEmojiCoordinates((0,0), in: geometry))
                )
                    .gesture(doubleTapToZoom(in: geometry.size).exclusively(before: tapToDeselectAllEmojis()))
                if document.backgroundImageFetchStatus == .fetching {
                    ProgressView().scaleEffect(2)
                } else {
                    ForEach(document.emojis) { emoji in
                        Text(emoji.text)
                            .border(.red, width: isEmojiSelected(emoji) ? 4 : 0)
                            .animatableSystemFont(size: fontSize(for: emoji) * (isEmojiSelected(emoji) ? zoomEmojisScale : zoomScale))
//                            // easy way to resize font but causing blurred text
//                            .font(.system(size: fontSize(for: emoji)))
//                            .scaleEffect(isEmojiSelected(emoji) ? zoomEmojisScale : zoomScale)
                            .position(position(for: emoji, in: geometry))
                            .onTapGesture {
                                toggleEmojiSelection(emoji)
                            }.onLongPressGesture {
                                document.removeEmoji(emoji)
                            }
                    }
                }
            }
            .clipped()
            .onDrop(of: [.plainText,.url,.image], isTargeted: nil) { providers, location in
                drop(providers: providers, at: location, in: geometry)
            }
            .gesture(
                // The map is for type erasure so AnyGesture is inferred to be AnyGeature<Void> for both cases.
                selectedEmojis.isEmpty
                ? AnyGesture(panGesture().simultaneously(with: zoomGesture()).map { _ in () })
                : AnyGesture(moveEmojisGesture(in: geometry).simultaneously(with: zoomEmojis()).map { _ in () })
            )
        }
    }
    
    // MARK: - Emoji selection
    func toggleEmojiSelection(_ emoji: EmojiArtModel.Emoji) {
        if isEmojiSelected(emoji) {
            selectedEmojis.remove(emoji.id)
        } else {
            selectedEmojis.insert(emoji.id)
        }
    }
    
    func isEmojiSelected(_ emoji: EmojiArtModel.Emoji) -> Bool {
        selectedEmojis.contains(emoji.id)
    }
    
    private func tapToDeselectAllEmojis() -> some Gesture {
        TapGesture(count: 1)
            .onEnded {
                selectedEmojis = []
            }
    }
    
    // MARK: - Drag and Drop
    
    private func drop(providers: [NSItemProvider], at location: CGPoint, in geometry: GeometryProxy) -> Bool {
        var found = providers.loadObjects(ofType: URL.self) { url in
            document.setBackground(.url(url.imageURL))
        }
        if !found {
            found = providers.loadObjects(ofType: UIImage.self) { image in
                if let data = image.jpegData(compressionQuality: 1.0) {
                    document.setBackground(.imageData(data))
                }
            }
        }
        if !found {
            found = providers.loadObjects(ofType: String.self) { string in
                if let emoji = string.first, emoji.isEmoji {
                    document.addEmoji(
                        String(emoji),
                        at: convertToEmojiCoordinates(location, in: geometry),
//                        // I believe the zoom only belongs to the View, not the model,
//                        // so the zoomScale should not be preserved in the model.
//                        // When we display a emoji, we respect the zoom scale. This
//                        // behavior is more intuitive than the one in the lecture
//                        // that the zoom scale affects the model stored, which creates
//                        // different sizes of emojis, which is weird.
//                        size: defaultEmojiFontSize / zoomScale
                        size: defaultEmojiFontSize
                    )
                }
            }
        }
        return found
    }
    
    // MARK: - Positioning/Sizing Emoji
    
    private func position(for emoji: EmojiArtModel.Emoji, in geometry: GeometryProxy) -> CGPoint {
        let x = emoji.x + (isEmojiSelected(emoji) ? Int(gestureEmojisMoveOffset.width) : 0)
        let y = emoji.y + (isEmojiSelected(emoji) ? Int(gestureEmojisMoveOffset.height) : 0)
        return convertFromEmojiCoordinates((x, y), in: geometry)
    }
    
    private func fontSize(for emoji: EmojiArtModel.Emoji) -> CGFloat {
        CGFloat(emoji.size)
    }
    
    private func convertToEmojiCoordinates(_ location: CGPoint, in geometry: GeometryProxy) -> (x: Int, y: Int) {
        let center = geometry.frame(in: .local).center
        let location = CGPoint(
            x: (location.x - panOffset.width - center.x) / zoomScale,
            y: (location.y - panOffset.height - center.y) / zoomScale
        )
        return (Int(location.x), Int(location.y))
    }
    
    private func convertFromEmojiCoordinates(_ location: (x: Int, y: Int), in geometry: GeometryProxy) -> CGPoint {
        let center = geometry.frame(in: .local).center
        return CGPoint(
            x: center.x + CGFloat(location.x) * zoomScale + panOffset.width,
            y: center.y + CGFloat(location.y) * zoomScale + panOffset.height
        )
    }
    
    // MARK: - Zooming
    
    @State private var steadyStateZoomScale: CGFloat = 1
    @GestureState private var gestureZoomScale: CGFloat = 1
    
    private var zoomScale: CGFloat {
        steadyStateZoomScale * gestureZoomScale
    }
    
    private func zoomGesture() -> some Gesture {
        MagnificationGesture()
            .updating($gestureZoomScale) { latestGestureScale, gestureZoomScale, _ in
                gestureZoomScale = latestGestureScale
            }
            .onEnded { gestureScaleAtEnd in
                steadyStateZoomScale *= gestureScaleAtEnd
            }
    }
    
    private func doubleTapToZoom(in size: CGSize) -> some Gesture {
        TapGesture(count: 2)
            .onEnded {
                withAnimation {
                    zoomToFit(document.backgroundImage, in: size)
                }
            }
    }
    
    private func zoomToFit(_ image: UIImage?, in size: CGSize) {
        if let image = image, image.size.width > 0, image.size.height > 0, size.width > 0, size.height > 0  {
            let hZoom = size.width / image.size.width
            let vZoom = size.height / image.size.height
            steadyStatePanOffset = .zero
            steadyStateZoomScale = min(hZoom, vZoom)
        }
    }
    
    // MARK: - zoom selected emojis only
    
    @GestureState private var gestureZoomEmojisScale: CGFloat = 1
    
    private var zoomEmojisScale: CGFloat {
        zoomScale * gestureZoomEmojisScale
    }
    
    private func zoomEmojis() -> some Gesture {
        MagnificationGesture()
            .updating($gestureZoomEmojisScale) { latestGestureScale, gestureZoomEmojisScale, _ in
                gestureZoomEmojisScale = latestGestureScale
            }
            .onEnded { gestureScaleAtEnd in
                document.emojis.filter(isEmojiSelected).forEach { emoji in
                    document.scaleEmoji(emoji, by: gestureScaleAtEnd)
                }
            }
    }
    // MARK: - Panning
    
    @State private var steadyStatePanOffset: CGSize = CGSize.zero
    @GestureState private var gesturePanOffset: CGSize = CGSize.zero
    
    private var panOffset: CGSize {
        (steadyStatePanOffset + gesturePanOffset) * zoomScale
    }
    
    private func panGesture() -> some Gesture {
        DragGesture()
            .updating($gesturePanOffset) { latestDragGestureValue, gesturePanOffset, _ in
                gesturePanOffset = latestDragGestureValue.translation / zoomScale
            }
            .onEnded { finalDragGestureValue in
                steadyStatePanOffset = steadyStatePanOffset + (finalDragGestureValue.translation / zoomScale)
            }
    }

    // MARK: - Moving emojis
    @GestureState private var gestureEmojisMoveOffset: CGSize = CGSize.zero
    
    private func moveEmojisGesture(in geometry: GeometryProxy) -> some Gesture {
        DragGesture()
            .updating($gestureEmojisMoveOffset) { latestDragGestureValue, gestureEmojisMoveOffset, _ in
                gestureEmojisMoveOffset = latestDragGestureValue.translation / zoomScale
            }
            .onEnded { finalDragGestureValue in
                document.emojis.filter(isEmojiSelected).forEach { emoji in
                    document.moveEmoji(emoji, by: finalDragGestureValue.translation / zoomScale)
                }
            }
    }
    
    // MARK: - Palette
    
    var palette: some View {
        ScrollingEmojisView(emojis: testEmojis)
            .font(.system(size: defaultEmojiFontSize))
    }
    
    let testEmojis = "😀😷🦠💉👻👀🐶🌲🌎🌞🔥🍎⚽️🚗🚓🚲🛩🚁🚀🛸🏠⌚️🎁🗝🔐❤️⛔️❌❓✅⚠️🎶➕➖🏳️"
}

struct ScrollingEmojisView: View {
    let emojis: String

    var body: some View {
        ScrollView(.horizontal) {
            HStack {
                ForEach(emojis.map { String($0) }, id: \.self) { emoji in
                    Text(emoji)
                        .onDrag { NSItemProvider(object: emoji as NSString) }
                }
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        EmojiArtDocumentView(document: EmojiArtDocument())
    }
}
