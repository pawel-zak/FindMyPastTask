package pawel.zak.findmypasttask.data.local.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import pawel.zak.findmypasttask.domain.model.Profile

@Entity
data class ProfileLocal(
    @PrimaryKey val profileId: String,
    val dob: String,
    val firstname: String,
    val image: String?,
    @Embedded val relationships: RelationshipsLocal? = null,
    val surname: String
)

fun ProfileLocal.toProfile() = Profile(
    id = profileId,
    firstname = firstname,
    surname = surname,
    dateOfBirth = dob,
    image = image,
    relationships = relationships?.toRelationships()
)

fun Profile.toLocalProfile() =
    ProfileLocal(
        profileId = id,
        firstname = firstname,
        surname = surname,
        dob = dateOfBirth,
        image = image,
        relationships = relationships?.toLocalRelationships()
    )

fun Profile.toUserProfilesCrossRef(
    userId: String
): UserProfilesCrossRef {
    return UserProfilesCrossRef(userId, this.id)
}