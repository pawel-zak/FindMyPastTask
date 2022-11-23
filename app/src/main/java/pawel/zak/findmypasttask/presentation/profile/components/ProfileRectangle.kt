package pawel.zak.findmypasttask.presentation.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import coil.compose.AsyncImage
import pawel.zak.findmypasttask.domain.model.Profile
import pawel.zak.findmypasttask.R

@Composable
fun ProfileRectangle(
    width: Dp,
    textSize: TextUnit,
    profile: Profile?,
    modifier: Modifier = Modifier,
    invisible: Boolean = false
) {
    Card(
        modifier = modifier
            .alpha(if (invisible) 0f else 1f)
            .heightIn(min = width)
            .width(width)
            .padding(dimensionResource(R.dimen.small_50)),
        elevation = dimensionResource(R.dimen.small_125),
        shape = RoundedCornerShape(dimensionResource(R.dimen.normal_125))
    ) {
        Column(
            modifier = modifier.padding(dimensionResource(R.dimen.small_125))
        ) {

            AsyncImage(
                model = profile?.image,
                contentDescription = "${profile?.firstname} ${profile?.surname}",
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .align(CenterHorizontally)
                    .clip(shape = CircleShape)
                    .size(width / 2),
                error = painterResource(R.drawable.ic_baseline_person_48)
            )
            Spacer(modifier = modifier.height(dimensionResource(R.dimen.small_50)))
            ResponsiveText(
                text = profile?.firstname ?: "-",
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
                modifier = modifier
                    .align(CenterHorizontally),
                textStyle = TextStyle(fontSize = textSize)
            )
            ResponsiveText(
                text = profile?.surname ?: "-",
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
                modifier = modifier
                    .align(CenterHorizontally),
                textStyle = TextStyle(fontSize = textSize)
            )
            ResponsiveText(
                text = profile?.dateOfBirth ?: "",
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
                modifier = modifier
                    .align(CenterHorizontally),
                textStyle = TextStyle(fontSize = textSize)
            )
        }
    }
}
