//
//  Pie.swift
//  CardMatching
//
//  Created by Herman (Heung) Tai on 10/28/21.
//

import SwiftUI

struct Pie: Shape {
    // The start angle of the Pie. The angle is measured from the right, then goes clockwise.
    var startAngle: Angle
    // The start angle of the Pie. The angle is measured from the right, then goes clockwise.
    var endAngle: Angle
    
    // This is clockwise in the natural sense, not inverted in graphics.
    var clockwise = false
    
    // the Shape protocol inherits from Animatable
    // and this var is the only thing in Animatable
    // so by implementing it to get/set our pair of angles
    // we are thus animatable
    var animatableData: AnimatablePair<Double, Double> {
        get {
            AnimatablePair(startAngle.radians, endAngle.radians)
        }
        set {
            startAngle = Angle.radians(newValue.first)
            endAngle = Angle.radians(newValue.second)
        }
    }
    
    func path(in rect: CGRect) -> Path {
        let center = CGPoint(x: rect.midX, y: rect.midY)
        let radius = min(rect.width, rect.height) / 2
        let start = CGPoint(
            x: center.x + radius * cos(startAngle.radians),
            y: center.y + radius * sin(startAngle.radians)
        )
        
        var p = Path()
        p.move(to: center)
        p.addLine(to: start)
        p.addArc(
            center: center,
            radius: radius,
            startAngle: startAngle,
            endAngle: endAngle,
            clockwise: !clockwise)
        p.addLine(to: center)
        
        return p
    }
}
