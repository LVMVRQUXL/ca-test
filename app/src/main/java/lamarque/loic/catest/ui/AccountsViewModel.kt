package lamarque.loic.catest.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import lamarque.loic.catest.core.Bank
import lamarque.loic.catest.core.CreditAgricole
import lamarque.loic.catest.core.NonCreditAgricoleBank
import lamarque.loic.catest.core.sortedByNameAlphabetically
import lamarque.loic.catest.core.sortedByRegionAlphabetically
import lamarque.loic.catest.core.sortedByTitleAlphabetically
import lamarque.loic.catest.infrastructure.getBanksFromServer

class AccountsViewModel : ViewModel() {
    private var _uiState: MutableStateFlow<UIState> =
        MutableStateFlow(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState

    fun fetchBanks() {
        _uiState.value = UIState.Loading
        val handler = CoroutineExceptionHandler { _, _ ->
            _uiState.value = UIState.Error
        }
        viewModelScope.launch(handler) {
            delay(1000)
            val banks: List<Bank> = getBanksFromServer()
            val creditAgricoleBanks = banks.filterIsInstance<CreditAgricole>()
                .let(this@AccountsViewModel::sortCreditAgricoleBanks)
            val otherBanks = banks.filterIsInstance<NonCreditAgricoleBank>()
                .let(this@AccountsViewModel::sortOtherBanks)
            val banksSummary = BanksSummary(creditAgricoleBanks, otherBanks)
            _uiState.value = UIState.Success(banksSummary)
        }
    }

    private fun sortCreditAgricoleBanks(
        banks: List<CreditAgricole>
    ): List<CreditAgricole> = banks.sortedByRegionAlphabetically().map {
        it.copy(accounts = it.accounts.sortedByTitleAlphabetically())
    }

    private fun sortOtherBanks(banks: List<NonCreditAgricoleBank>):
            List<NonCreditAgricoleBank> = banks.sortedByNameAlphabetically()
        .map {
            it.copy(accounts = it.accounts.sortedByTitleAlphabetically())
        }
}

data class BanksSummary(
    val creditAgricoleBanks: List<CreditAgricole>,
    val otherBanks: List<NonCreditAgricoleBank>
)
