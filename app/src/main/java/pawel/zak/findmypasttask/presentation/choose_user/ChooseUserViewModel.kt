package pawel.zak.findmypasttask.presentation.choose_user

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pawel.zak.findmypasttask.common.Resource
import pawel.zak.findmypasttask.common.UiText
import pawel.zak.findmypasttask.di.IoDispatcher
import pawel.zak.findmypasttask.domain.use_case.user.validate_user.ValidateUserUseCase
import javax.inject.Inject

@HiltViewModel
class ChooseUserViewModel @Inject constructor(
    private val validateUserUseCase: ValidateUserUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId = savedStateHandle.getStateFlow(userIdKey, "")
    private val userIdError = savedStateHandle.getStateFlow<UiText?>(userIdErrorKey, null)
    private val isLoading = MutableStateFlow(false)

    val state = combine(userId, userIdError, isLoading) { userId, userIdError, isLoading ->
        ChooseUserState(userId = userId, userIdError = userIdError, isLoading = isLoading)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(timeoutSharing), ChooseUserState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()


    fun onEvent(event: ChooseUserEvent) {
        when (event) {
            is ChooseUserEvent.UserIdChanged -> {
                savedStateHandle[userIdKey] = event.userId
                savedStateHandle[userIdErrorKey] = null
            }
            is ChooseUserEvent.Submit -> {
                validateUserId(event.userId)
            }
        }
    }

    private fun validateUserId(userId: String) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                validateUserUseCase(userId).onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            isLoading.value = false
                            validationEventChannel.send(ValidationEvent.Success)
                        }
                        is Resource.Error -> {
                            isLoading.value = false
                            savedStateHandle[userIdErrorKey] = result.message
                        }
                        is Resource.Loading -> {
                            isLoading.value = true
                        }
                    }
                }.collect()
            }
        }
    }

    companion object {
        const val userIdKey = "userId"
        const val userIdErrorKey = "userIdError"
        const val timeoutSharing = 5000L
    }
}