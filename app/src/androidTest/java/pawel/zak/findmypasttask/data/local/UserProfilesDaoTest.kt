@file:OptIn(ExperimentalCoroutinesApi::class)

package pawel.zak.findmypasttask.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pawel.zak.findmypasttask.data.local.model.ProfileLocal
import pawel.zak.findmypasttask.data.local.model.UserLocal
import pawel.zak.findmypasttask.data.local.model.toProfile
import pawel.zak.findmypasttask.data.local.model.toUserProfilesCrossRef

@RunWith(AndroidJUnit4::class)
@SmallTest
class UserProfilesDaoTest {

    private lateinit var database: UserProfilesDatabase
    private lateinit var dao: UserProfilesDao

    private val userLocal = UserLocal(
        userId = "cgriswold"
    )

    private val profileLocal = ProfileLocal(
        profileId = "001",
        dob = "08-10-1943",
        firstname = "Clark",
        surname = "Griswold, Jr",
        image = "https://m.media-amazon.com/images/M/MV5BMTMwNTY2ODA4OV5BMl5BanBnXkFtZTcwOTE1NjAxMw@@._V1_UY317_CR15,0,214,317_AL_.jpg"
    )

    private val listOfProfiles = listOf(
        ProfileLocal(
            profileId = "001",
            dob = "08-10-1943",
            firstname = "Clark",
            surname = "Griswold, Jr",
            image = "https://m.media-amazon.com/images/M/MV5BMTMwNTY2ODA4OV5BMl5BanBnXkFtZTcwOTE1NjAxMw@@._V1_UY317_CR15,0,214,317_AL_.jpg"
        ), ProfileLocal(
            profileId = "002",
            dob = "08-10-1943",
            firstname = "Clark",
            surname = "Griswold, Jr",
            image = "https://m.media-amazon.com/images/M/MV5BMTMwNTY2ODA4OV5BMl5BanBnXkFtZTcwOTE1NjAxMw@@._V1_UY317_CR15,0,214,317_AL_.jpg"
        )
    )

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UserProfilesDatabase::class.java
        ).allowMainThreadQueries().build()


        dao = database.userProfilesDao
    }

    @After
    fun teardown() {
        database.close()
    }


    @Test
    fun insert_profile_to_database_and_get_it__same_profile() = runTest {

        dao.insertProfile(profileLocal)

        val profileResult = dao.getProfile(profileId = profileLocal.profileId)

        assertThat(profileResult).isEqualTo(profileLocal)
    }

    @Test
    fun insert_user_profiles_to_database_and_get_them__same_profiles() = runTest {

        dao.insertUser(userLocal)
        dao.insertProfile(*listOfProfiles.toTypedArray())
        dao.insertUserProfiles(
            listOfProfiles[0].toProfile().toUserProfilesCrossRef(userId = userLocal.userId),
            listOfProfiles[1].toProfile().toUserProfilesCrossRef(userId = userLocal.userId)
        )

        val userLocalResult = dao.getUserProfiles(userId = userLocal.userId)

        assertThat(userLocalResult?.profiles).isEqualTo(listOfProfiles)
    }


    @Test
    fun is_user_profiles_stored_in_database__stored() = runTest {

        dao.insertUser(userLocal)
        dao.insertProfile(*listOfProfiles.toTypedArray())
        dao.insertUserProfiles(
            listOfProfiles[0].toProfile().toUserProfilesCrossRef(userId = userLocal.userId),
            listOfProfiles[1].toProfile().toUserProfilesCrossRef(userId = userLocal.userId)
        )

        val result = dao.isUserProfilesStored(userId = userLocal.userId)

        assertThat(result).isTrue()
    }

    @Test
    fun is_user_profiles_stored_in_database__not_stored() = runTest {

        val result = dao.isUserProfilesStored(userId = userLocal.userId)

        assertThat(result).isFalse()
    }
}