package lamarque.loic.catest.ui

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import lamarque.loic.catest.R
import lamarque.loic.catest.core.BankAccount
import lamarque.loic.catest.core.BankAccountOperation
import lamarque.loic.catest.core.amountInEuros
import lamarque.loic.catest.core.balanceInEuros
import lamarque.loic.catest.core.frenchDate
import lamarque.loic.catest.ui.theme.CATestTheme
import java.util.Date

class AccountDetailsActivity : ComponentActivity() {
    private val viewModel: AccountDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.configureFinishOnBack()

        val id: String? = intent.getStringExtra(BANK_ACCOUNT_ID_EXTRA)
        checkNotNull(id) { "Bank account's id not found from intent." }
        viewModel.fetchBankAccount(id)

        setContent {
            CATestTheme {
                val uiState: UIState by viewModel.uiState.collectAsState()
                AccountDetailsScreen(uiState, finishActivity = this::finish) {
                    viewModel.fetchBankAccount(id)
                }
            }
        }
    }

    private fun configureFinishOnBack() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                ::finish
            )
        else onBackPressedDispatcher.addCallback { finish() }
    }

    companion object {
        const val BANK_ACCOUNT_ID_EXTRA: String = "bankAccountId"
    }
}

@Composable
fun AccountDetailsScreen(
    uiState: UIState,
    finishActivity: () -> Unit,
    fetchData: () -> Unit
): Unit =
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        AnimatedContent(
            targetState = uiState,
            label = "UIStateAnimated"
        ) { state: UIState ->
            when (state) {
                is UIState.Error -> ErrorScreen(fetchData)
                UIState.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }

                is UIState.Success<*> -> AccountDetailsView(
                    state.value as BankAccount,
                    finishActivity
                )
            }
        }
    }

@Composable
private fun AccountDetailsView(
    bankAccount: BankAccount,
    finishActivity: () -> Unit
): Unit = Column {
    Button(
        onClick = finishActivity,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Icon(
            imageVector = Icons.Sharp.KeyboardArrowLeft,
            contentDescription = null
        )
        Text(text = stringResource(id = R.string.my_accounts))
    }
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = bankAccount.balanceInEuros,
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Text(
            text = bankAccount.title,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
        LazyColumn(modifier = Modifier.padding(vertical = 16.dp)) {
            items(items = bankAccount.operations) {
                OperationCard(it)
            }
        }
    }
}

@Composable
private fun OperationCard(operation: BankAccountOperation): Unit = Card(
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
        Column {
            Text(text = operation.description)
            Text(text = operation.frenchDate)
        }
        Text(text = operation.amountInEuros)
    }
}

// --------------------------------- Previews ----------------------------------

@Composable
@DevicePreviews
private fun AccountDetailsPreview(
    @PreviewParameter(AccountDetailsUIStateProvider::class) uiState: UIState
): Unit = CATestTheme {
    AccountDetailsScreen(uiState, finishActivity = {}, fetchData = {})
}

private class AccountDetailsUIStateProvider :
    PreviewParameterProvider<UIState> {
    @Suppress("SpellCheckingInspection")
    override val values: Sequence<UIState> = sequenceOf(
        UIState.Loading,
        UIState.Error,
        UIState.Success(
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
    )
}

