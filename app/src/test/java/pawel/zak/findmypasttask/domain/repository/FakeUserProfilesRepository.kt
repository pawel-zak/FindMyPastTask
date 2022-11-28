package pawel.zak.findmypasttask.domain.repository

import kotlinx.coroutines.flow.Flow
import pawel.zak.findmypasttask.common.Resource
import pawel.zak.findmypasttask.domain.model.Profile

internal class FakeUserProfilesRepository: UserProfilesRepository{

    override suspend fun getProfiles(userId: String): Flow<Resource<List<Profile>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMainProfile(userId: String, profileId: String): Flow<Resource<Profile>> {
        TODO("Not yet implemented")
    }

    override suspend fun getProfile(userId: String, profileId: String): Flow<Resource<Profile>> {
        TODO("Not yet implemented")
    }
}