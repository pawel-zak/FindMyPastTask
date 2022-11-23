package pawel.zak.findmypasttask.domain.use_case.user.validate_user

import pawel.zak.findmypasttask.R
import pawel.zak.findmypasttask.common.UiText

sealed class UserValidationError(val errorMessage: UiText) {
    object UserIdBlankError :
        UserValidationError(UiText.StringResource(R.string.user_id_blank_error))

    object FetchUserError :
        UserValidationError(UiText.StringResource(R.string.fetch_user_error))
}
