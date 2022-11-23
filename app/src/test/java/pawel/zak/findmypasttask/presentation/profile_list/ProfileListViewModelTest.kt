@file:OptIn(ExperimentalCoroutinesApi::class)

package pawel.zak.findmypasttask.presentation.profile_list

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pawel.zak.findmypasttask.common.Resource
import pawel.zak.findmypasttask.common.UiText
import pawel.zak.findmypasttask.domain.model.Profile
import pawel.zak.findmypasttask.domain.use_case.profile.get_profiles.GetProfilesUseCase
import pawel.zak.findmypasttask.presentation.navigation.USER_ID_KEY

class ProfileListViewModelTest {

    private lateinit var dispatcher: CoroutineDispatcher
    private lateinit var getProfilesUseCaseMock: GetProfilesUseCase
    private lateinit var savedStateHandle: SavedStateHandle

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

    @BeforeEach
    fun setup() {
        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)
        savedStateHandle = SavedStateHandle()
        getProfilesUseCaseMock = mockk()

    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `on initialization, initial state`() = runTest {
        val savedStateHandle = SavedStateHandle()
        val getProfilesUseCaseMock = mockk<GetProfilesUseCase>()
        val viewModel = createViewModel(
            getProfilesUseCase = getProfilesUseCaseMock,
            dispatcher = StandardTestDispatcher(),
            savedStateHandle = savedStateHandle
        )

        viewModel.state.test {
            val initialState = awaitItem()
            Truth.assertThat(initialState.profiles.isEmpty()).isTrue()
            Truth.assertThat(initialState.isLoading).isEqualTo(false)
            Truth.assertThat(initialState.error).isEqualTo(null)
        }
    }

    @Test
    fun `getProfiles returns success, state contains profiles`() = runTest {
        val userId = "userId"
        savedStateHandle[USER_ID_KEY] = userId
        val viewModel = createViewModel(
            getProfilesUseCaseMock,
            dispatcher,
            savedStateHandle
        )

        coEvery { getProfilesUseCaseMock(userId) } returns flowOf(
            Resource.Success(
                listOfProfile
            )
        )

        viewModel.state.test {
            awaitItem()
            val result = awaitItem().profiles
            Truth.assertThat(result).isEqualTo(listOfProfile)
        }
    }


    @Test
    fun `getProfiles returns error, state contains errorMessage`() = runTest {

        val userId = "userId"
        val testErrorMessage = UiText.DynamicString("test error")
        savedStateHandle[USER_ID_KEY] = userId
        val viewModel = createViewModel(
            getProfilesUseCaseMock,
            dispatcher,
            savedStateHandle
        )

        coEvery { getProfilesUseCaseMock(userId) } returns flowOf(
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
    getProfilesUseCase: GetProfilesUseCase,
    dispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
) = ProfileListViewModel(getProfilesUseCase, dispatcher, savedStateHandle)
