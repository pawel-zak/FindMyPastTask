package pawel.zak.findmypasttask.data.remote.model

import com.google.gson.annotations.SerializedName


data class ProfileDto(
    @SerializedName("data")
    val profile: ProfileRemote,
    val success: Boolean
)