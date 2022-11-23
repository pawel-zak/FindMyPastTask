package pawel.zak.findmypasttask.presentation.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pawel.zak.findmypasttask.common.Constants
import pawel.zak.findmypasttask.common.Resource
import pawel.zak.findmypasttask.common.UiText
import pawel.zak.findmypasttask.di.IoDispatcher
import pawel.zak.findmypasttask.domain.model.Profile
import pawel.zak.findmypasttask.domain.model.ProfileRelationships
import pawel.zak.findmypasttask.domain.use_case.profile.get_profile.GetProfileUseCase
import pawel.zak.findmypasttask.domain.use_case.profile.get_profile_relationships.GetProfileRelationshipsUseCase
import pawel.zak.findmypasttask.presentation.navigation.PROFILE_ID_KEY
import pawel.zak.findmypasttask.presentation.navigation.USER_ID_KEY
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val getProfileRelationshipsUseCase: GetProfileRelationshipsUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val profile = savedStateHandle.getStateFlow<Profile?>(profileKey, null)
    private val profileRelationships =
        savedStateHandle.getStateFlow<ProfileRelationships?>(profileRelationshipsKey, null)
    private val error = savedStateHandle.getStateFlow<UiText?>(errorKey, null)
    private val isLoading = MutableStateFlow(false)

    val state = combine(
        profile,
        profileRelationships,
        error,
        isLoading
    ) { profile, profileRelationships, error, isLoading ->
        ProfileState(
            profile = profile,
            profileRelationships = profileRelationships,
            error = error,
            isLoading = isLoading
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(Constants.TIMEOUT_SHARING),
        ProfileState()
    )

    init {
        savedStateHandle.get<String>(USER_ID_KEY)?.let { userId ->
            savedStateHandle.get<String>(PROFILE_ID_KEY)?.let { profileId ->
                getProfile(userId, profileId)
            }
        }
    }

    private fun getProfile(userId: String, profileId: String) {
        viewModelScope.launch {
            withContext(ioDispatcher) {
                getProfileUseCase(userId, profileId).onEach { result ->
                    when (result) {
                        is Resource.Success -> {

                            savedStateHandle[profileKey] = result.data

                            result.data?.relationships?.let {
                                savedStateHandle[profileRelationshipsKey] =
                                    getProfileRelationshipsUseCase(userId, it, ioDispatcher).data
                            }
                        }
                        is Resource.Error -> {
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
        const val profileKey = "profile"
        const val profileRelationshipsKey = "profileRelationships"
        const val errorKey = "error"
    }
}