package lamarque.loic.catest.core

import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import lamarque.loic.catest.core.internal.hashCodeOf
import lamarque.loic.catest.core.internal.simpleNameOf

/** Represents a bank account. */
public class BankAccount private constructor(
    private val label: NotBlankString,
    private val balance: Double,
    private val operations: List<BankAccountOperation>
) {
    // -------------------- Structural equality operations ---------------------

    /**
     * Returns `true` if the [other] object is an instance of [BankAccount] with
     * the same [label], or returns `false` otherwise.
     */
    override fun equals(other: Any?): Boolean =
        other is BankAccount && this.label == other.label

    /** Returns a hash code value for this bank account. */
    override fun hashCode(): Int = hashCodeOf(label)

    // ------------------------------ Conversions ------------------------------

    /** Returns the string representation of this bank account. */
    override fun toString(): String {
        val type: String = simpleNameOf<BankAccount>()
        return "$type(label=$label, balance=$balance, operations=$operations)"
    }

    // -------------------------------------------------------------------------

    /** Contains static declarations for the [BankAccount] type. */
    public companion object {
        /**
         * Creates a bank account from the specified [label], [balance] and
         * [operations].
         * Throws an [IllegalArgumentException] if the [label] is blank.
         */
        public fun from(
            label: String,
            balance: Double,
            operations: List<BankAccountOperation> = emptyList()
        ): BankAccount {
            val validLabel: NotBlankString =
                requireNotNull(label.toNotBlankString().getOrNull()) {
                    "Bank account's label shouldn't be blank."
                }
            return BankAccount(validLabel, balance, operations)
        }
    }
}
