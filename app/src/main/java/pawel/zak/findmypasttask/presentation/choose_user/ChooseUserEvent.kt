package pawel.zak.findmypasttask.presentation.choose_user

sealed class ChooseUserEvent {
    data class UserIdChanged(val userId: String) : ChooseUserEvent()
    data class Submit(val userId: String) : ChooseUserEvent()
}
