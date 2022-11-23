package pawel.zak.findmypasttask.data.local

import androidx.room.*
import pawel.zak.findmypasttask.data.local.model.ProfileLocal
import pawel.zak.findmypasttask.data.local.model.UserLocal
import pawel.zak.findmypasttask.data.local.model.UserProfilesCrossRef
import pawel.zak.findmypasttask.data.local.model.UserWithProfiles


@Dao
interface UserProfilesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userLocal: UserLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(vararg profileLocal: ProfileLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfiles(vararg crossRef: UserProfilesCrossRef)

    @Query("SELECT EXISTS(SELECT * FROM UserLocal WHERE userId = :userId)")
    suspend fun isUserProfilesStored(userId: String): Boolean

    @Query("SELECT * FROM ProfileLocal WHERE profileId = :profileId")
    suspend fun getProfile(profileId: String): ProfileLocal?

    @Transaction
    @Query("SELECT * FROM UserLocal WHERE userId = :userId")
    suspend  fun getUserProfiles(userId: String): UserWithProfiles?

}