package lamarque.loic.catest.old.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BanksDto(val banks: List<BankDto>)

@Serializable
data class BankDto(
    val name: String,
    val isCA: Byte,
    val accounts: List<AccountDto>
)

@Serializable
data class AccountDto(
    val order: Byte,
    val id: String,
    val holder: String,
    val role: Byte,
    @SerialName("contract_number")
    val contractNumber: String,
    val label: String,
    @SerialName("product_code")
    val productCode: String,
    val balance: Double,
    val operations: List<OperationDto>
)

@Serializable
data class OperationDto(
    val id: String,
    val title: String,
    val amount: String,
    val category: String,
    val date: String
)
