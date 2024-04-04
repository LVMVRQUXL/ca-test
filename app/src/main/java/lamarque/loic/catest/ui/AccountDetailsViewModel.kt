package lamarque.loic.catest.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import lamarque.loic.catest.core.Bank
import lamarque.loic.catest.core.BankAccount
import lamarque.loic.catest.infrastructure.getBanksFromServer

class AccountDetailsViewModel : ViewModel() {
    private var _uiState: MutableStateFlow<UIState> =
        MutableStateFlow(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState

    fun fetchBankAccount(id: String) {
        _uiState.value = UIState.Loading
        val handler = CoroutineExceptionHandler { _, _ ->
            _uiState.value = UIState.Error
        }
        viewModelScope.launch(handler) {
            delay(2000)
            _uiState.value = getBanksFromServer()
                .asSequence()
                .flatMap(Bank::accounts)
                .first { it.id == id }
                .let { UIState.Success(it) }
        }
    }
}

data class AccountDetailsState(val bankAccount: BankAccount)
