package pawel.zak.findmypasttask.data.remote.model


import com.google.gson.annotations.SerializedName

data class UserProfilesDto(
    @SerializedName("data")
    val profiles: List<ProfileRemote>,
    val success: Boolean,
    val error: String?
)