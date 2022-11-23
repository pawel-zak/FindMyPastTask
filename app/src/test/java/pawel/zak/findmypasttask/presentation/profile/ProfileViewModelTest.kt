@file:OptIn(ExperimentalCoroutinesApi::class)

package pawel.zak.findmypasttask.presentation.profile

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pawel.zak.findmypasttask.common.Resource
import pawel.zak.findmypasttask.common.UiText
import pawel.zak.findmypasttask.domain.model.Profile
import pawel.zak.findmypasttask.domain.model.ProfileRelationships
import pawel.zak.findmypasttask.domain.model.Relationships
import pawel.zak.findmypasttask.domain.use_case.profile.get_profile.GetProfileUseCase
import pawel.zak.findmypasttask.domain.use_case.profile.get_profile_relationships.GetProfileRelationshipsUseCase
import pawel.zak.findmypasttask.presentation.navigation.PROFILE_ID_KEY
import pawel.zak.findmypasttask.presentation.navigation.USER_ID_KEY

class ProfileViewModelTest {

    private lateinit var dispatcher: CoroutineDispatcher
    private lateinit var getProfileUseCaseMock: GetProfileUseCase
    private lateinit var getProfileRelationshipsUseCaseMock: GetProfileRelationshipsUseCase
    private lateinit var savedStateHandle: SavedStateHandle

    private val fakeProfile = Profile(
        id = "001",
        dateOfBirth = "08-10-1943",
        firstname = "Clark",
        surname = "Griswold, Jr",
        image = "https://m.media-amazon.com/images/M/MV5BMTMwNTY2ODA4OV5BMl5BanBnXkFtZTcwOTE1NjAxMw@@._V1_UY317_CR15,0,214,317_AL_.jpg",
        relationships = Relationships("001", "002", "003")
    )

    @BeforeEach
    fun setup() {
        dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)
        savedStateHandle = SavedStateHandle()
        getProfileUseCaseMock = mockk()
        getProfileRelationshipsUseCaseMock = mockk()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `on initialization, initial state`() = runTest {
        val savedStateHandle = SavedStateHandle()

        val viewModel = createViewModel(
            getProfileUseCaseMock,
            getProfileRelationshipsUseCaseMock,
            dispatcher = StandardTestDispatcher(),
            savedStateHandle = savedStateHandle
        )

        viewModel.state.test {
            val initialState = awaitItem()
            Truth.assertThat(initialState.profile).isEqualTo(null)
            Truth.assertThat(initialState.profileRelationships).isEqualTo(null)
            Truth.assertThat(initialState.isLoading).isEqualTo(false)
            Truth.assertThat(initialState.error).isEqualTo(null)
        }
    }


    @Test
    fun `getProfile returns success, state contains profiles and relatives`() = runTest {
        val userId = "userId"
        val profileId = "profileId"
        savedStateHandle[USER_ID_KEY] = userId
        savedStateHandle[PROFILE_ID_KEY] = profileId

        val profileRelationships = ProfileRelationships(
            fakeProfile, fakeProfile, fakeProfile,
            listOf(fakeProfile)
        )

        val viewModel = createViewModel(
            getProfileUseCaseMock,
            getProfileRelationshipsUseCaseMock,
            dispatcher = StandardTestDispatcher(),
            savedStateHandle = savedStateHandle
        )

        coEvery { getProfileUseCaseMock(userId, profileId) } returns flowOf(
            Resource.Success(
                fakeProfile
            )
        )
        coEvery {
            getProfileRelationshipsUseCaseMock(
                any(), any(), any()
            )
        } returns Resource.Success(profileRelationships)


        viewModel.state.test {
            awaitItem()
            val firstEmission = awaitItem()
            Truth.assertThat(firstEmission.profile).isEqualTo(fakeProfile)
            val secondEmission = awaitItem()
            Truth.assertThat(secondEmission.profileRelationships).isNotNull()
        }
    }


    @Test
    fun `getProfile returns error, state contains errorMessage`() = runTest {
        val userId = "userId"
        val profileId = "profileId"
        val testErrorMessage = UiText.DynamicString("test error")
        savedStateHandle[USER_ID_KEY] = userId
        savedStateHandle[PROFILE_ID_KEY] = profileId

        val viewModel = createViewModel(
            getProfileUseCaseMock,
            getProfileRelationshipsUseCaseMock,
            dispatcher = StandardTestDispatcher(),
            savedStateHandle = savedStateHandle
        )

        coEvery { getProfileUseCaseMock(userId, profileId) } returns flowOf(
            Resource.Error(testErrorMessage)
        )

        viewModel.state.test {
            awaitItem()
            val result = awaitItem().error
            Truth.assertThat(result).isEqualTo(testErrorMessage)
        }
    }
}


fun createViewModel(
    getProfileUseCase: GetProfileUseCase,
    getProfileRelationshipsUseCase: GetProfileRelationshipsUseCase,
    dispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
) = ProfileViewModel(
    getProfileUseCase,
    getProfileRelationshipsUseCase,
    dispatcher,
    savedStateHandle
)
