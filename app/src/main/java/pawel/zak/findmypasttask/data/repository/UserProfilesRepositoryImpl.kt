package pawel.zak.findmypasttask.data.repository

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import pawel.zak.findmypasttask.common.ResourceRequest
import pawel.zak.findmypasttask.common.Resource
import pawel.zak.findmypasttask.common.UiText
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
            ResourceRequest.safeRequest { userProfilesDao.getUserProfiles(userId) }
                .map { resource ->
                    mapRequest(resource, true, resource.message) {
                        resource.data!!.profiles.map { it.toProfile() }
                    }
                }
        else
            ResourceRequest.safeRequest {
                api.getProfiles(userId)
            }.map { it ->
                mapRequest(
                    resource = it,
                    isSuccess = it.data?.success == true,
                    message = it.message
                ) {
                    it.data!!.profiles.map { it.toProfile() }.also { profileList ->
                        insertDataToDataBase(userId, profileList)
                    }
                }
            }
    }

    override suspend fun getMainProfile(
        userId: String,
        profileId: String
    ): Flow<Resource<Profile>> =
        ResourceRequest.safeRequest(false) { userProfilesDao.getProfile(profileId) }
            .flatMapConcat { localResource ->
                if (localResource.data?.toProfile()?.relationships == null) {
                    ResourceRequest.safeRequest {
                        api.getProfile(userId, profileId)
                    }.map { resource ->
                        mapRequest(
                            resource = resource,
                            isSuccess = resource.data?.success == true,
                            message = resource.message
                        ) {
                            resource.data!!.profile.toProfile().also {
                                insertDataToDataBase(userId, listOf(it))
                            }
                        }
                    }
                } else {
                    ResourceRequest.safeRequest { localResource.data.toProfile() }
                }
            }


    override suspend fun getProfile(userId: String, profileId: String): Flow<Resource<Profile>> =
        ResourceRequest.safeRequest { userProfilesDao.getProfile(profileId) }.map {
            mapRequest(it, true, it.message) {
                it.data!!.toProfile()
            }
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

suspend fun <T, R> mapRequest(
    resource: Resource<R>,
    isSuccess: Boolean,
    message: UiText?,
    onSuccess: suspend () -> T
) = when (resource) {
    is Resource.Success -> {
        if (isSuccess)
            Resource.Success(onSuccess())
        else
            Resource.Error(message ?: UiText.DynamicString("unknown error, message null"))
    }
    is Resource.Error -> {
        Resource.Error(message ?: UiText.DynamicString("unknown error, message null"))
    }
    is Resource.Loading -> {
        Resource.Loading()
    }
}

