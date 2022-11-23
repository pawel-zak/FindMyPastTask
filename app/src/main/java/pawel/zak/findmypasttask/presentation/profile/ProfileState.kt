package pawel.zak.findmypasttask.presentation.profile

import pawel.zak.findmypasttask.common.UiText
import pawel.zak.findmypasttask.domain.model.Profile
import pawel.zak.findmypasttask.domain.model.ProfileRelationships

data class ProfileState(
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val profileRelationships: ProfileRelationships? = null,
    val error: UiText? = null
)
