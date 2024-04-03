package lamarque.loic.catest.core

/** Represents a bank. */
public sealed interface Bank {
    /** The accounts of this bank. */
    public val accounts: List<BankAccount>
}

/** Represents a [Credit Agricole](https://www.credit-agricole.fr/) bank. */
public data class CreditAgricole(
    /** The region of this bank. */
    public val region: String,
    override val accounts: List<BankAccount>
) : Bank

/**
 * Returns a list of these banks sorted by [region][CreditAgricole.region]
 * alphabetically, ignoring case differences.
 */
public fun List<CreditAgricole>.sortedByRegionAlphabetically():
        List<CreditAgricole> = this.sortedBy { it.region.lowercase() }

/** Represents a bank that is not [Credit Agricole][CreditAgricole]. */
public data class NonCreditAgricoleBank(
    /** The name of this bank. */
    public val name: String,
    override val accounts: List<BankAccount>
) : Bank

/**
 * Returns a list of these banks sorted by [name][NonCreditAgricoleBank.name]
 * alphabetically, ignoring case differences.
 */
public fun List<NonCreditAgricoleBank>.sortedByNameAlphabetically():
        List<NonCreditAgricoleBank> = this.sortedBy { it.name.lowercase() }
