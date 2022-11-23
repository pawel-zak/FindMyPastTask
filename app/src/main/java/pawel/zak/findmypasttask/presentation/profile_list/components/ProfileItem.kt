package pawel.zak.findmypasttask.presentation.profile_list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import pawel.zak.findmypasttask.domain.model.Profile
import pawel.zak.findmypasttask.R

@Composable
fun ProfileItem(profile: Profile, onItemClick: (Profile) -> Unit) {

    Box(
        modifier = Modifier
            .clickable(
                onClick = {
                    onItemClick(profile)
                }
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.small_100)),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = profile.image,
                contentDescription = "${profile.firstname} ${profile.surname}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .size(dimensionResource(R.dimen.large_150)),
                error = painterResource(R.drawable.ic_baseline_person_48)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(R.dimen.small_100))
            ) {

                Text(
                    text = "${profile.firstname} ${profile.surname}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.h6
                )
                Text(
                    modifier = Modifier
                        .padding(top = dimensionResource(R.dimen.small_25)),
                    text = profile.dateOfBirth,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body2
                )
            }
        }
        Image(
            modifier = Modifier
                .align(alignment = Alignment.CenterEnd)
                .padding(dimensionResource(R.dimen.small_100)),
            painter = painterResource(id = R.drawable.ic_baseline_arrow_forward_ios_24),
            contentDescription = stringResource(R.string.profile_arrow_content_description)
        )
    }
}