package lamarque.loic.catest.data.local

import java.util.Date

data class BankModel(
    val name: String,
    val isCA: Boolean,
    val accounts: List<AccountModel>
)

data class AccountModel(
    val id: String,
    val holder: String,
    val label: String,
    val balance: Double,
    val operations: List<OperationModel>
)

data class OperationModel(
    val title: String,
    val amount: Double,
    val date: Date
)
