@file:Suppress("TestFunctionName")

package lamarque.loic.catest.ui

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import lamarque.loic.catest.core.BankAccount
import lamarque.loic.catest.core.CreditAgricole
import lamarque.loic.catest.core.NonCreditAgricoleBank
import lamarque.loic.catest.old.ui.theme.CATestTheme

@Composable
@DevicePreviews
private fun AccountsScreenPreview(
    @PreviewParameter(PreviewProviderBankAccounts::class)
    banksAccounts: List<BankAccount>
): Unit = CATestTheme {
    AccountsScreen(accountViewModelPreview(banksAccounts))
}


private fun accountViewModelPreview(
    bankAccounts: List<BankAccount>
): AccountsViewModel = AccountsViewModel(
    initialCreditAgricoleBanks = listOf(
        CreditAgricole(
            region = "Languedoc",
            accounts = bankAccounts
        ),
        CreditAgricole(
            region = "Centre-Est",
            accounts = listOf(
                BankAccount(
                    title = "Compte de dépôt",
                    balance = 425.84
                )
            )
        )
    ),
    nonCreditAgricoleBanks = listOf(
        NonCreditAgricoleBank(
            name = "Boursorama",
            accounts = listOf(
                BankAccount(
                    title = "Compte de dépôt",
                    balance = 45.84
                )
            )
        ),
        NonCreditAgricoleBank(
            name = "Banque Populaire",
            accounts = listOf(
                BankAccount(
                    title = "Compte joint",
                    balance = 675.04
                ),
                BankAccount(
                    title = "Compte Chèques",
                    balance = 675.04
                ),
                BankAccount(
                    title = "Compte de dépôt",
                    balance = 675.04
                )
            )
        )
    )
)

class PreviewProviderBankAccounts :
    PreviewParameterProvider<List<BankAccount>> {
    override val values: Sequence<List<BankAccount>>
        get() = sequenceOf(
            listOf(
                BankAccount(
                    title = "Compte joint",
                    balance = 843.15
                ),
                BankAccount(
                    title = "Compte Mozaïc",
                    balance = 209.39
                ),
                BankAccount(
                    title = "Compte de dépôt",
                    balance = 2031.84
                )
            )
        )
}

@Preview(
    name = "phone_light",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "phone_dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class DevicePreviews
