class SignUpActivity : ComponentActivity() {
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var email by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            var confirmPassword by remember { mutableStateOf("") }
            var loading by remember { mutableStateOf(false) }

            CROPNOVATheme {
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
                        loading = true
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener {
                                startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener {
                                loading = false
                                Toast.makeText(this@SignUpActivity, "Sign up failed", Toast.LENGTH_SHORT).show()
                            }
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Create Account")
                    }
                }
            }
        }
    }
}

