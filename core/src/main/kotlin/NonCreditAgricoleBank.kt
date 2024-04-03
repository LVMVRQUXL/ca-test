package lamarque.loic.catest.core

import kotools.types.collection.NotEmptySet
import kotools.types.collection.toNotEmptySet
import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import lamarque.loic.catest.core.internal.hashCodeOf
import lamarque.loic.catest.core.internal.simpleNameOf

/** Represents a bank other than [Credit Agricole][CreditAgricole]. */
public class NonCreditAgricoleBank(
    private val name: NotBlankString,
    private val accounts: NotEmptySet<BankAccount>
) {
    // -------------------- Structural equality operations ---------------------

    /**
     * Returns `true` if the [other] object is an instance of
     * [NonCreditAgricoleBank] with the same [name], or returns `false`
     * otherwise.
     */
    override fun equals(other: Any?): Boolean =
        other is NonCreditAgricoleBank && this.name == other.name

    /** Returns a hash code value for this bank. */
    override fun hashCode(): Int = hashCodeOf(name)

    // ------------------------------ Conversions ------------------------------

    /** Returns the string representation of this bank. */
    override fun toString(): String {
        val type: String = simpleNameOf<NonCreditAgricoleBank>()
        return "$type(name=$name, accounts=$accounts)"
    }

    // -------------------------------------------------------------------------

    /** Contains static declarations for the [NonCreditAgricoleBank] type. */
    public companion object {
        internal const val BLANK_NAME_ERROR_MESSAGE: String =
            "Bank's name shouldn't be blank."

        internal const val EMPTY_ACCOUNTS_ERROR_MESSAGE: String =
            "Bank should have at least one account."

        /**
         * Creates a bank from the specified [name] and [accounts].
         * Throws an [IllegalArgumentException] if the [name] is blank or if the
         * collection of [accounts] is empty.
         */
        public fun from(
            name: String,
            accounts: Set<BankAccount>
        ): NonCreditAgricoleBank {
            val validName: NotBlankString = requireNotNull(
                name.toNotBlankString().getOrNull(),
                this::BLANK_NAME_ERROR_MESSAGE
            )
            val validAccounts: NotEmptySet<BankAccount> = requireNotNull(
                accounts.toNotEmptySet().getOrNull(),
                this::EMPTY_ACCOUNTS_ERROR_MESSAGE
            )
            return NonCreditAgricoleBank(validName, validAccounts)
        }
    }
}
