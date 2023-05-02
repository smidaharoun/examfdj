package hsmida.exam.fdj.compose

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun ExamApp() {

    val navController = rememberNavController()

    AppNavHost(navController = navController)
}