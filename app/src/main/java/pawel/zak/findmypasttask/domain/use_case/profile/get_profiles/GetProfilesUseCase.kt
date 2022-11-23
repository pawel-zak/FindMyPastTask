package pawel.zak.findmypasttask.domain.use_case.profile.get_profiles

import kotlinx.coroutines.flow.Flow
import pawel.zak.findmypasttask.common.Resource
import pawel.zak.findmypasttask.domain.model.Profile
import pawel.zak.findmypasttask.domain.repository.UserProfilesRepository
import javax.inject.Inject

class GetProfilesUseCase @Inject constructor(private val repository: UserProfilesRepository) {
    suspend operator fun invoke(userId: String): Flow<Resource<List<Profile>>> {
        return repository.getProfiles(userId)
    }
}
