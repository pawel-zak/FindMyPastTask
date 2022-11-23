package pawel.zak.findmypasttask.presentation.navigation


const val USER_ID_KEY = "userId"
const val PROFILE_ID_KEY = "profileId"

sealed class Screen(val route: String) {

    object ChooseUserScreen : Screen("choose_user_screen")

    object ProfileListScreen : Screen("profile_list_screen/{$USER_ID_KEY}") {
        fun createRoute(userId: String) =
            this.route.replace(oldValue = "{$USER_ID_KEY}", newValue = userId)
    }

    object ProfileScreen : Screen("profile_screen/{$USER_ID_KEY}/{$PROFILE_ID_KEY}") {
        fun createRoute(userId: String, profileId: String) =
            this.route.replace(
                oldValue = "{$USER_ID_KEY}/{$PROFILE_ID_KEY}",
                newValue = "$userId/$profileId"
            )
    }
}