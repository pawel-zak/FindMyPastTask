package pawel.zak.findmypasttask.presentation.profile_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pawel.zak.findmypasttask.R
import pawel.zak.findmypasttask.common.Constants.TIMEOUT_SHARING
import pawel.zak.findmypasttask.common.Resource
import pawel.zak.findmypasttask.common.UiText
import pawel.zak.findmypasttask.di.IoDispatcher
import pawel.zak.findmypasttask.domain.model.Profile
import pawel.zak.findmypasttask.domain.use_case.profile.get_profiles.GetProfilesUseCase
import pawel.zak.findmypasttask.presentation.navigation.USER_ID_KEY
import javax.inject.Inject

@HiltViewModel
class ProfileListViewModel @Inject constructor(
    private val getProfilesUseCase: GetProfilesUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val profileList = savedStateHandle.getStateFlow(profileListKey, listOf<Profile>())
    private val error = savedStateHandle.getStateFlow<UiText?>(errorKey, null)
    private val isLoading = MutableStateFlow(false)
    var userId: String = ""
        private set

    val state = combine(profileList, error, isLoading) { profileList, error, isLoading ->
        ProfileListState(profiles = profileList, error = error, isLoading = isLoading)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(TIMEOUT_SHARING),
        ProfileListState()
    )

    init {
        savedStateHandle.get<String>(USER_ID_KEY)?.let { userId ->
            this.userId = userId
            getProfiles(userId)
        }
    }

    private fun getProfiles(userId: String) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                getProfilesUseCase(userId)
                    .onEach { result ->
                        when (result) {
                            is Resource.Success -> {
                                isLoading.value = false
                                val personList = result.data ?: listOf()
                                if (personList.isEmpty())
                                    savedStateHandle[errorKey] =
                                        UiText.StringResource(R.string.empty_list_error)
                                else
                                    savedStateHandle[profileListKey] = personList
                            }
                            is Resource.Error -> {
                                isLoading.value = false
                                savedStateHandle[errorKey] = result.message
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
        const val profileListKey = "profileId"
        const val errorKey = "error"
    }
}