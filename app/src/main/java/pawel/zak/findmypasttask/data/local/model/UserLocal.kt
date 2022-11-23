package pawel.zak.findmypasttask.data.local.model

import androidx.room.*

@Entity
data class UserLocal(
    @PrimaryKey
    val userId: String
)

@Entity(primaryKeys = ["userId", "profileId"])
data class UserProfilesCrossRef(
    val userId: String,
    val profileId: String
)

data class UserWithProfiles(
    @Embedded val user: UserLocal,
    @Relation(
        parentColumn = "userId",
        entityColumn = "profileId",
        associateBy = Junction(UserProfilesCrossRef::class)
    )
    val profiles: List<ProfileLocal>
)