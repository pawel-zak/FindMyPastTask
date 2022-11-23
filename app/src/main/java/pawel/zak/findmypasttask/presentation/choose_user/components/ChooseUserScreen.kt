package pawel.zak.findmypasttask.presentation.choose_user.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import pawel.zak.findmypasttask.R
import pawel.zak.findmypasttask.presentation.choose_user.ChooseUserEvent
import pawel.zak.findmypasttask.presentation.choose_user.ChooseUserViewModel
import pawel.zak.findmypasttask.presentation.choose_user.ValidationEvent

@Composable
fun ChooseUserScreen(
    showUserProfiles: (userId: String) -> Unit,
    viewModel: ChooseUserViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.Success -> {
                    showUserProfiles(state.userId)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = state.userId,
            maxLines = 1,
            onValueChange = {
                viewModel.onEvent(ChooseUserEvent.UserIdChanged(it))
            },
            label = { Text(stringResource(R.string.userIdTextLabel)) },
            isError = state.userIdError != null,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )
        state.userIdError?.let {
            Text(
                text = it.asString(),
                color = MaterialTheme.colors.error,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        GradientButton(
            text = stringResource(R.string.submitButton),
            textColor = MaterialTheme.colors.onPrimary,
            gradient = Brush.horizontalGradient(
                colors = listOf(
                    MaterialTheme.colors.primary,
                    MaterialTheme.colors.secondary
                )
            )
        ) {
            viewModel.onEvent(ChooseUserEvent.Submit(state.userId))
        }
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}