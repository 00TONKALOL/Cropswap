import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.cropswap.AppDatabase
import com.example.cropswap.MainActivity
import com.example.cropswap.TenderApplication
import com.example.cropswap.ui.theme.CROPSWAPTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.firebase.database.FirebaseDatabase
import androidx.compose.ui.Alignment
import com.example.cropswap.WelcomeActivity

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
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var loading by remember { mutableStateOf(false) }
            var error by remember { mutableStateOf<String?>(null) }
            val context = LocalContext.current

            MaterialTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Login", style = MaterialTheme.typography.headlineMedium)
                        Spacer(Modifier.height(16.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(16.dp))
                        if (loading) {
                            CircularProgressIndicator()
                        } else {
                            Button(onClick = {
                                error = null
                                if (email.isBlank() || password.isBlank()) {
                                    error = "Fields cannot be empty"
                                    return@Button
                                }
                                // Request permission if needed
                                if (ContextCompat.checkSelfPermission(
                                        this as Context,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                }
                                loading = true
                                auth.signInWithEmailAndPassword(email, password)
                                    .addOnSuccessListener {
                                        val userId = auth.currentUser?.uid
                                        if (userId != null) {
                                            FirebaseDatabase.getInstance().reference.child("users").child(userId).get()
                                                .addOnSuccessListener { dataSnapshot ->
                                                    // You can use user data if needed
                                                    loading = false
                                                    val role = dataSnapshot.child("role").getValue(String::class.java) ?: "user"
                                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                                    intent.putExtra("user_role", role)
                                                    startActivity(intent)
                                                    finish()
                                                }
                                                .addOnFailureListener {
                                                    loading = false
                                                    error = "Failed to fetch user data"
                                                }
                                        } else {
                                            loading = false
                                            error = "User ID not found"
                                        }
                                    }
                                    .addOnFailureListener {
                                        loading = false
                                        error = it.localizedMessage ?: "Login failed"
                                    }
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text("Login")
                            }
                        }
                        error?.let {
                            Spacer(Modifier.height(8.dp))
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                        Spacer(Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = {
                                startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
                            }) {
                                Text("No account? Sign up")
                            }
                        }
                    }
                    // Back to Home button at the bottom
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {
                            startActivity(Intent(this@LoginActivity, WelcomeActivity::class.java))
                            finish()
                        }) {
                            Text("Back to Home")
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
