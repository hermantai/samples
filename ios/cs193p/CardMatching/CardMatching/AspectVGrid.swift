//
//  AspectVGrid.swift
//  CardMatching
//
//  Created by Herman (Heung) Tai on 10/28/21.
//
//  Reference: https://youtu.be/Og9gXZpbKWo
//

import SwiftUI

/// Arranges items such that they are all visible within this container without scrolling.
struct AspectVGrid<Item: Identifiable, Content: View>: View {
    /// The elements to be placed in the grid.
    var items: [Item]
    
    /// The width/height ratio for each element.
    var aspectRatio: CGFloat
    
    var content: (Item) -> Content
    
    /// Creates a grid that grows vertically given the provided properties.
    ///
    /// - Parameters:
    ///     - items: the items to be laid out
    ///
    ///     - aspectRatio: the width/height ratio for each item
    ///     
    ///     - content:takes an item to return some view
    init(items: [Item], aspectRatio: CGFloat, @ViewBuilder content: @escaping (Item) -> Content) {
        self.items = items
        self.aspectRatio = aspectRatio
        self.content = content
    }
    
    var body: some View {
        GeometryReader { geometry in
            VStack {
                let width = widthThatFits(itemCount: items.count, in: geometry.size, itemAspectRatio: aspectRatio)
                LazyVGrid(columns: [adaptiveGridItem(width: width)], spacing: 0) {
                    ForEach(items) { item in
                        content(item)
                            .aspectRatio(aspectRatio, contentMode: .fit)
                    }
                }
                
                // This is for making the content of GeometryReader flexible which takes the whole
                // size offered to it.
                Spacer(minLength: 0)
            }
        }
    }

    private func adaptiveGridItem(width: CGFloat) -> GridItem {
        var gridItem = GridItem(.adaptive(minimum: width))
        gridItem.spacing = 0
        return gridItem
    }
}
