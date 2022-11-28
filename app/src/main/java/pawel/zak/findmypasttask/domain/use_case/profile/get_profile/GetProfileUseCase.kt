package pawel.zak.findmypasttask.domain.use_case.profile.get_profile

import kotlinx.coroutines.flow.Flow
import pawel.zak.findmypasttask.common.Resource
import pawel.zak.findmypasttask.domain.model.Profile
import pawel.zak.findmypasttask.domain.repository.UserProfilesRepository
import javax.inject.Inject


class GetProfileUseCase @Inject constructor(private val repository: UserProfilesRepository) {
    suspend operator fun invoke(userId: String, profileId: String): Flow<Resource<Profile>> {
        return repository.getMainProfile(userId, profileId)
    }
}