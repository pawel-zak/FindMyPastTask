package pawel.zak.findmypasttask.domain.repository

import kotlinx.coroutines.flow.Flow
import pawel.zak.findmypasttask.common.Resource
import pawel.zak.findmypasttask.domain.model.Profile

interface UserProfilesRepository {
    suspend fun getProfiles(userId: String): Flow<Resource<List<Profile>>>
    suspend fun getProfile(userId: String, profileId: String): Flow<Resource<Profile>>
}