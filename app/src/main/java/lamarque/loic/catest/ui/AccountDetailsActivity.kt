package lamarque.loic.catest.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material.icons.sharp.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import lamarque.loic.catest.core.BankAccount
import lamarque.loic.catest.core.amountInEuros
import lamarque.loic.catest.core.balanceInEuros
import lamarque.loic.catest.core.frenchDate
import lamarque.loic.catest.old.ui.theme.CATestTheme

class AccountDetailsActivity : ComponentActivity() {
    private val viewModel: AccountDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                ::finish
            )
        else onBackPressedDispatcher.addCallback { finish() }

        val id: String = checkNotNull(intent.getStringExtra("bankAccountId")) {
            "Bank account's id not found from intent."
        }
        viewModel.fetchBankAccount(id)

        setContent {
            CATestTheme { AccountDetailsScreen(viewModel.account) }
        }
    }
}

@Composable
fun AccountDetailsScreen(account: BankAccount?) {
    val context: Context = LocalContext.current
    val activity: AccountDetailsActivity = context as AccountDetailsActivity
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Button(
                onClick = activity::finish,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Sharp.KeyboardArrowLeft,
                    contentDescription = null
                )
                Text(text = "Mes Comptes")
            }
            AnimatedContent(
                targetState = null != account,
                label = "AccountDetailsAnimated"
            ) {
                if (!it) Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
                else Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = account!!.balanceInEuros,
                        style = MaterialTheme.typography.displayMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                    Text(
                        text = account.title,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                    LazyColumn(modifier = Modifier.padding(vertical = 16.dp)) {
                        items(items = account.operations) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.padding(4.dp)) {
                                        Text(text = it.description)
                                        Text(text = it.frenchDate)
                                    }
                                    Text(text = it.amountInEuros)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
