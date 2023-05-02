package hsmida.exam.fdj.compose

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import hsmida.exam.fdj.compose.leagues.LeagueListScreen
import hsmida.exam.fdj.compose.teams.TeamListScreen

enum class Screen {
    GetTeams,
    SearchLeague
}

@Composable
fun AppNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = "${Screen.GetTeams.name}?leagueName={leagueName}",
    ) {
        composable(
            route = "${Screen.GetTeams.name}?leagueName={leagueName}",
            arguments = listOf(
                navArgument("leagueName") {
                    type = NavType.StringType
                    defaultValue = ""
                },
            )
        ) {
            TeamListScreen(
                onNavigateToLeagues = {
                    navController.navigate(
                        route = Screen.SearchLeague.name + "?query={query}"
                    )
                }
            )
        }
        composable(
            route = "${Screen.SearchLeague.name}?query={query}",
            arguments = listOf(
                navArgument("query") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) {
            LeagueListScreen(
                onNavigateToTeams = {
                    navController.popBackStack(
                        route = "${Screen.GetTeams.name}?leagueName={leagueName}",
                        inclusive = true
                    )
                    navController.navigate(
                        route = Screen.GetTeams.name + "?leagueName=${it?.strLeague}"
                    )
                },
                onNavigateUp = {
                    navController.popBackStack(
                        route = "${Screen.GetTeams.name}?leagueName={leagueName}",
                        inclusive = false
                    )
                }
            )
        }
    }
}