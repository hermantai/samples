//
//  SizeUtil.swift
//  CardMatching
//
//  Created by Herman (Heung) Tai on 10/31/21.
//

import SwiftUI

// The width that can make all the items fitted into the size.
func widthThatFits(itemCount: Int, in size: CGSize, itemAspectRatio: CGFloat) -> CGFloat {
    var columnCount = 1
    var rowCount = itemCount
    repeat {
        let itemWidth = size.width / CGFloat(columnCount)
        let itemHeight = itemWidth / itemAspectRatio
        if  CGFloat(rowCount) * itemHeight < size.height {
            break
        }
        columnCount += 1
        // equivalent to: rowCount = Int(ceil(Double(itemCount) / Double(columnCount)))
        rowCount = (itemCount - 1) / columnCount + 1
    } while columnCount < itemCount
    if columnCount > itemCount {
        columnCount = itemCount
    }
    return floor(size.width / CGFloat(columnCount))
}
