import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cropswap.MainActivity
import com.example.cropswap.LoginActivity
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

            CROPSWAPTheme {
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

                    Button(onClick = {
                        if (password != confirmPassword) {
                            Toast.makeText(this@SignUpActivity, "Passwords don't match", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener {
                                val userId = auth.currentUser?.uid
                                val userMap = mapOf("email" to email)
                                if (userId != null) {
                                    FirebaseDatabase.getInstance().reference.child("users").child(userId).setValue(userMap)
                                }
                                Toast.makeText(this@SignUpActivity, "Account created! Please sign in.", Toast.LENGTH_LONG).show()
                                startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this@SignUpActivity, "Sign up failed", Toast.LENGTH_SHORT).show()
                            }
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Create Account")
                    }

                    Spacer(Modifier.height(16.dp))

                    Button(onClick = {
                        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                        finish()
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Back to Login")
                    }
                }
            }
        }
    }
}


