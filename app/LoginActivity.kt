import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : ComponentActivity() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CROPNOVATheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var isLoading by remember { mutableStateOf(false) }
                    var errorMessage by remember { mutableStateOf<String?>(null) }

                    LoginScreen(
                        isLoading = isLoading,
                        errorMessage = errorMessage,
                        onLogin = { email, password ->
                            if (email.isBlank() || password.isBlank()) {
                                errorMessage = "Fields cannot be empty"
                                return@LoginScreen
                            }

                            isLoading = true
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnSuccessListener {
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finish()
                                }
                                .addOnFailureListener {
                                    isLoading = false
                                    errorMessage = "Login failed: ${it.localizedMessage}"
                                }
                        },
                        onForgotPassword = { email ->
                            if (email.isNotBlank()) {
                                auth.sendPasswordResetEmail(email)
                                    .addOnSuccessListener {
                                        errorMessage = "Reset link sent to $email"
                                    }
                                    .addOnFailureListener {
                                        errorMessage = "Error: ${it.localizedMessage}"
                                    }
                            } else {
                                errorMessage = "Enter your email to reset password"
                            }
                        },
                        onSignup = {
                            startActivity(Intent(this, SignupActivity::class.java))
                        }
                    )
                }
            }
        }
    }
}
