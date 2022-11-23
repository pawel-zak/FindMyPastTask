package pawel.zak.findmypasttask.presentation.choose_user

import pawel.zak.findmypasttask.common.UiText


data class ChooseUserState(
    val isLoading: Boolean = false,
    val userId: String = "",
    val userIdError: UiText? = null
)
