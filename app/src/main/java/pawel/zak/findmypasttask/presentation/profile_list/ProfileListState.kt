package pawel.zak.findmypasttask.presentation.profile_list

import pawel.zak.findmypasttask.common.UiText
import pawel.zak.findmypasttask.domain.model.Profile

data class ProfileListState(
    val isLoading: Boolean = false,
    val profiles: List<Profile> = emptyList(),
    val error: UiText? = null
)
