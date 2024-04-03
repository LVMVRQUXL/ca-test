package lamarque.loic.catest.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import lamarque.loic.catest.core.Bank
import lamarque.loic.catest.core.CreditAgricole
import lamarque.loic.catest.core.NonCreditAgricoleBank
import lamarque.loic.catest.core.sortedByNameAlphabetically
import lamarque.loic.catest.core.sortedByRegionAlphabetically
import lamarque.loic.catest.core.sortedByTitleAlphabetically

class AccountsViewModel(
    initialCreditAgricoleBanks: List<CreditAgricole> = emptyList(),
    nonCreditAgricoleBanks: List<NonCreditAgricoleBank> = emptyList()
) : ViewModel() {
    var creditAgricoleBanks: List<CreditAgricole> by mutableStateOf(
        initialCreditAgricoleBanks.sortedByRegionAlphabetically().map {
            it.copy(accounts = it.accounts.sortedByTitleAlphabetically())
        }
    )
        private set

    var otherBanks: List<NonCreditAgricoleBank> by mutableStateOf(
        nonCreditAgricoleBanks.sortedByNameAlphabetically().map {
            it.copy(accounts = it.accounts.sortedByTitleAlphabetically())
        }
    )
        private set

    var expandedBanks: List<Bank> by mutableStateOf(emptyList())
        private set

    fun onBankClicked(bank: Bank) {
        expandedBanks =
            if (bank in expandedBanks) expandedBanks.filter { it != bank }
            else expandedBanks + bank
    }
}
