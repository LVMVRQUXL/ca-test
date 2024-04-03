package lamarque.loic.catest.core

import lamarque.loic.catest.core.internal.simpleNameOf
import java.util.Date
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class BankAccountOperationTest {
    @Test
    fun `structural operations should pass with the same instance`() {
        val first: Any = BankAccountOperation.from(
            title = "Netflix",
            date = Random.nextLong(),
            amount = Random.nextDouble()
        )
        val second: Any = first
        assertEquals(expected = second, actual = first)
        assertEquals(expected = second.hashCode(), actual = first.hashCode())
    }

    @Test
    fun `structural operations should pass with another AccountOperation having the same title, date and amount`() {
        val title = "Netflix"
        val date: Long = Random.nextLong()
        val amount: Double = Random.nextDouble()
        val first: Any = BankAccountOperation.from(title, date, amount)
        val second: Any = BankAccountOperation.from(title, date, amount)
        assertEquals(expected = second, actual = first)
        assertEquals(expected = second.hashCode(), actual = first.hashCode())
    }

    @Test
    fun `structural operations should fail with null`() {
        val first: Any = BankAccountOperation.from(
            title = "Netflix",
            date = Random.nextLong(),
            amount = Random.nextDouble()
        )
        val second: Any? = null
        assertNotEquals(illegal = second, actual = first)
        assertNotEquals(illegal = second.hashCode(), actual = first.hashCode())
    }

    @Test
    fun `structural operations should fail with an object having another type than AccountOperation`() {
        val first: Any = BankAccountOperation.from(
            title = "Netflix",
            date = Random.nextLong(),
            amount = Random.nextDouble()
        )
        val second: Any = "uh-oh"
        assertNotEquals(illegal = second, actual = first)
        assertNotEquals(illegal = second.hashCode(), actual = first.hashCode())
    }

    @Test
    fun `structural operations should fail with another AccountOperation having another title`() {
        val date: Long = Random.nextLong()
        val amount: Double = Random.nextDouble()
        val first: Any =
            BankAccountOperation.from(title = "Netflix", date, amount)
        val second: Any =
            BankAccountOperation.from(title = "Orange", date, amount)
        assertNotEquals(illegal = second, actual = first)
        assertNotEquals(illegal = second.hashCode(), actual = first.hashCode())
    }

    @Test
    fun `structural operations should fail with another AccountOperation having another date`() {
        val title = "Netflix"
        val amount: Double = Random.nextDouble()
        val first: Any =
            BankAccountOperation.from(title, date = Random.nextLong(), amount)
        val second: Any =
            BankAccountOperation.from(title, date = Random.nextLong(), amount)
        assertNotEquals(illegal = second, actual = first)
        assertNotEquals(illegal = second.hashCode(), actual = first.hashCode())
    }

    @Test
    fun `structural operations should fail with another AccountOperation having another amount`() {
        val title = "Netflix"
        val date: Long = Random.nextLong()
        val first: Any =
            BankAccountOperation.from(title, date, amount = Random.nextDouble())
        val second: Any =
            BankAccountOperation.from(title, date, amount = Random.nextDouble())
        assertNotEquals(illegal = second, actual = first)
        assertNotEquals(illegal = second.hashCode(), actual = first.hashCode())
    }

    @Test
    fun `toString() should pass`() {
        val title = "Netflix"
        val date: Long = Random.nextLong()
        val amount: Double = Random.nextDouble()
        val operation: Any = BankAccountOperation.from(title, date, amount)
        val actual = "$operation"
        val type: String = simpleNameOf<BankAccountOperation>()
        val expectedDate = Date(date)
        val expected = "$type(title=$title, date=$expectedDate, amount=$amount)"
        assertEquals(expected, actual)
    }
}

class BankAccountOperationCompanionTest {
    @Test
    fun `from should pass with valid arguments`() {
        val title = "Netflix"
        val date = 1644870724L
        val amount = -15.99
        val result: Result<BankAccountOperation> = kotlin.runCatching {
            BankAccountOperation.from(title, date, amount)
        }
        assertTrue(result.isSuccess)
    }

    @Test
    fun `from should fail with blank title`() {
        val title = "   "
        val date = 1644870724L
        val amount = -15.99
        val exception: IllegalArgumentException = assertFailsWith {
            BankAccountOperation.from(title, date, amount)
        }
        val actual: String? = exception.message
        val expected = "Account operation's title shouldn't be blank."
        assertEquals(expected, actual)
    }
}
