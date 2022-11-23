package pawel.zak.findmypasttask.presentation.profile.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import pawel.zak.findmypasttask.presentation.profile.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    RelativesTree(
        father = state.profileRelationships?.father,
        mother = state.profileRelationships?.mother,
        mainProfile = state.profile,
        spouse = state.profileRelationships?.spouse,
        children = state.profileRelationships?.children?.filterNotNull()
    )
}