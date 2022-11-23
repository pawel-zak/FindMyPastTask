package pawel.zak.findmypasttask.data.remote

import com.google.gson.Gson
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pawel.zak.findmypasttask.data.remote.model.ProfileDto
import pawel.zak.findmypasttask.data.remote.model.UserProfilesDto
import pawel.zak.findmypasttask.networking.getJson

class GsonMappingTest {

    private lateinit var gson: Gson

    @BeforeEach
    fun setup() {
        gson = Gson()
    }

    @Test
    fun `map profile response json to ProfileDto, no errors`() {
        val data = getJson("profileResponse.json")
        val expectedSizeOfChildrenList = 2

        val systemUnderTest = gson.fromJson(data, ProfileDto::class.java)

        assertTrue(systemUnderTest.success)
        assertEquals(
            expectedSizeOfChildrenList,
            systemUnderTest.profile.relationships?.children?.size
        )
    }

    @Test
    fun `map user profiles response json, with no errors`() {
        val data = getJson("userProfilesResponse.json")
        val expectedSizeOfProfilesList = 16

        val systemUnderTest = gson.fromJson(data, UserProfilesDto::class.java)

        assertTrue(systemUnderTest.success)
        assertEquals(expectedSizeOfProfilesList, systemUnderTest.profiles.size)
        assertNull(
            systemUnderTest.profiles[0].relationships
        )
    }
}