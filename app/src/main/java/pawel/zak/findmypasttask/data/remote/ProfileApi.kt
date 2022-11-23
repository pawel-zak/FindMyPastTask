package pawel.zak.findmypasttask.data.remote

import pawel.zak.findmypasttask.data.remote.model.ProfileDto
import pawel.zak.findmypasttask.data.remote.model.UserProfilesDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileApi {

    @GET("/profiles/{userId}")
    suspend fun getProfiles(@Path("userId") userId: String): UserProfilesDto

    @GET("profile/{profileId}/{userId}")
    suspend fun getProfile(
        @Path("userId") userId: String,
        @Path("profileId") profileId: String
    ): ProfileDto

}