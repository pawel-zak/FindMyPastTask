package pawel.zak.findmypasttask.presentation.profile_list.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import pawel.zak.findmypasttask.R
import pawel.zak.findmypasttask.presentation.profile_list.ProfileListViewModel

@Composable
fun ProfileListScreen(
    showProfile: (userId: String, profileId: String) -> Unit,
    viewModel: ProfileListViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.profiles) { profile ->
                ProfileItem(profile = profile, onItemClick = {
                    showProfile(viewModel.userId, profile.id)
                })
            }
        }
        if (state.error?.asString()?.isNotBlank() == true) {
            Text(
                text = state.error?.asString() ?: "",
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.normal_125))
                    .align(Alignment.Center)
            )
        }
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}