package pawel.zak.findmypasttask.presentation.profile.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pawel.zak.findmypasttask.domain.model.Profile

@Composable
fun RelativesTree(
    father: Profile?,
    mother: Profile?,
    mainProfile: Profile?,
    spouse: Profile?,
    children: List<Profile>?,
    modifier: Modifier = Modifier,
    colorOfColor: Color = MaterialTheme.colors.secondary,
    strokeWidth: Float = 10f,
    maxWidth: Dp = dimensionResource(pawel.zak.findmypasttask.R.dimen.large_375),
    textSize: TextUnit = 12.sp
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Box(modifier = modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.matchParentSize()) {

            RelativesLines.FatherLines(size.width, size.height).lines.forEach {
                drawLine(
                    start = it.startingOffset,
                    end = it.endOffset,
                    color = colorOfColor,
                    strokeWidth = strokeWidth
                )
            }


            RelativesLines.MotherLines(size.width, size.height).lines.forEach {
                drawLine(
                    start = it.startingOffset,
                    end = it.endOffset,
                    color = colorOfColor,
                    strokeWidth = strokeWidth
                )
            }


            RelativesLines.ProfileLines(size.width, size.height).lines.forEach {
                drawLine(
                    start = it.startingOffset,
                    end = it.endOffset,
                    color = colorOfColor,
                    strokeWidth = strokeWidth
                )
            }


            spouse?.let {
                RelativesLines.SpouseLines(size.width, size.height).lines.forEach {
                    drawLine(
                        start = it.startingOffset,
                        end = it.endOffset,
                        color = colorOfColor,
                        strokeWidth = strokeWidth
                    )
                }
            }

            children?.forEachIndexed { index, _ ->
                RelativesLines.ChildrenLines(
                    size.width,
                    size.height,
                    children.size,
                    index
                ).lines.forEach {
                    drawLine(
                        start = it.startingOffset,
                        end = it.endOffset,
                        color = colorOfColor,
                        strokeWidth = strokeWidth
                    )
                }
            }
        }

        Column(modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly) {
//PARENTS
            Row(
                modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfileRectangle(
                    maxWidth,
                    textSize,
                    profile = father
                )

                ProfileRectangle(
                    maxWidth,
                    textSize,
                    profile = mother
                )
            }
//MAIN PROFILE & SPOUSE
            Row(
                modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                ProfileRectangle(
                    maxWidth,
                    textSize,
                    profile = mainProfile
                )

                ProfileRectangle(
                    maxWidth,
                    textSize,
                    profile = spouse,
                    invisible = spouse == null
                )
            }
//CHILDREN
            Row(
                modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (children != null && children.isNotEmpty()) {
                    val availableSize = screenWidth / children.size

                    val sizeOfProfileRectangle =
                        if (availableSize.value > maxWidth.value) maxWidth else availableSize

                    children.forEach { child ->
                        ProfileRectangle(
                            sizeOfProfileRectangle,
                            textSize,
                            profile = child
                        )
                    }
                } else
                    ProfileRectangle(
                        maxWidth,
                        textSize,
                        profile = null,
                        invisible = true
                    )
            }

        }
    }
}