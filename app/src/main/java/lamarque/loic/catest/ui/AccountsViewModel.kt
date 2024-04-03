package lamarque.loic.catest.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lamarque.loic.catest.core.Bank
import lamarque.loic.catest.core.CreditAgricole
import lamarque.loic.catest.core.NonCreditAgricoleBank
import lamarque.loic.catest.core.sortedByNameAlphabetically
import lamarque.loic.catest.core.sortedByRegionAlphabetically
import lamarque.loic.catest.core.sortedByTitleAlphabetically
import lamarque.loic.catest.infrastructure.getBanksFromServer

class AccountsViewModel(
    initialCreditAgricoleBanks: List<CreditAgricole> = emptyList(),
    nonCreditAgricoleBanks: List<NonCreditAgricoleBank> = emptyList()
) : ViewModel() {
    var creditAgricoleBanks: List<CreditAgricole> by mutableStateOf(
        sortCreditAgricoleBanks(initialCreditAgricoleBanks)
    )
        private set

    var otherBanks: List<NonCreditAgricoleBank> by mutableStateOf(
        sortOtherBanks(nonCreditAgricoleBanks)
    )
        private set

    val areBanksAvailable: Boolean
        get() = creditAgricoleBanks.isNotEmpty() || otherBanks.isNotEmpty()

    private var expandedBanks: List<Bank> by mutableStateOf(emptyList())

    fun isExpanded(bank: Bank): Boolean = bank in expandedBanks

    fun onBankClicked(bank: Bank) {
        expandedBanks =
            if (bank in expandedBanks) expandedBanks.filter { it != bank }
            else expandedBanks + bank
    }

    fun fetchBanks() {
        viewModelScope.launch {
            val banks: List<Bank> = getBanksFromServer()
            delay(2000)
            launch {
                creditAgricoleBanks = banks.filterIsInstance<CreditAgricole>()
                    .let(this@AccountsViewModel::sortCreditAgricoleBanks)
            }
            launch {
                otherBanks = banks.filterIsInstance<NonCreditAgricoleBank>()
                    .let(this@AccountsViewModel::sortOtherBanks)
            }
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
