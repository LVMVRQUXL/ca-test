package lamarque.loic.catest.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowDown
import androidx.compose.material.icons.sharp.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import lamarque.loic.catest.R
import lamarque.loic.catest.core.Bank
import lamarque.loic.catest.core.BankAccount
import lamarque.loic.catest.core.CreditAgricole
import lamarque.loic.catest.core.NonCreditAgricoleBank
import lamarque.loic.catest.core.balanceInEuros
import lamarque.loic.catest.core.totalBalanceInEuros
import lamarque.loic.catest.old.ui.theme.CATestTheme

class AccountsActivity : ComponentActivity() {
    private val viewModel: AccountsViewModel by viewModels<AccountsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch { viewModel.fetchBanks() }
        setContent {
            CATestTheme { AccountsScreen(viewModel) }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun AccountsScreen(
    viewModel: AccountsViewModel,
    modifier: Modifier = Modifier
): Unit = Surface(
    modifier = modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.my_accounts),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        AnimatedContent(
            targetState = viewModel.areBanksAvailable,
            label = "AnimatedBanks"
        ) { banksAvailable: Boolean ->
            if (!banksAvailable) Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            else LazyColumn {
                stickyHeader {
                    BankGroup(stringResource(R.string.ca_bank))
                }
                items(items = viewModel.creditAgricoleBanks) {
                    BankCard(bank = it, viewModel = viewModel)
                }
                stickyHeader {
                    BankGroup(stringResource(R.string.other_banks))
                }
                items(items = viewModel.otherBanks) {
                    BankCard(bank = it, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
private fun BankGroup(name: String): Unit = Text(
    text = name,
    modifier = Modifier.padding(vertical = 16.dp),
    style = MaterialTheme.typography.titleMedium
)

@Composable
private fun BankCard(bank: Bank, viewModel: AccountsViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { viewModel.onBankClicked(bank) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val text: String = when (bank) {
                is CreditAgricole -> bank.region
                is NonCreditAgricoleBank -> bank.name
            }
            Text(text = text)
            Row {
                Text(text = bank.accounts.totalBalanceInEuros)
                AnimatedContent(
                    targetState = viewModel.isExpanded(bank),
                    label = "AnimatedIcon"
                ) {
                    val icon: ImageVector =
                        if (it) Icons.Sharp.KeyboardArrowUp
                        else Icons.Sharp.KeyboardArrowDown
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        }
        AnimatedContent(
            targetState = viewModel.isExpanded(bank),
            label = "AnimatedCardExpansion"
        ) { isExpanded: Boolean ->
            if (isExpanded) bank.accounts.forEach { AccountCard(it) }
        }
    }
}

@Composable
private fun AccountCard(account: BankAccount): Unit = Row(
    modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.primaryContainer)
        .padding(16.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
) {
    Text(text = account.title)
    Text(text = account.balanceInEuros)
}

// --------------------------------- Previews ----------------------------------

@Composable
@Preview(showBackground = true)
fun AccountsScreenLightPreview(): Unit = CATestTheme(darkTheme = false) {
    AccountsScreen(accountViewModelPreview())
}

@Composable
@Preview(showBackground = true)
fun AccountsScreenDarkPreview(): Unit = CATestTheme(darkTheme = true) {
    AccountsScreen(accountViewModelPreview())
}

private fun accountViewModelPreview(): AccountsViewModel = AccountsViewModel(
    initialCreditAgricoleBanks = listOf(
        CreditAgricole(
            region = "Languedoc",
            accounts = listOf(
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
