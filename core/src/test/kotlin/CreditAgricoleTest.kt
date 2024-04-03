package lamarque.loic.catest.core

import lamarque.loic.catest.core.internal.simpleNameOf
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class CreditAgricoleTest {
    @Test
    fun `structural equality should pass with the same instance`() {
        val first: Any = CreditAgricole.from(
            region = "Languedoc",
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
    fun `structural equality should pass with another CreditAgricole in the same region`() {
        val region = "Languedoc"
        val first: Any = CreditAgricole.from(
            region,
            accounts = setOf(
                BankAccount.from(
                    label = "Compte de dépôt",
                    balance = Random.nextDouble()
                )
            )
        )
        val second: Any = CreditAgricole.from(
            region,
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
        val first: Any = CreditAgricole.from(
            region = "Languedoc",
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
    fun `structural equality should fail with an object having another type than CreditAgricole`() {
        val first: Any = CreditAgricole.from(
            region = "Languedoc",
            accounts = setOf(
                BankAccount.from(
                    label = "Compte de dépôt",
                    balance = Random.nextDouble()
                )
            )
        )
        val second: Any = "null"
        assertNotEquals(illegal = second, actual = first)
        assertNotEquals(illegal = second.hashCode(), actual = first.hashCode())
    }

    @Test
    fun `structural equality should fail with another CreditAgricole from another region`() {
        val first: Any = CreditAgricole.from(
            region = "Languedoc",
            accounts = setOf(
                BankAccount.from(
                    label = "Compte de dépôt",
                    balance = Random.nextDouble()
                )
            )
        )
        val second: Any = CreditAgricole.from(
            region = "Centre-Est",
            accounts = setOf(
                BankAccount.from(
                    label = "Compte de dépôt",
                    balance = Random.nextDouble()
                )
            )
        )
        assertNotEquals(illegal = second, actual = first)
        assertNotEquals(illegal = second.hashCode(), actual = first.hashCode())
    }

    @Test
    fun `toString should pass`() {
        val region = "Languedoc"
        val account: BankAccount = BankAccount.from(
            label = "Compte de dépôt",
            balance = Random.nextDouble()
        )
        val accounts: Set<BankAccount> = setOf(account)
        val bank: CreditAgricole = CreditAgricole.from(region, accounts)
        val actual = "$bank"
        val type: String = simpleNameOf<CreditAgricole>()
        val expected = "$type(region=$region, accounts=$accounts)"
        assertEquals(expected, actual)
    }
}

class CreditAgricoleCompanionTest {
    @Test
    fun `from should pass with valid arguments`() {
        val region = "Languedoc"
        val account: BankAccount = BankAccount.from(
            label = "Compte de dépôt",
            balance = Random.nextDouble()
        )
        val accounts: Set<BankAccount> = setOf(account)
        val result: Result<CreditAgricole> = kotlin.runCatching {
            CreditAgricole.from(region, accounts)
        }
        assertTrue(result.isSuccess)
    }

    @Test
    fun `from should fail with a blank region`() {
        val region = "  "
        val account: BankAccount = BankAccount.from(
            label = "Compte de dépôt",
            balance = Random.nextDouble()
        )
        val accounts: Set<BankAccount> = setOf(account)
        val exception: IllegalArgumentException = assertFailsWith {
            CreditAgricole.from(region, accounts)
        }
        val actual: String? = exception.message
        val expected = "Credit Agricole bank's region shouldn't be blank."
        assertEquals(expected, actual)
    }

    @Test
    fun `from should fail with an empty list of accounts`() {
        val region = "Languedoc"
        val accounts: Set<BankAccount> = emptySet()
        val exception: IllegalArgumentException = assertFailsWith {
            CreditAgricole.from(region, accounts)
        }
        val actual: String? = exception.message
        val expected =
            "Credit Agricole bank's should have at least one bank account."
        assertEquals(expected, actual)
    }
}
