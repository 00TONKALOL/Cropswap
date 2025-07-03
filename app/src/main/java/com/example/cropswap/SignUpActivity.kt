import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cropswap.MainActivity
import com.example.cropswap.WelcomeActivity
import com.example.cropswap.ui.theme.CROPSWAPTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : ComponentActivity() {
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var confirmPassword by remember { mutableStateOf("") }
            var loading by remember { mutableStateOf(false) }
            var error by remember { mutableStateOf<String?>(null) }

            CROPSWAPTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Sign Up", style = MaterialTheme.typography.headlineMedium)
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
                        Spacer(Modifier.height(8.dp))
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirm Password") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(16.dp))
                        if (loading) {
                            CircularProgressIndicator()
                        } else {
                            Button(onClick = {
                                error = null
                                if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                                    error = "All fields are required"
                                    return@Button
                                }
                                if (password != confirmPassword) {
                                    error = "Passwords don't match"
                                    return@Button
                                }
                                loading = true
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnSuccessListener {
                                        val userId = auth.currentUser?.uid
                                        val userMap = mapOf("email" to email)
                                        if (userId != null) {
                                            FirebaseDatabase.getInstance().reference.child("users").child(userId).setValue(userMap)
                                        }
                                        loading = false
                                        Toast.makeText(this@SignUpActivity, "Account created! Please sign in.", Toast.LENGTH_LONG).show()
                                        startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                                        finish()
                                    }
                                    .addOnFailureListener {
                                        loading = false
                                        error = it.localizedMessage ?: "Sign up failed"
                                    }
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text("Create Account")
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
                                startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                                finish()
                            }) {
                                Text("Already have an account? Sign in")
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
                            startActivity(Intent(this@SignUpActivity, WelcomeActivity::class.java))
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


