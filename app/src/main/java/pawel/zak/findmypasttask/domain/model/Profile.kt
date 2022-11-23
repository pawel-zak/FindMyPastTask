package pawel.zak.findmypasttask.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Profile(
    val id: String,
    val firstname: String,
    val surname: String,
    val dateOfBirth: String,
    val image: String?,
    val relationships: Relationships? = null
): Parcelable
