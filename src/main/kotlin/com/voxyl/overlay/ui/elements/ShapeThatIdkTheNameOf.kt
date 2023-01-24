package com.voxyl.overlay.ui.elements

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class ShapeThatIdkTheNameOf(private val size: Size) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = drawMyShapePath(size = size)
        )
    }

    private fun drawMyShapePath(size: Size): Path {
        return Path().apply {
            reset()

            arcTo(
                rect = Rect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.width
                ),
                startAngleDegrees = 180.0f,
                sweepAngleDegrees = -180.0f,
                forceMoveTo = false
            )
            lineTo(x = size.width, y = size.height)
            arcTo(
                rect = Rect(
                    left = 0f,
                    top = size.height - size.width,
                    right = size.width,
                    bottom = size.height
                ),
                startAngleDegrees = 0.0f,
                sweepAngleDegrees = 180.0f,
                forceMoveTo = false
            )
            lineTo(x = 0f, y = size.height - size.width)

            close()
        }
    }
}
