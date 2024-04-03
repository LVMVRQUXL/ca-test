package lamarque.loic.catest.core

import lamarque.loic.catest.core.internal.simpleNameOf
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class BankAccountTest {
    @Test
    fun `structural equality should pass with the same instance`() {
        val first: Any = BankAccount.from(
            label = "Compte de dépôt",
            balance = Random.nextDouble()
        )
        val second: Any = first
        assertEquals(expected = second, actual = first)
        assertEquals(expected = second.hashCode(), actual = first.hashCode())
    }

    @Test
    fun `structural equality should pass with another BankAccount having the same label`() {
        val label = "Compte de dépôt"
        val first: Any = BankAccount.from(label, balance = Random.nextDouble())
        val second: Any = BankAccount.from(label, balance = Random.nextDouble())
        assertEquals(expected = second, actual = first)
        assertEquals(expected = second.hashCode(), actual = first.hashCode())
    }

    @Test
    fun `structural equality should fail with null`() {
        val first: Any = BankAccount.from(
            label = "Compte de dépôt",
            balance = Random.nextDouble()
        )
        val second: Any? = null
        assertNotEquals(illegal = second, actual = first)
        assertNotEquals(illegal = second.hashCode(), actual = first.hashCode())
    }

    @Test
    fun `structural equality should fail with an object having another type than BankAccount`() {
        val first: Any = BankAccount.from(
            label = "Compte de dépôt",
            balance = Random.nextDouble()
        )
        val second: Any = "uh-oh"
        assertNotEquals(illegal = second, actual = first)
        assertNotEquals(illegal = second.hashCode(), actual = first.hashCode())
    }

    @Test
    fun `structural equality should fail with another BankAccount with another label`() {
        val first: Any = BankAccount.from(
            label = "Compte de dépôt",
            balance = Random.nextDouble()
        )
        val second: Any = BankAccount.from(
            label = "Compte joint",
            balance = Random.nextDouble()
        )
        assertNotEquals(illegal = second, actual = first)
        assertNotEquals(illegal = second.hashCode(), actual = first.hashCode())
    }

    @Test
    fun `toString should pass`() {
        val label = "Compte de dépôt"
        val balance: Double = Random.nextDouble()
        val operations: List<BankAccountOperation> = emptyList()
        val account: Any = BankAccount.from(label, balance, operations)
        val actual = "$account"
        val type: String = simpleNameOf<BankAccount>()
        val expected =
            "$type(label=$label, balance=$balance, operations=$operations)"
        assertEquals(expected, actual)
    }
}

class BankAccountCompanionTest {
    @Test
    fun `from should pass with valid arguments`() {
        val label = "Compte de dépôt"
        val balance: Double = Random.nextDouble()
        val result: Result<BankAccount> = kotlin.runCatching {
            BankAccount.from(label, balance)
        }
        assertTrue(result.isSuccess)
    }

    @Test
    fun `from should fail with blank label`() {
        val label = "  "
        val balance: Double = Random.nextDouble()
        val exception: IllegalArgumentException = assertFailsWith {
            BankAccount.from(label, balance)
        }
        val actual: String? = exception.message
        val expected = "Bank account's label shouldn't be blank."
        assertEquals(expected, actual)
    }
}
