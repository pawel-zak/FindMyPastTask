package pawel.zak.findmypasttask.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import pawel.zak.findmypasttask.presentation.choose_user.components.ChooseUserScreen
import pawel.zak.findmypasttask.presentation.profile_list.components.ProfileListScreen
import pawel.zak.findmypasttask.presentation.profile.components.ProfileScreen

@Composable
fun SetUpGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.ChooseUserScreen.route) {

        composable(
            route = Screen.ChooseUserScreen.route
        ) {
            ChooseUserScreen(showUserProfiles = { userId ->
                navController.navigate(
                    route = Screen.ProfileListScreen.createRoute(
                        userId
                    )
                )
            })
        }
        composable(
            route = Screen.ProfileListScreen.route,
            arguments = listOf(navArgument(USER_ID_KEY) {
                type = NavType.StringType
            })

        ) {
            ProfileListScreen(showProfile = { userId, profileId ->
                navController.navigate(
                    route = Screen.ProfileScreen.createRoute(
                        userId, profileId
                    )
                )
            })
        }
        composable(
            route = Screen.ProfileScreen.route,
            arguments = listOf(navArgument(USER_ID_KEY) {
                type = NavType.StringType
            }, navArgument(PROFILE_ID_KEY) {
                type = NavType.StringType
            })
        ) {
            ProfileScreen()
        }
    }
}