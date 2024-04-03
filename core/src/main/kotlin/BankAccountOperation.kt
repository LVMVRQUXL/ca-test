package lamarque.loic.catest.core

import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import lamarque.loic.catest.core.internal.hashCodeOf
import lamarque.loic.catest.core.internal.simpleNameOf
import java.util.Date

/** Represents a bank account operation. */
public class BankAccountOperation private constructor(
    private val title: NotBlankString,
    private val date: Date,
    private val amount: Double
) {
    // -------------------- Structural equality operations ---------------------

    /**
     * Returns `true` if the [other] object is an instance of
     * [BankAccountOperation] with the same [title], [date] and [amount], or
     * returns `false` otherwise.
     */
    override fun equals(other: Any?): Boolean = other is BankAccountOperation
            && this.title == other.title
            && this.date == other.date
            && this.amount == other.amount

    /** Returns a hash code value for this operation. */
    override fun hashCode(): Int = hashCodeOf(title, date, amount)

    // ------------------------------ Conversions ------------------------------

    /** Returns the string representation of this operation. */
    override fun toString(): String {
        val type: String = simpleNameOf<BankAccountOperation>()
        return "$type(title=$title, date=$date, amount=$amount)"
    }

    // -------------------------------------------------------------------------

    /** Contains static declarations for the [BankAccountOperation] type. */
    public companion object {
        /**
         * Creates a bank account operation from the specified [title], [date]
         * and [amount].
         * Throws an [IllegalArgumentException] if the [title] is blank or if
         * the [date] doesn't represent a valid date.
         */
        public fun from(
            title: String,
            date: Long,
            amount: Double
        ): BankAccountOperation {
            val validTitle: NotBlankString =
                requireNotNull(title.toNotBlankString().getOrNull()) {
                    "Account operation's title shouldn't be blank."
                }
            val validDate = Date(date)
            return BankAccountOperation(validTitle, validDate, amount)
        }
    }
}
