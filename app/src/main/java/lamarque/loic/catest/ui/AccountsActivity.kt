package lamarque.loic.catest.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
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
import lamarque.loic.catest.ui.theme.CATestTheme

class AccountsActivity : ComponentActivity() {
    private val viewModel: AccountsViewModel by viewModels<AccountsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch { viewModel.fetchBanks() }
        setContent {
            val uiState: UIState by viewModel.uiState.collectAsState()
            CATestTheme {
                AccountsScreen(uiState, fetchBanks = viewModel::fetchBanks)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun AccountsScreen(
    uiState: UIState,
    modifier: Modifier = Modifier,
    fetchBanks: () -> Unit = {}
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
            targetState = uiState,
            label = "AnimatedBanks"
        ) { state: UIState ->
            when (state) {
                is UIState.Error -> ErrorScreen(fetchBanks)

                is UIState.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                is UIState.Success<*> -> LazyColumn {
                    val banksSummary = state.value as BanksSummary
                    stickyHeader {
                        BankGroup(stringResource(R.string.ca_bank))
                    }
                    items(items = banksSummary.creditAgricoleBanks) {
                        BankCard(bank = it)
                    }
                    stickyHeader {
                        BankGroup(stringResource(R.string.other_banks))
                    }
                    items(items = banksSummary.otherBanks) {
                        BankCard(bank = it)
                    }
                }
            }
        }
    }
}

@Composable
private fun BankGroup(name: String): Unit = Box(
    modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.background)
) {
    Text(
        text = name,
        modifier = Modifier.padding(vertical = 16.dp),
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun BankCard(bank: Bank) {
    var isExpanded: Boolean by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { isExpanded = !isExpanded }
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
                val degrees: Float = if (isExpanded) 180f else 0f
                Icon(
                    imageVector = Icons.Sharp.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .rotate(degrees)
                )
            }
        }
        AnimatedContent(
            targetState = isExpanded,
            label = "AnimatedCardExpansion"
        ) { isExpanded: Boolean ->
            if (isExpanded) Column {
                bank.accounts.forEach { AccountView(account = it) }
            }
        }
    }
}

@Composable
private fun AccountView(account: BankAccount) {
    val context: Context = LocalContext.current
    val launcher: ManagedActivityResultLauncher<Intent, ActivityResult> =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) {}
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp)
            .clickable {
                val intent = Intent(context, AccountDetailsActivity::class.java)
                intent.putExtra(
                    AccountDetailsActivity.BANK_ACCOUNT_ID_EXTRA,
                    account.id
                )
                launcher.launch(intent)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = account.title)
        Text(text = account.balanceInEuros)
    }
}

// --------------------------------- Previews ----------------------------------

@Composable
@DevicePreviews
private fun AccountsScreenPreview(
    @PreviewParameter(AccountsStatePreviewProvider::class) uiState: UIState
): Unit = CATestTheme { AccountsScreen(uiState) }

private class AccountsStatePreviewProvider : PreviewParameterProvider<UIState> {
    override val values: Sequence<UIState> = run {
        val accounts: List<BankAccount> = listOf(
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
        sequenceOf(
            UIState.Loading,
            UIState.Error,
            UIState.Success(
                BanksSummary(
                    creditAgricoleBanks = listOf(
                        CreditAgricole(region = "Languedoc", accounts),
                        CreditAgricole(region = "Centre-Est", accounts)
                    ),
                    otherBanks = listOf(
                        NonCreditAgricoleBank(name = "Boursorama", accounts),
                        NonCreditAgricoleBank(name = "SG", accounts)
                    ),
                )
            )
        )
    }
}
