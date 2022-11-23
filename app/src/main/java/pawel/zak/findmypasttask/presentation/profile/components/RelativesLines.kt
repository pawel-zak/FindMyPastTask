package pawel.zak.findmypasttask.presentation.profile.components

import androidx.compose.ui.geometry.Offset


sealed class RelativesLines(val lines: List<Line>) {

    class FatherLines(width: Float, height: Float) : RelativesLines(
        listOf(
            Line(
                Offset(0.25f * width, 0.20f * height),
                Offset(0.5f * width, 0.20f * height)
            ),
            Line(
                Offset(0.5f * width, 0.20f * height),
                Offset(0.5f * width, 0.33f * height)
            )
        )
    )

    class MotherLines(width: Float, height: Float) : RelativesLines(
        listOf(
            Line(
                Offset(0.75f * width, 0.20f * height),
                Offset(0.5f * width, 0.20f * height)
            ),
            Line(
                Offset(0.5f * width, 0.20f * height),
                Offset(0.5f * width, 0.33f * height)
            )
        )
    )

    class ProfileLines(width: Float, height: Float) : RelativesLines(
        listOf(
            Line(
                Offset(0.25f * width, 0.5f * height),
                Offset(0.25f * width, 0.33f * height)
            ),
            Line(
                Offset(0.25f * width, 0.33f * height),
                Offset(0.5f * width, 0.33f * height)
            )
        )
    )

    class SpouseLines(width: Float, height: Float) : RelativesLines(
        listOf(
            Line(
                Offset(0.75f * width, 0.5f * height),
                Offset(0.25f * width, 0.5f * height)
            )
        )
    )


    class ChildrenLines(
        width: Float,
        height: Float,
        numberOfChildren: Int,
        indexOfChild: Int,
        startWidth: Float = width / (2 * numberOfChildren),
        distance: Float = width / numberOfChildren
    ) : RelativesLines(
        listOf(
            Line(
                Offset((startWidth + (indexOfChild) * distance), 0.75f * height),
                Offset((startWidth + (indexOfChild) * distance), 0.66f * height)
            ),
            Line(
                Offset((startWidth + (indexOfChild) * distance), 0.66f * height),
                Offset(0.5f * width, 0.66f * height)
            ),
            Line(
                Offset(0.5f * width, 0.66f * height),
                Offset(0.5f * width, 0.5f * height)
            ),
            Line(
                Offset(0.25f * width, 0.5f * height),
                Offset(0.5f * width, 0.5f * height)
            )
        )
    )
}

data class Line(val startingOffset: Offset, val endOffset: Offset)
