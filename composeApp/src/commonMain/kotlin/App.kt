import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.publicvalue.multiplatform.oidc.OpenIdConnectClient
import org.publicvalue.multiplatform.oidc.types.CodeChallengeMethod

@Composable
@Preview
fun App() {
    var persistedOpenIdConfig by mutableSetting()
    var persistedEmail by mutableSetting()


    var openIdConfig: String by remember { mutableStateOf(persistedOpenIdConfig) }
    var loggedIn by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }


    val client = OpenIdConnectClient(discoveryUri = "<discovery url>") {
        endpoints {
            tokenEndpoint = "<tokenEndpoint>"
            authorizationEndpoint = "<authorizationEndpoint>"
            userInfoEndpoint = null
            endSessionEndpoint = "<endSessionEndpoint>"
        }

        clientId = "<clientId>"
        clientSecret = "<clientSecret>"
        scope = "openid profile"
        codeChallengeMethod = CodeChallengeMethod.S256
        redirectUri = "<redirectUri>"
    }


    MaterialTheme {
        if (openIdConfig.isEmpty()) {
            QrScannerCompose(openIdConfig) {
                openIdConfig = it
                persistedOpenIdConfig = it
            }
        } else {
            if (!loggedIn) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Login",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp)
                    )
                    Text(
                        text = openIdConfig,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp)
                    )

                    LoginElement(initialEmail = persistedEmail) {
                        loggedIn = loggedIn.not()
                        persistedEmail = it.email
                        password = it.password
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    Button(
                        onClick = { openIdConfig = "" },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Reset URL")
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Email: $persistedEmail")
                    Text(text = "Passwort: $password")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { loggedIn = loggedIn.not() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Log out")
                    }
                }
            }

        }
    }
}