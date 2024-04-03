package lamarque.loic.catest.infrastructure

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import lamarque.loic.catest.core.Bank
import lamarque.loic.catest.core.BankAccount
import lamarque.loic.catest.core.CreditAgricole
import lamarque.loic.catest.core.NonCreditAgricoleBank

/** Returns a list of banks retrieved from the server. */
public suspend fun getBanksFromServer(): List<Bank> =
    withContext(Dispatchers.IO) {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                val format = Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                }
                json(format)
            }
        }
        val response: HttpResponse = client.get(
            "https://cdf-test-mobile-default-rtdb.europe-west1.firebasedatabase.app/banks.json"
        )
        response.body<List<BankDto>>()
            .map(BankDto::toBank)
            .also { client.close() }
    }

@Serializable
internal data class BankDto(
    val accounts: List<AccountDto>,
    val isCA: Int,
    val name: String
)

private fun BankDto.toBank(): Bank = if (isCA == 1) CreditAgricole(
    region = name.substringAfter("CA "),
    accounts = accounts.map(AccountDto::toBankAccount)
) else NonCreditAgricoleBank(name, accounts.map(AccountDto::toBankAccount))

@Serializable
internal data class AccountDto(val balance: Double, val label: String)

private fun AccountDto.toBankAccount(): BankAccount =
    BankAccount(title = label, balance)
