package lamarque.loic.catest.ui

import android.content.Context
import android.content.Intent
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
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
            CATestTheme { AccountsScreen(viewModel) }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun AccountsScreen(
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
                val degrees: Float =
                    if (viewModel.isExpanded(bank)) 180f else 0f
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
            targetState = viewModel.isExpanded(bank),
            label = "AnimatedCardExpansion"
        ) { isExpanded: Boolean ->
            if (isExpanded) Column {
                bank.accounts.forEach { AccountCard(account = it) }
            }
        }
    }
}

@Composable
private fun AccountCard(account: BankAccount) {
    val context: Context = LocalContext.current
    val activity: AccountsActivity = context as AccountsActivity
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp)
            .clickable {
                val intent = Intent(context, AccountDetailsActivity::class.java)
                intent.putExtra("bankAccountId", account.id)
                activity.startActivity(intent)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = account.title)
        Text(text = account.balanceInEuros)
    }
}
