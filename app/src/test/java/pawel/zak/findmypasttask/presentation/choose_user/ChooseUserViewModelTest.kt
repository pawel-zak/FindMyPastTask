@file:OptIn(ExperimentalCoroutinesApi::class)

package pawel.zak.findmypasttask.presentation.choose_user

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
import pawel.zak.findmypasttask.domain.use_case.user.validate_user.UserValidationError
import pawel.zak.findmypasttask.domain.use_case.user.validate_user.UserValidationResult
import pawel.zak.findmypasttask.domain.use_case.user.validate_user.ValidateUserUseCase

class ChooseUserViewModelTest {

    private lateinit var dispatcher: CoroutineDispatcher
    private lateinit var viewModel: ChooseUserViewModel
    private lateinit var validateUserUseCaseMock: ValidateUserUseCase
    private lateinit var savedStateHandle: SavedStateHandle

    @BeforeEach
    fun setup() {
        dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)
        savedStateHandle = SavedStateHandle()
        validateUserUseCaseMock = mockk()
        viewModel = createViewModel(
            validateUserUseCaseMock,
            dispatcher,
            savedStateHandle = savedStateHandle
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `on initialization, initial state`() = runTest {
        viewModel.state.test {
            val initialState = awaitItem()
            Truth.assertThat(initialState.userId).isEqualTo("")
            Truth.assertThat(initialState.isLoading).isEqualTo(false)
            Truth.assertThat(initialState.userIdError).isEqualTo(null)
        }
    }

    @Test
    fun `submit userId with success, validation success event`() = runTest {
        val userId = "userId"

        coEvery { validateUserUseCaseMock(userId) } returns flowOf(
            Resource.Success(
                UserValidationResult(true)
            )
        )

        viewModel.onEvent(ChooseUserEvent.Submit(userId))

        viewModel.validationEvents.test {
            val result = awaitItem()
            Truth.assertThat(result).isInstanceOf(ValidationEvent.Success::class.java)
        }
    }


    @Test
    fun `submit userId invoke error, FetchUserError errorMessage in userIdError`() = runTest {

        val userId = "userId"

        coEvery { validateUserUseCaseMock(userId) } returns flowOf(
            Resource.Error(
                UserValidationError.FetchUserError.errorMessage,
                UserValidationResult(false, UserValidationError.FetchUserError)
            )
        )

        viewModel.onEvent(ChooseUserEvent.Submit(userId))

        viewModel.state.test {
            awaitItem()
            val secondEmission = awaitItem()
            Truth.assertThat(secondEmission.userIdError)
                .isEqualTo(UserValidationError.FetchUserError.errorMessage)
        }
    }
}


fun createViewModel(
    validateUserUseCase: ValidateUserUseCase,
    dispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
) = ChooseUserViewModel(validateUserUseCase, dispatcher, savedStateHandle)
