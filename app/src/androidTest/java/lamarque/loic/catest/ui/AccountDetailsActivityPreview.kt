@file:Suppress("TestFunctionName")

package lamarque.loic.catest.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import lamarque.loic.catest.core.BankAccount
import lamarque.loic.catest.core.BankAccountOperation
import lamarque.loic.catest.old.ui.theme.CATestTheme
import java.util.Date

@Composable
@DevicePreviews
fun AccountDetailsPreview(
    @PreviewParameter(BankAccountProvider::class) account: BankAccount
): Unit = CATestTheme {
    AccountDetailsScreen(account)
}

private class BankAccountProvider : PreviewParameterProvider<BankAccount> {
    override val values: Sequence<BankAccount> = sequenceOf(
        BankAccount(
            id = "151515151151",
            title = "Compte principal",
            balance = 843.15,
            operations = listOf(
                BankAccountOperation(
                    description = "Netflix",
                    date = Date(1644784369000),
                    amount = -15.99
                ),
                BankAccountOperation(
                    description = "Orange",
                    date = Date(1644784369000),
                    amount = -15.99
                )
            )
        )
    )
}
