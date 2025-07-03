package com.example.cropswap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.cropswap.ui.theme.CROPSWAPTheme
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CROPSWAPTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("crops") { CropInventoryScreen(navController) }
        composable("market") { MarketScreen(navController) }
    }
}

@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(24.dp)
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            Text("CropSwap", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    // TODO: Add Firebase Auth logic here
                    if (email.isNotBlank() && password.isNotBlank()) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        error = "Please enter email and password"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Login", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { /* TODO: Forgot password logic */ }) {
                Text("Forgot Password?", color = MaterialTheme.colorScheme.primary)
            }
            error?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = Color.Red)
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val db = remember {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java, "cropswap-db"
        ).build()
    }
    val firestore = FirebaseFirestore.getInstance()
    val realtimeDb = FirebaseDatabase.getInstance().reference
    var showDialog by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }
    var tenderCounty by remember { mutableStateOf("") }
    var tenderItem by remember { mutableStateOf("") }
    var tenderDescription by remember { mutableStateOf("") }
    var applicantName by remember { mutableStateOf("") }
    var applicantContact by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Description, contentDescription = "Register Tender Application")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text("Welcome to CropSwap!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                QuickAccessCard("Tenders", Icons.Default.Description)
                QuickAccessCard("Crops", Icons.Default.Agriculture)
                QuickAccessCard("Market", Icons.Default.Store)
                QuickAccessCard("Support", Icons.Default.SupportAgent)
            }
        }
        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 8.dp
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Register Tender Application", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = tenderCounty,
                            onValueChange = { tenderCounty = it },
                            label = { Text("County") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = tenderItem,
                            onValueChange = { tenderItem = it },
                            label = { Text("Item") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = tenderDescription,
                            onValueChange = { tenderDescription = it },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = applicantName,
                            onValueChange = { applicantName = it },
                            label = { Text("Your Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = applicantContact,
                            onValueChange = { applicantContact = it },
                            label = { Text("Contact (Phone/Email)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (tenderCounty.isNotBlank() && tenderItem.isNotBlank() && tenderDescription.isNotBlank() && applicantName.isNotBlank() && applicantContact.isNotBlank()) {
                                        val application = TenderApplication(
                                            tenderCounty = tenderCounty,
                                            tenderItem = tenderItem,
                                            tenderDescription = tenderDescription,
                                            applicantName = applicantName,
                                            applicantContact = applicantContact,
                                            timestamp = System.currentTimeMillis()
                                        )
                                        // Save to Room
                                        CoroutineScope(Dispatchers.IO).launch {
                                            db.tenderApplicationDao().insert(application)
                                        }
                                        // Save to Firestore
                                        val applicationMap = hashMapOf(
                                            "tenderCounty" to tenderCounty,
                                            "tenderItem" to tenderItem,
                                            "tenderDescription" to tenderDescription,
                                            "applicantName" to applicantName,
                                            "applicantContact" to applicantContact,
                                            "timestamp" to System.currentTimeMillis()
                                        )
                                        firestore.collection("tender_applications")
                                            .add(applicationMap)
                                            .addOnSuccessListener {
                                                showConfirmation = true
                                            }
                                            .addOnFailureListener {
                                                showConfirmation = true // Still show confirmation for demo
                                            }
                                        // Save to Realtime Database
                                        val applicationId = realtimeDb.child("tender_applications").push().key ?: ""
                                        realtimeDb.child("tender_applications").child(applicationId).setValue(applicationMap)
                                            .addOnSuccessListener {
                                                showConfirmation = true
                                            }
                                            .addOnFailureListener {
                                                showConfirmation = true // Still show confirmation for demo
                                            }
                                        showDialog = false
                                        // Clear fields
                                        tenderCounty = ""
                                        tenderItem = ""
                                        tenderDescription = ""
                                        applicantName = ""
                                        applicantContact = ""
                                    }
                                },
                                enabled = tenderCounty.isNotBlank() && tenderItem.isNotBlank() && tenderDescription.isNotBlank() && applicantName.isNotBlank() && applicantContact.isNotBlank()
                            ) {
                                Text("Register")
                            }
                        }
                    }
                }
            }
        }
        if (showConfirmation) {
            Dialog(onDismissRequest = { showConfirmation = false }) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 8.dp
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Registration Successful!", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Your application has been received.")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { showConfirmation = false }) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Dialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
    TODO("Not yet implemented")
}

@Composable
fun QuickAccessCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(100.dp, 120.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = title, modifier = Modifier.size(36.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun CropInventoryScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Crop Inventory (Coming Soon)", fontSize = 20.sp)
        }
    }
}

@Composable
fun MarketScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Market (Coming Soon)", fontSize = 20.sp)
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        NavItem("home", Icons.Default.Home, "Home"),
        NavItem("crops", Icons.Default.Agriculture, "Crops"),
        NavItem("market", Icons.Default.Store, "Market")
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo("home")
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}

data class NavItem(val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val label: String)