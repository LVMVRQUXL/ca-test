package lamarque.loic.catest.core

import kotlin.test.Test
import kotlin.test.assertEquals

class BankAccountTest {
    @Test
    fun `balance in euros should pass`() {
        val actual: String = BankAccount(
            title = "Compte joint",
            balance = 5.155
        ).balanceInEuros
        val expected = "5,16 €"
        assertEquals(expected, actual)
    }

    @Test
    fun `total balance in euros should pass`() {
        val firstAccount = BankAccount(
            title = "Compte joint",
            balance = 5.155
        )
        val secondAccount = BankAccount(
            title = "Compte Mozaïc",
            balance = 3.10
        )
        val actual: String = listOf(firstAccount, secondAccount)
            .totalBalanceInEuros
        val expected = "8,26 €"
        assertEquals(expected, actual)
    }

    @Test
    fun `sorting a list of BankAccount by title alphabetically should pass`() {
        val firstAccount = BankAccount(
            title = "Compte Mozaïc",
            balance = 5.155
        )
        val secondAccount = BankAccount(
            title = "Compte joint",
            balance = 3.10
        )
        val actual: List<BankAccount> = listOf(firstAccount, secondAccount)
            .sortedByTitleAlphabetically()
        val expected: List<BankAccount> = listOf(secondAccount, firstAccount)
        assertEquals(expected, actual)
    }
}
