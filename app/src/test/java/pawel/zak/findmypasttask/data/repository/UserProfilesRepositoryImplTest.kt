package pawel.zak.findmypasttask.data.repository

import android.database.sqlite.SQLiteException
import androidx.room.withTransaction
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import pawel.zak.findmypasttask.data.local.UserProfilesDao
import pawel.zak.findmypasttask.data.local.UserProfilesDatabase
import pawel.zak.findmypasttask.data.local.model.ProfileLocal
import pawel.zak.findmypasttask.data.local.model.UserLocal
import pawel.zak.findmypasttask.data.local.model.UserWithProfiles
import pawel.zak.findmypasttask.data.remote.ProfileApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pawel.zak.findmypasttask.common.Resource
import pawel.zak.findmypasttask.data.local.model.RelationshipsLocal
import pawel.zak.findmypasttask.data.remote.model.ProfileDto
import pawel.zak.findmypasttask.data.remote.model.ProfileRemote
import pawel.zak.findmypasttask.data.remote.model.RelationshipsRemote
import pawel.zak.findmypasttask.data.remote.model.UserProfilesDto
import pawel.zak.findmypasttask.domain.model.Profile
import pawel.zak.findmypasttask.domain.model.Relationships
import retrofit2.HttpException
import retrofit2.Response

class UserProfilesRepositoryImplTest {

    private var api = mockk<ProfileApi>()
    private var userProfilesDao = mockk<UserProfilesDao>()
    private var database = mockk<UserProfilesDatabase>()

    private lateinit var systemUnderTest: UserProfilesRepositoryImpl

    private val userLocal = UserLocal(
        userId = "cgriswold"
    )
    private val listOfProfileLocal = listOf(
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

    private val listOfProfileRemote = listOf(
        ProfileRemote(
            id = "001",
            dob = "08101943",
            firstname = "Clark",
            surname = "Griswold, Jr",
            image = "https://m.media-amazon.com/images/M/MV5BMTMwNTY2ODA4OV5BMl5BanBnXkFtZTcwOTE1NjAxMw@@._V1_UY317_CR15,0,214,317_AL_.jpg"
        ), ProfileRemote(
            id = "002",
            dob = "08101943",
            firstname = "Clark",
            surname = "Griswold, Jr",
            image = "https://m.media-amazon.com/images/M/MV5BMTMwNTY2ODA4OV5BMl5BanBnXkFtZTcwOTE1NjAxMw@@._V1_UY317_CR15,0,214,317_AL_.jpg"
        )
    )

    private val listOfProfile = listOf(
        Profile(
            id = "001",
            dateOfBirth = "08-10-1943",
            firstname = "Clark",
            surname = "Griswold, Jr",
            image = "https://m.media-amazon.com/images/M/MV5BMTMwNTY2ODA4OV5BMl5BanBnXkFtZTcwOTE1NjAxMw@@._V1_UY317_CR15,0,214,317_AL_.jpg"
        ), Profile(
            id = "002",
            dateOfBirth = "08-10-1943",
            firstname = "Clark",
            surname = "Griswold, Jr",
            image = "https://m.media-amazon.com/images/M/MV5BMTMwNTY2ODA4OV5BMl5BanBnXkFtZTcwOTE1NjAxMw@@._V1_UY317_CR15,0,214,317_AL_.jpg"
        )
    )

    private val profileRemote = ProfileRemote(
        id = "001",
        dob = "08101943",
        firstname = "Clark",
        surname = "Griswold, Jr",
        image = "https://m.media-amazon.com/images/M/MV5BMTMwNTY2ODA4OV5BMl5BanBnXkFtZTcwOTE1NjAxMw@@._V1_UY317_CR15,0,214,317_AL_.jpg",
        relationships = RelationshipsRemote(
            children = listOf("009", "010"),
            father = "004",
            mother = "003",
            spouse = "002"
        )
    )

    private val profileLocal = ProfileLocal(
        profileId = "001",
        dob = "08-10-1943",
        firstname = "Clark",
        surname = "Griswold, Jr",
        image = "https://m.media-amazon.com/images/M/MV5BMTMwNTY2ODA4OV5BMl5BanBnXkFtZTcwOTE1NjAxMw@@._V1_UY317_CR15,0,214,317_AL_.jpg",
        relationships = RelationshipsLocal(
            children = listOf("009", "010"),
            father = "004",
            mother = "003",
            spouse = "002"
        )
    )

    private val profile = Profile(
        id = "001",
        dateOfBirth = "08-10-1943",
        firstname = "Clark",
        surname = "Griswold, Jr",
        image = "https://m.media-amazon.com/images/M/MV5BMTMwNTY2ODA4OV5BMl5BanBnXkFtZTcwOTE1NjAxMw@@._V1_UY317_CR15,0,214,317_AL_.jpg",
        relationships = Relationships(
            children = listOf("009", "010"),
            father = "004",
            mother = "003",
            spouse = "002"
        )
    )

    @BeforeEach
    fun setup() {
        systemUnderTest = UserProfilesRepositoryImpl(api, userProfilesDao, database)

        MockKAnnotations.init(this)
        mockkStatic(
            "androidx.room.RoomDatabaseKt"
        )
        val transactionLambda = slot<suspend () -> Unit>()
        coEvery { database.withTransaction(capture(transactionLambda)) } coAnswers {}
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get profiles from database, Success resource with list of profile`() = runTest {

        val userWithProfiles = UserWithProfiles(userLocal, listOfProfileLocal)

        coEvery { userProfilesDao.isUserProfilesStored(any()) } returns true
        coEvery { userProfilesDao.getUserProfiles(any()) } returns userWithProfiles

        systemUnderTest.getProfiles(userLocal.userId).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            assertThat(result.data).isEqualTo(listOfProfile)
            awaitComplete()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get profiles from database throws SQLiteException, Error resource`() = runTest {

        coEvery { userProfilesDao.isUserProfilesStored(any()) } returns true
        coEvery { userProfilesDao.getUserProfiles(any()) } throws SQLiteException()

        systemUnderTest.getProfiles(userLocal.userId).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Error::class.java)
            awaitComplete()
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getting profiles from network request, Success resource with list of profile`() = runTest {

        val profilesDto = UserProfilesDto(listOfProfileRemote, true, null)

        coEvery { userProfilesDao.isUserProfilesStored(any()) } returns false
        coEvery { api.getProfiles(any()) } returns profilesDto

        systemUnderTest.getProfiles(userLocal.userId).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            assertThat(result.data).isEqualTo(listOfProfile)
            awaitComplete()
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `get profiles from network request throws IOException, Error resource`() = runTest {

        coEvery { userProfilesDao.isUserProfilesStored(any()) } returns false
        coEvery { api.getProfiles(any()) } throws HttpException(
            Response.error<UserProfilesDto>(401, "".toResponseBody())
        )

        systemUnderTest.getProfiles(userLocal.userId).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Error::class.java)
            awaitComplete()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getting profile from database, Success resource with profile`() = runTest {

        coEvery { userProfilesDao.getProfile(any()) } returns profileLocal

        systemUnderTest.getMainProfile(userLocal.userId, profile.id).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            assertThat(result.data).isEqualTo(profile)
            awaitComplete()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getting profile from network request, Success resource with profile`() = runTest {

        coEvery { userProfilesDao.getProfile(any()) } returns listOfProfileLocal[0]
        coEvery { api.getProfile(any(), any()) } returns ProfileDto(profileRemote, true)

        systemUnderTest.getMainProfile(userLocal.userId, profile.id).test {
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)
            val result = awaitItem()
            assertThat(result).isInstanceOf(Resource.Success::class.java)
            assertThat(result.data).isEqualTo(profile)
            awaitComplete()
        }
    }
}