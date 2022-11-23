package pawel.zak.findmypasttask.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Relationships(
    val father: String?,
    val mother: String?,
    val spouse: String?,
    val children: List<String>? = listOf()
): Parcelable
