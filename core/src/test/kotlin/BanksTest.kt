package lamarque.loic.catest.core

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertContentEquals

class CreditAgricoleTest {
    @Test
    fun `sorting a list CreditAgricole alphabetically by region should pass`() {
        val banks: List<CreditAgricole> = listOf(
            CreditAgricole(
                region = "Languedoc",
                accounts = listOf(
                    BankAccount(
                        id = "1644784369",
                        title = "Compte joint",
                        balance = Random.nextDouble(),
                        operations = emptyList()
                    )
                )
            ),
            CreditAgricole(
                region = "Centre-Est",
                accounts = listOf(
                    BankAccount(
                        id = "1644784369",
                        title = "Compte de dépôt",
                        balance = Random.nextDouble(),
                        operations = emptyList()
                    )
                )
            )
        )
        val actual: List<CreditAgricole> = banks.sortedByRegionAlphabetically()
        val expected: List<CreditAgricole> = banks.sortedBy {
            it.region.lowercase()
        }
        assertContentEquals(expected, actual)
    }
}

class NonCreditAgricoleTest {
    @Test
    fun `sorting a list NonCreditAgricoleBank by name alphabetically should pass`() {
        val banks: List<NonCreditAgricoleBank> = listOf(
            NonCreditAgricoleBank(
                name = "Boursorama",
                accounts = listOf(
                    BankAccount(
                        id = "1644784369",
                        title = "Compte de dépôt",
                        balance = Random.nextDouble(),
                        operations = emptyList()
                    )
                )
            ),
            NonCreditAgricoleBank(
                name = "Banque Populaire",
                accounts = listOf(
                    BankAccount(
                        id = "1644784369",
                        title = "Compte joint",
                        balance = Random.nextDouble(),
                        operations = emptyList()
                    )
                )
            )
        )
        val actual: List<NonCreditAgricoleBank> =
            banks.sortedByNameAlphabetically()
        val expected: List<NonCreditAgricoleBank> = banks.sortedBy {
            it.name.lowercase()
        }
        assertContentEquals(expected, actual)
    }
}
