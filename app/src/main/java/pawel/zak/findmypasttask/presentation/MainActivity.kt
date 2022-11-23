package pawel.zak.findmypasttask.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import pawel.zak.findmypasttask.presentation.navigation.SetUpGraph
import pawel.zak.findmypasttask.ui.theme.FindMyPastTaskTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FindMyPastTaskTheme {

                navController = rememberNavController()

                setContent {
                    Surface(color = MaterialTheme.colors.background) {

                        SetUpGraph(navController)

                    }
                }
            }
        }
    }
}