package lamarque.loic.catest.core

import lamarque.loic.catest.core.internal.simpleNameOf
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class NonCreditAgricoleBankTest {
    @Test
    fun `structural equality should pass with the same instance`() {
        val first: Any = NonCreditAgricoleBank.from(
            name = "Boursorama",
            accounts = setOf(
                BankAccount.from(
                    label = "Compte de dépôt",
                    balance = Random.nextDouble()
                )
            )
        )
        val second: Any = first
        assertEquals(expected = second, actual = first)
        assertEquals(expected = second.hashCode(), actual = first.hashCode())
    }

    @Test
    fun `structural equality should pass with another NonCreditAgricoleBank having the same name`() {
        val name = "Boursorama"
        val first: Any = NonCreditAgricoleBank.from(
            name,
            accounts = setOf(
                BankAccount.from(
                    label = "Compte de dépôt",
                    balance = Random.nextDouble()
                )
            )
        )
        val second: Any = NonCreditAgricoleBank.from(
            name,
            accounts = setOf(
                BankAccount.from(
                    label = "Compte joint",
                    balance = Random.nextDouble()
                )
            )
        )
        assertEquals(expected = second, actual = first)
        assertEquals(expected = second.hashCode(), actual = first.hashCode())
    }

    @Test
    fun `structural equality should fail with null`() {
        val first: Any = NonCreditAgricoleBank.from(
            name = "Boursorama",
            accounts = setOf(
                BankAccount.from(
                    label = "Compte de dépôt",
                    balance = Random.nextDouble()
                )
            )
        )
        val second: Any? = null
        assertNotEquals(illegal = second, actual = first)
        assertNotEquals(illegal = second.hashCode(), actual = first.hashCode())
    }

    @Test
    fun `structural equality should fail with an object having another type than NonCreditAgricoleBank`() {
        val first: Any = NonCreditAgricoleBank.from(
            name = "Boursorama",
            accounts = setOf(
                BankAccount.from(
                    label = "Compte de dépôt",
                    balance = Random.nextDouble()
                )
            )
        )
        val second: Any = "wow"
        assertNotEquals(illegal = second, actual = first)
        assertNotEquals(illegal = second.hashCode(), actual = first.hashCode())
    }

    @Test
    fun `structural equality should fail with another NonCreditAgricoleBank having another name`() {
        val first: Any = NonCreditAgricoleBank.from(
            name = "Boursorama",
            accounts = setOf(
                BankAccount.from(
                    label = "Compte de dépôt",
                    balance = Random.nextDouble()
                )
            )
        )
        val second: Any = NonCreditAgricoleBank.from(
            name = "Banque populaire",
            accounts = setOf(
                BankAccount.from(
                    label = "Compte joint",
                    balance = Random.nextDouble()
                )
            )
        )
        assertNotEquals(illegal = second, actual = first)
        assertNotEquals(illegal = second.hashCode(), actual = first.hashCode())
    }

    @Test
    fun `toString should pass`() {
        val name = "Boursorama"
        val account: BankAccount = BankAccount.from(
            label = "Compte de dépôt",
            balance = Random.nextDouble()
        )
        val accounts: Set<BankAccount> = setOf(account)
        val actual: String = NonCreditAgricoleBank.from(name, accounts)
            .toString()
        val type: String = simpleNameOf<NonCreditAgricoleBank>()
        val expected = "$type(name=$name, accounts=$accounts)"
        assertEquals(expected, actual)
    }
}

class NonCreditAgricoleBankCompanionTest {
    @Test
    fun `from should pass with valid arguments`() {
        val name = "Boursorama"
        val account: BankAccount = BankAccount.from(
            label = "Compte de dépôt",
            balance = Random.nextDouble()
        )
        val accounts: Set<BankAccount> = setOf(account)
        val result: Result<NonCreditAgricoleBank> = kotlin.runCatching {
            NonCreditAgricoleBank.from(name, accounts)
        }
        assertTrue(result.isSuccess)
    }

    @Test
    fun `from should fail with a blank name`() {
        val name = "  "
        val account: BankAccount = BankAccount.from(
            label = "Compte de dépôt",
            balance = Random.nextDouble()
        )
        val accounts: Set<BankAccount> = setOf(account)
        val exception: IllegalArgumentException = assertFailsWith {
            NonCreditAgricoleBank.from(name, accounts)
        }
        val actual: String? = exception.message
        val expected: String = NonCreditAgricoleBank.BLANK_NAME_ERROR_MESSAGE
        assertEquals(expected, actual)
    }

    @Test
    fun `from should fail with an empty set of accounts`() {
        val name = "Boursorama"
        val accounts: Set<BankAccount> = emptySet()
        val exception: IllegalArgumentException = assertFailsWith {
            NonCreditAgricoleBank.from(name, accounts)
        }
        val actual: String? = exception.message
        val expected: String =
            NonCreditAgricoleBank.EMPTY_ACCOUNTS_ERROR_MESSAGE
        assertEquals(expected, actual)
    }
}
