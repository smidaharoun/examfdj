package hsmida.exam.fdj.compose.teams

import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hsmida.exam.fdj.R
import hsmida.exam.fdj.data.TeamRepository
import hsmida.exam.fdj.model.ApiResult
import hsmida.exam.fdj.model.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class SearchTeamsUIState(
    var title: String? = null,
    var isLoading: Boolean = false,
    var teams: List<Team> = emptyList(),
    var isEmpty: Boolean = true,
    @StringRes var emptyMessage: Int = R.string.selectLeague,
    var isError: Boolean = false,
    @StringRes var errorMessage: Int = R.string.errorMessage,
)

@HiltViewModel
class TeamListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: TeamRepository
) : ViewModel() {

    private var leagueName = savedStateHandle.get<String>(LEAGUE_NAME_SAVED_STATE_KEY) ?: ""

    private val _uiState = MutableStateFlow(SearchTeamsUIState(title = leagueName))
    val uiState: StateFlow<SearchTeamsUIState> = _uiState.asStateFlow()

    init {
        if (leagueName.isNotBlank()) {
            reload()
        }
    }

    fun reload() {
        searchAllTeams(leagueName)
    }

    @VisibleForTesting
    fun searchAllTeams(leagueName: String) {
        viewModelScope.launch {
            repository.searchAllTeams(leagueName).collect { result ->
                when (result) {
                    is ApiResult.Loading -> _uiState.update { currentState ->
                        currentState.copy(isLoading = true)
                    }

                    is ApiResult.Success -> when {
                        result.data.isNullOrEmpty() -> _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                isEmpty = true,
                                emptyMessage = R.string.emptyTeamsMessage
                            )
                        }

                        else -> {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    isLoading = false,
                                    isEmpty = false,
                                    teams = result.data
                                        .slice(0 until result.data.size step 2)
                                        .sortedByDescending { it.strTeam }
                                )
                            }
                        }
                    }

                    is ApiResult.Error -> _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            isEmpty = false,
                            isError = true,
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val LEAGUE_NAME_SAVED_STATE_KEY = "leagueName"
    }
}
