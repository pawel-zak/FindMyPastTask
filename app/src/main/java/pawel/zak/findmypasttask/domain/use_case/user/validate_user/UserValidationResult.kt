package pawel.zak.findmypasttask.domain.use_case.user.validate_user

data class UserValidationResult(val successful: Boolean, val error: UserValidationError? = null)