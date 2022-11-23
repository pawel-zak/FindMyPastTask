package pawel.zak.findmypasttask.domain.use_case.user.validate_user

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import pawel.zak.findmypasttask.common.Resource
import pawel.zak.findmypasttask.domain.repository.UserProfilesRepository
import javax.inject.Inject

class ValidateUserUseCase @Inject constructor(private val repository: UserProfilesRepository) {
    suspend operator fun invoke(userId: String): Flow<Resource<UserValidationResult>> = flow {

        if (userId.isBlank()) {
            emit(
                Resource.Error(
                    message = UserValidationError.UserIdBlankError.errorMessage,
                    data = UserValidationResult(
                        false,
                        UserValidationError.UserIdBlankError
                    )
                )
            )
        } else
            repository.getProfiles(userId).map {
                when (it) {
                    is Resource.Success -> {
                        emit(Resource.Success(UserValidationResult(successful = true)))
                    }
                    is Resource.Error -> {
                        emit(
                            Resource.Error(
                                message = UserValidationError.FetchUserError.errorMessage,
                                UserValidationResult(
                                    successful = false,
                                    error = UserValidationError.FetchUserError
                                )
                            )
                        )
                    }
                    is Resource.Loading -> {
                        emit(Resource.Loading())
                    }
                }
            }.collect()
    }
}