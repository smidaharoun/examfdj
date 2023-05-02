package hsmida.exam.fdj.compose.leagues

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hsmida.exam.fdj.R
import hsmida.exam.fdj.data.LeagueRepository
import hsmida.exam.fdj.model.ApiResult
import hsmida.exam.fdj.model.League
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchLeagueUiState(
    var isLoading: Boolean = true,
    var leagues: List<League> = emptyList(),
    var isEmpty: Boolean = false,
    @StringRes var emptyMessage: Int = R.string.emptyLeaguesMessage,
    var isError: Boolean = false,
    @StringRes var errorMessage: Int = R.string.errorMessage,
    var query: String = "",
)

@HiltViewModel
class SearchLeagueViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: LeagueRepository
) : ViewModel() {

    private var query = checkNotNull(savedStateHandle.get<String>(QUERY_SAVED_STATE_KEY))

    private val _uiState = MutableStateFlow(SearchLeagueUiState())
    val uiState: StateFlow<SearchLeagueUiState> = _uiState.asStateFlow()

    private var items: List<League> = emptyList()

    init {
        getAllLeagues()
    }

    fun getAllLeagues() {
        viewModelScope.launch {
            repository.getAllLeagues().collect { result ->
                when (result) {
                    is ApiResult.Loading -> _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = true
                        )
                    }

                    is ApiResult.Success -> {
                        items = result.data ?: emptyList()
                        filterItems(query)
                    }

                    is ApiResult.Error -> _uiState.update { currentState ->
                        currentState.copy(
                            isError = true,
                        )
                    }
                }
            }
        }
    }

    fun filterItems(query: String) {
        val filteredItems = when {
            query.isNotBlank() -> items.filter {
                it.strLeague.lowercase().contains(query.lowercase()) ||
                        it.strLeagueAlternate?.lowercase()?.contains(query.lowercase()) == true
            }.sortedBy { it.idLeague }
            else -> items
        }
        _uiState.update { currentState ->
            currentState.copy(
                query = query,
                isLoading = false,
                isEmpty = filteredItems.isEmpty(),
                leagues = filteredItems
            )
        }
    }

    companion object {
        private const val QUERY_SAVED_STATE_KEY = "query"
    }
}