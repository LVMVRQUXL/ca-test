package lamarque.loic.catest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import lamarque.loic.catest.data.local.AccountModel
import lamarque.loic.catest.data.local.BankModel
import lamarque.loic.catest.ui.BankingViewModel
import lamarque.loic.catest.ui.theme.CATestTheme

class MainActivity : ComponentActivity() {
    private val viewModel: BankingViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                BankingViewModel(this@MainActivity.assets) as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchAllBanks()
        setContent {
            CATestTheme {
                AccountsScreen(viewModel.banks)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AccountsScreen(
    banks: List<BankModel>,
    modifier: Modifier = Modifier
): Unit = Surface(
    modifier = modifier.fillMaxSize(),
    color = MaterialTheme.colorScheme.background
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Mes Comptes",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyColumn {
            stickyHeader {
                Text(
                    text = "Crédit Agricole",
                    modifier = modifier.padding(vertical = 16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            items(items = banks) { bank: BankModel ->
                if (bank.isCA) bank.accounts.forEach {
                    Account(account = it)
                }
            }
            stickyHeader {
                Text(
                    text = "Autres Banques",
                    modifier = modifier.padding(vertical = 16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            items(items = banks) { bank: BankModel ->
                if (!bank.isCA) Bank(text = bank.name) {
                    bank.accounts.forEach { Account(account = it) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Bank(
    text: String,
    modifier: Modifier = Modifier,
    accounts: @Composable () -> Unit
) {
    val isExpanded: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }
    Card(
        onClick = { isExpanded.value = !isExpanded.value },
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        Text(
            text = text,
            modifier = modifier
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .fillMaxWidth()
                .padding(16.dp)
        )
        if (isExpanded.value) accounts()
    }
}

@Composable
fun Account(account: AccountModel, modifier: Modifier = Modifier) {
    val text = "${account.holder} - ${account.label}"
    val context: Context = LocalContext.current
    val activity: MainActivity = context as MainActivity
    Text(
        text = text,
        modifier = modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(enabled = true) {
                Intent(context, AccountDetailsActivity::class.java).run {
                    putExtra("accountId", account.id)
                    activity.startActivity(this)
                }
                println("Clicked on '$text' account!")
            }
    )
}

@Preview(showBackground = true)
@Composable
fun AccountsLightPreview(): Unit = CATestTheme(darkTheme = false) {
    AccountsScreen(
        listOf(
            BankModel(
                name = "Crédit Agricole - Toulouse",
                isCA = true,
                accounts = listOf(
                    AccountModel(
                        id = "151515151151",
                        holder = "Pi'erre",
                        label = "Compte de dépôt",
                        balance = 2031.84,
                        operations = emptyList()
                    )
                )
            ),
            BankModel(
                name = "Boursorama",
                isCA = false,
                accounts = listOf(
                    AccountModel(
                        id = "09878900000",
                        holder = "ONE.dot",
                        label = "Compte de dépôt",
                        balance = 45.84,
                        operations = emptyList()
                    )
                )
            ),
            BankModel(
                name = "Banque Populaire",
                isCA = false,
                accounts = listOf(
                    AccountModel(
                        id = "3982997777",
                        holder = "T.Lion",
                        label = "Compte Chèques",
                        balance = 675.04,
                        operations = emptyList()
                    )
                )
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
fun AccountsDarkPreview(): Unit = CATestTheme(darkTheme = true) {
    AccountsScreen(emptyList())
}
