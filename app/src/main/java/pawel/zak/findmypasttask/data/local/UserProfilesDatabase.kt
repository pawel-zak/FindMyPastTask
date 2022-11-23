package pawel.zak.findmypasttask.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pawel.zak.findmypasttask.data.local.model.ProfileLocal
import pawel.zak.findmypasttask.data.local.model.UserLocal
import pawel.zak.findmypasttask.data.local.model.UserProfilesCrossRef

@Database(
    entities = [
        UserLocal::class,
        ProfileLocal::class,
        UserProfilesCrossRef::class],
    version = 1
)
@TypeConverters(Converter::class)
abstract class UserProfilesDatabase : RoomDatabase() {

    abstract val userProfilesDao: UserProfilesDao

    companion object {
        const val DATABASE_NAME = "profile_db"
    }
}