package lamarque.loic.catest.core

/** Represents a bank account. */
public class BankAccount(
    /** The title of this bank account. */
    public val title: String,
    /** The balance available on this bank account. */
    public val balance: Double
)

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
