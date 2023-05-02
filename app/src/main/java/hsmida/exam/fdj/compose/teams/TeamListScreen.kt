package hsmida.exam.fdj.compose.teams

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import hsmida.exam.fdj.R
import hsmida.exam.fdj.compose.components.SearchBar
import hsmida.exam.fdj.compose.components.ApiResponseView
import hsmida.exam.fdj.model.Team
import hsmida.exam.fdj.ui.theme.ExamTheme

@Composable
fun TeamListScreen(
    modifier: Modifier = Modifier,
    viewModel: TeamListViewModel = hiltViewModel(),
    onNavigateToLeagues: (String?) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TeamListScreen(
        modifier = modifier.padding(dimensionResource(id = R.dimen.default_screen_margin)),
        uiState = uiState,
        onRetryClicked = { viewModel.reload() },
        onNavigateToLeagues = onNavigateToLeagues
    )
}

@Composable
private fun TeamListScreen(
    modifier: Modifier = Modifier,
    uiState: SearchTeamsUIState = SearchTeamsUIState(),
    onRetryClicked: () -> Unit = {},
    onNavigateToLeagues: (String?) -> Unit = {},
) {
    Scaffold(
        topBar = {
            SearchBar(
                modifier = modifier,
                title = uiState.title ?: stringResource(id = R.string.searchLeagues),
                onSearchBarClicked = {
                    onNavigateToLeagues(uiState.title)
                }
            )
        },
        content = { innerPadding ->
            ApiResponseView(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                isLoading = uiState.isLoading,
                isEmpty = uiState.isEmpty,
                emptyMessage = stringResource(id = uiState.emptyMessage),
                isError = uiState.isError,
                errorMessage = stringResource(id = uiState.errorMessage),
                onRetryClicked = onRetryClicked
            ) {
                LazyVerticalGrid(
                    modifier = modifier.fillMaxSize(),
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.grid_spacing))
                ) {
                    items(
                        items = uiState.teams,
                        key = { it.idTeam }
                    ) { team ->
                        TeamItem(team = team)
                    }
                }
            }
        }
    )
}

@Composable
fun TeamItem(
    team: Team
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(team.strTeamBadge)
            .crossfade(true)
            .build(),
        placeholder = painterResource(R.drawable.placeholder),
        contentDescription = "Team Logo",
        contentScale = ContentScale.Inside,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TeamListPreview() {
    ExamTheme {
        TeamListScreen(
            uiState = SearchTeamsUIState(
                isEmpty = false,
                teams = listOf(
                    Team(idTeam = "", idLeague = "", strTeam = "", strTeamBadge = ""),
                    Team(idTeam = "", idLeague = "", strTeam = "", strTeamBadge = ""),
                    Team(idTeam = "", idLeague = "", strTeam = "", strTeamBadge = ""),
                    Team(idTeam = "", idLeague = "", strTeam = "", strTeamBadge = ""),
                )
            )
        )
    }
}