package lamarque.loic.catest.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lamarque.loic.catest.core.Bank
import lamarque.loic.catest.core.BankAccount
import lamarque.loic.catest.infrastructure.getBanksFromServer

class AccountDetailsViewModel : ViewModel() {
    var account: BankAccount? by mutableStateOf(null)
        private set

    fun fetchBankAccount(id: String) {
        viewModelScope.launch {
            delay(2000)
            account = getBanksFromServer()
                .asSequence()
                .flatMap(Bank::accounts)
                .first { it.id == id }
        }
    }
}
