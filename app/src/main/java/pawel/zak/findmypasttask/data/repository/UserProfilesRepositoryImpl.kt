package pawel.zak.findmypasttask.data.repository

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import pawel.zak.findmypasttask.common.ResourceRequest
import pawel.zak.findmypasttask.common.Resource
import pawel.zak.findmypasttask.data.local.UserProfilesDao
import pawel.zak.findmypasttask.data.local.UserProfilesDatabase
import pawel.zak.findmypasttask.data.local.model.UserLocal
import pawel.zak.findmypasttask.data.local.model.toLocalProfile
import pawel.zak.findmypasttask.data.local.model.toProfile
import pawel.zak.findmypasttask.data.local.model.toUserProfilesCrossRef
import pawel.zak.findmypasttask.data.remote.ProfileApi
import pawel.zak.findmypasttask.data.remote.model.toProfile
import pawel.zak.findmypasttask.domain.model.Profile
import pawel.zak.findmypasttask.domain.repository.UserProfilesRepository
import javax.inject.Inject

class UserProfilesRepositoryImpl @Inject constructor(
    private val api: ProfileApi,
    private val userProfilesDao: UserProfilesDao,
    private val database: UserProfilesDatabase
) : UserProfilesRepository {

    override suspend fun getProfiles(userId: String): Flow<Resource<List<Profile>>> {

        return if (userProfilesDao.isUserProfilesStored(userId))
            ResourceRequest.safeRequest { userProfilesDao.getUserProfiles(userId)!!.profiles.map { it.toProfile() } }
        else
            ResourceRequest.safeRequest {
                api.getProfiles(userId).profiles.map { it.toProfile() }
                    .also { profileList ->
                        insertDataToDataBase(userId, profileList)
                    }
            }
    }

    override suspend fun getProfile(userId: String, profileId: String): Flow<Resource<Profile>> =
        ResourceRequest.safeRequest {
            val profile = userProfilesDao.getProfile(profileId)

            if (profile?.relationships == null)
                return@safeRequest api.getProfile(userId, profileId).profile.toProfile().also {
                    insertDataToDataBase(userId, listOf(it))
                }
            else
                return@safeRequest profile.toProfile()

        }

    private suspend fun insertDataToDataBase(userId: String, profileList: List<Profile>) {
        database.withTransaction {
            userProfilesDao.insertUser(UserLocal(userId))
            userProfilesDao.insertProfile(
                *profileList.map { it.toLocalProfile() }
                    .toTypedArray())
            userProfilesDao.insertUserProfiles(*profileList.map {
                it.toUserProfilesCrossRef(
                    userId
                )
            }.toTypedArray())
        }
    }
}