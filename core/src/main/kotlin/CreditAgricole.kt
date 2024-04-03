package lamarque.loic.catest.core

import kotools.types.collection.NotEmptySet
import kotools.types.collection.toNotEmptySet
import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import lamarque.loic.catest.core.internal.hashCodeOf
import lamarque.loic.catest.core.internal.simpleNameOf

/** Represents the [CreditAgricole](https://www.credit-agricole.fr/) bank. */
public class CreditAgricole private constructor(
    private val region: NotBlankString,
    private val accounts: NotEmptySet<BankAccount>
) {
    // -------------------- Structural equality operations ---------------------

    /**
     * Returns `true` if the [other] object is an instance of [CreditAgricole]
     * with the same [region], or returns `false` otherwise.
     */
    override fun equals(other: Any?): Boolean =
        other is CreditAgricole && this.region == other.region

    /** Returns a hash code value for this bank. */
    override fun hashCode(): Int = hashCodeOf(region)

    // ------------------------------ Conversions ------------------------------

    /** Returns the string representation of this bank. */
    override fun toString(): String {
        val type: String = simpleNameOf<CreditAgricole>()
        return "$type(region=$region, accounts=$accounts)"
    }

    // -------------------------------------------------------------------------

    /** Contains static declarations for the [CreditAgricole] type. */
    public companion object {
        internal const val BLANK_REGION_ERROR_MESSAGE: String =
            "Credit Agricole bank's region shouldn't be blank."

        internal const val EMPTY_ACCOUNTS_ERROR_MESSAGE: String =
            "Credit Agricole bank should have at least one bank account."

        /**
         * Creates a bank from the specified [region] and [accounts].
         * Throws an [IllegalArgumentException] if the [region] is blank or if
         * the collection of [accounts] is empty.
         */
        public fun from(
            region: String,
            accounts: Set<BankAccount>
        ): CreditAgricole {
            val validRegion: NotBlankString = requireNotNull(
                region.toNotBlankString().getOrNull(),
                this::BLANK_REGION_ERROR_MESSAGE
            )
            val validAccounts: NotEmptySet<BankAccount> = requireNotNull(
                accounts.toNotEmptySet().getOrNull(),
                this::EMPTY_ACCOUNTS_ERROR_MESSAGE
            )
            return CreditAgricole(validRegion, validAccounts)
        }
    }
}
