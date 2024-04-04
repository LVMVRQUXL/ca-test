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
import lamarque.loic.catest.ui.theme.CATestTheme

@Composable
@DevicePreviews
private fun AccountsScreenPreview(
    @PreviewParameter(PreviewProviderBankAccounts::class)
    banksAccounts: List<BankAccount>
): Unit = CATestTheme {
    AccountsScreen(accountViewModelPreview(banksAccounts))
}

private fun accountViewModelPreview(
    accounts: List<BankAccount>
): AccountsViewModel = AccountsViewModel(
    initialCreditAgricoleBanks = listOf(
        CreditAgricole(region = "Languedoc", accounts),
        CreditAgricole(region = "Centre-Est", accounts)
    ),
    nonCreditAgricoleBanks = listOf(
        NonCreditAgricoleBank(name = "Boursorama", accounts),
        NonCreditAgricoleBank(name = "Banque Populaire", accounts)
    )
)

class PreviewProviderBankAccounts :
    PreviewParameterProvider<List<BankAccount>> {
    override val values: Sequence<List<BankAccount>>
        get() = sequenceOf(
            listOf(
                BankAccount(
                    id = "151515151151",
                    title = "Compte joint",
                    balance = 843.15,
                    operations = emptyList()
                ),
                BankAccount(
                    id = "9892736780987654",
                    title = "Compte Mozaïc",
                    balance = 209.39,
                    operations = emptyList()
                ),
                BankAccount(
                    id = "2354657678098765",
                    title = "Compte de dépôt",
                    balance = 2031.84,
                    operations = emptyList()
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
