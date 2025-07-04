import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.cropswap.AppDatabase
import com.example.cropswap.MainActivity
import com.example.cropswap.TenderApplication
import com.example.cropswap.ui.theme.CROPSWAPTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "cropswap-db"
        ).build()

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            // Handle permission result if needed
        }

        setContent {
            CROPSWAPTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var isLoading by remember { mutableStateOf(false) }
                    var errorMessage by remember { mutableStateOf<String?>(null) }

                    LoginScreen(
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
                        isLoading = isLoading,
                        errorMessage = errorMessage,
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
                        onSignUpClick = {
                            startActivity(Intent(this, SignUpActivity::class.java))
                        },
                        onThemeToggle = {}
                    )
                }
            }
        }
    }
}
