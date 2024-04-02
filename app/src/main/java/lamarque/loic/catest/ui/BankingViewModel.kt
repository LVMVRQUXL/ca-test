package lamarque.loic.catest.ui

import android.content.res.AssetManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lamarque.loic.catest.data.BankingRepository
import lamarque.loic.catest.data.local.AccountModel
import lamarque.loic.catest.data.local.BankModel

class BankingViewModel(assets: AssetManager) : ViewModel() {
    private val repository: BankingRepository = BankingRepository(assets)

    var banks: List<BankModel> by mutableStateOf(emptyList())
        private set

    var account: AccountModel? by mutableStateOf(null)
        private set

    fun fetchAllBanks() {
        banks = repository.getAllBanks()
    }

    suspend fun fetchAccountDetails(id: String) {
        viewModelScope.launch {
            delay(5000)
            account = repository.getAllBanks()
                .flatMap(BankModel::accounts)
                .first { it.id == id }
        }
    }
}
