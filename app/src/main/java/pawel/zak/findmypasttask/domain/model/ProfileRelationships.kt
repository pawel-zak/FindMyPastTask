package pawel.zak.findmypasttask.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileRelationships(
    val father: Profile? = null,
    val mother: Profile? = null,
    val spouse: Profile? = null,
    val children: List<Profile?>? = listOf()
) : Parcelable