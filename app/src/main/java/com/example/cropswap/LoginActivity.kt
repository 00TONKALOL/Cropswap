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
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.room.Room

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
            CROPNOVATheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var isLoading by remember { mutableStateOf(false) }
                    var errorMessage by remember { mutableStateOf<String?>(null) }

                    LoginScreen(
                        isLoading = isLoading,
                        errorMessage = errorMessage,
                        onLogin = { email, password, profilePhotoUri ->
                            if (email.isBlank() || password.isBlank()) {
                                errorMessage = "Fields cannot be empty"
                                return@LoginScreen
                            }
                            // Request permission if needed
                            if (profilePhotoUri != null &&
                                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            ) {
                                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                            isLoading = true
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnSuccessListener {
                                    // Save profile photo URI in Room
                                    val application = TenderApplication(
                                        tenderCounty = "",
                                        tenderItem = "",
                                        tenderDescription = "",
                                        applicantName = email,
                                        applicantContact = email,
                                        timestamp = System.currentTimeMillis(),
                                        profilePhotoUri = profilePhotoUri
                                    )
                                    Thread {
                                        db.tenderApplicationDao().insert(application)
                                    }.start()
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
