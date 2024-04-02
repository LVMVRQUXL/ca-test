package lamarque.loic.catest.old.data

import android.content.res.AssetManager
import kotlinx.serialization.json.Json
import lamarque.loic.catest.old.data.local.AccountModel
import lamarque.loic.catest.old.data.local.BankModel
import lamarque.loic.catest.old.data.local.OperationModel
import lamarque.loic.catest.old.data.remote.AccountDto
import lamarque.loic.catest.old.data.remote.BanksDto
import lamarque.loic.catest.old.data.remote.OperationDto
import java.util.Date

class BankingRepository(private val assets: AssetManager) {
    fun getAllBanks(): List<BankModel> {
        val rawValues: String = assets.open("banks.json").use {
            it.readBytes().decodeToString()
        }
        val banks: BanksDto = Json.decodeFromString<BanksDto>(rawValues)
        return banks.toBankModels()
    }
}

private fun BanksDto.toBankModels(): List<BankModel> = banks.map {
    val accounts: List<AccountModel> = it.accounts.map(AccountDto::toModel)
    BankModel(name = it.name, isCA = it.isCA == 1.toByte(), accounts)
}

private fun AccountDto.toModel(): AccountModel {
    val operations: List<OperationModel> =
        this.operations.map(OperationDto::toModel)
    return AccountModel(
        id,
        holder,
        label,
        balance,
        operations
    )
}

private fun OperationDto.toModel(): OperationModel {
    val amount: Double = amount.replace(',', '.')
        .toDouble()
    return OperationModel(title, amount, Date(this.date.toLong()))
}
