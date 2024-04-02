package lamarque.loic.catest

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import lamarque.loic.catest.data.local.AccountModel
import lamarque.loic.catest.ui.BankingViewModel
import lamarque.loic.catest.ui.theme.CATestTheme

class AccountDetailsActivity : ComponentActivity() {
    private val viewModel: BankingViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                BankingViewModel(this@AccountDetailsActivity.assets) as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                ::finish
            )
        else onBackPressedDispatcher.addCallback { finish() }
        val accountId = checkNotNull(intent.getStringExtra("accountId"))
        lifecycleScope.launch { viewModel.fetchAccountDetails(accountId) }
        setContent {
            CATestTheme { AccountDetails(account = viewModel.account) }
        }
    }
}

@Composable
fun AccountDetails(account: AccountModel?, modifier: Modifier = Modifier) {
    val context: Context = LocalContext.current
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column {
            Button(
                onClick = {
                    (context as AccountDetailsActivity).finish()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary
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
                label = "animation"
            ) { targetState ->
                if (!targetState) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else Text(text = account!!.label)
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun AccountDetailsLightPreview(): Unit = CATestTheme {
    AccountDetails(null)
}
