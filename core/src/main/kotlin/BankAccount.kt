package lamarque.loic.catest.core

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Represents a bank account operation. */
public class BankAccountOperation(
    public val description: String,
    public val date: Date,
    public val amount: Double
)

/** Returns the amount of this operation in euros. */
public val BankAccountOperation.amountInEuros: String get() = amount.euros()

/** Returns the date of this operation, formatted for french people. */
public val BankAccountOperation.frenchDate: String
    get() {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
        return formatter.format(date)
    }

/** Represents a bank account. */
public class BankAccount(
    /** The identifier of this bank account. */
    public val id: String,
    /** The title of this bank account. */
    public val title: String,
    /** The balance available on this bank account. */
    public val balance: Double,
    operations: List<BankAccountOperation>
) {
    /** The operations on this bank account. */
    public val operations: List<BankAccountOperation> = operations
        .sortedBy { it.description }
        .sortedByDescending { it.date }
}

/** Returns the balance of this bank account in euros. */
public val BankAccount.balanceInEuros: String get() = balance.euros()

/** Returns the total balance of these bank accounts in euros. */
public val List<BankAccount>.totalBalanceInEuros: String
    get() = sumOf(BankAccount::balance).euros()

private fun Double.euros(): String = String.format("%.2f €", this)
    .replace('.', ',')

/**
 * Returns a list of these bank accounts sorted [title][BankAccount.title]
 * alphabetically, ignoring case differences.
 */
public fun List<BankAccount>.sortedByTitleAlphabetically(): List<BankAccount> =
    this.sortedBy { it.title.lowercase() }
