package com.example.cropswap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.window.Dialog
import com.google.firebase.database.FirebaseDatabase
import android.content.Intent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.lazy.items

class MainActivity : ComponentActivity() {
    var userRole: String = "user"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userRole = intent.getStringExtra("user_role") ?: "user"
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
    NavHost(navController, startDestination = "home") {
        composable("login") { LoginScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("crops") { CropInventoryScreen(navController) }
        composable("market") { MarketScreen(navController) }
        composable("tenders") { TenderScreen(
            onBackToWelcome = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            }
        ) }
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

data class Officer(val name: String, val role: String, val phone: String)

@Composable
fun OfficerListDialog(show: Boolean, onDismiss: () -> Unit) {
    if (show) {
        val officers = listOf(
            Officer("James Kariuki", "Tender Officer", "+254712345678"),
            Officer("Alice Wambui", "Extension Officer", "+254734567890"),
            Officer("John Otieno", "Tender Officer", "+254701234567"),
            Officer("Grace Mwikali", "Extension Officer", "+254798765432"),
            Officer("Peter Njoroge", "Market Specialist", "+254722334455"),
            Officer("Mary Achieng", "Crop Specialist", "+254733445566"),
            Officer("Samuel Kiptoo", "Livestock Officer", "+254700112233"),
            Officer("Fatuma Hassan", "Irrigation Expert", "+254799887766"),
            Officer("David Mwaura", "Soil Scientist", "+254711223344"),
            Officer("Janet Chebet", "Seed Inspector", "+254722556677"),
            Officer("Paul Mwangi", "Pest Control Officer", "+254723456789"),
            Officer("Linda Njeri", "Agroforestry Specialist", "+254734567891"),
            Officer("Brian Oduor", "Extension Officer", "+254745678912"),
            Officer("Catherine Mutua", "Horticulture Officer", "+254756789123"),
            Officer("Elijah Kipkorir", "Veterinary Officer", "+254767891234"),
            Officer("Naomi Wanjiku", "Soil Analyst", "+254778912345"),
            Officer("Patrick Otieno", "Seed Quality Inspector", "+254789123456"),
            Officer("Susan Atieno", "Irrigation Engineer", "+254790123456"),
            Officer("George Kimani", "Extension Officer", "+254701234568"),
            Officer("Mercy Chebet", "Crop Protection Officer", "+254712345679")
        )
        Dialog(onDismissRequest = onDismiss) {
            Surface(shape = RoundedCornerShape(16.dp), tonalElevation = 8.dp) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Extension Officers", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                        items(officers) { officer ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(officer.name, style = MaterialTheme.typography.titleMedium)
                                    Text(officer.role, color = MaterialTheme.colorScheme.primary)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("Contact: ${officer.phone}")
                                        Spacer(modifier = Modifier.width(8.dp))
                                        val context = LocalContext.current
                                        IconButton(onClick = {
                                            val intent = android.content.Intent(android.content.Intent.ACTION_DIAL).apply {
                                                data = android.net.Uri.parse("tel:${officer.phone}")
                                            }
                                            context.startActivity(intent)
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Phone,
                                                contentDescription = "Call Officer",
                                                tint = Color(0xFF388E3C)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                        Text("Close")
                    }
                }
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
    var showOfficerDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = { BottomNavBar(navController, onShowOfficers = { showOfficerDialog = true }) },
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Welcome to CropSwap", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))
            // Centered grid for QuickAccessCards
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        QuickAccessCard("Tenders", Icons.Default.Description, Modifier.width(150.dp), onClick = {
                            navController.navigate("tenders")
                        })
                        QuickAccessCard("Crops", Icons.Default.Agriculture, Modifier.width(150.dp), onClick = {
                            navController.navigate("crops")
                        })
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        QuickAccessCard("Market", Icons.Default.Store, Modifier.width(150.dp), onClick = {
                            navController.navigate("market")
                        })
                        QuickAccessCard("Extension Officers", Icons.Default.SupportAgent, Modifier.width(150.dp), onClick = {
                            showOfficerDialog = true
                        })
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    context.startActivity(Intent(context, WelcomeActivity::class.java))
                    if (context is android.app.Activity) (context as android.app.Activity).finish()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Text("Back to Home")
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
        OfficerListDialog(show = showOfficerDialog, onDismiss = { showOfficerDialog = false })
    }
}

@Composable
fun QuickAccessCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Card(
        modifier = modifier
            .height(120.dp)
            .padding(vertical = 8.dp)
            .clickable { onClick() },
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
    val context = LocalContext.current
    val firebaseDb = remember { FirebaseDatabase.getInstance().reference.child("crops_inventory") }
    var crops by remember { mutableStateOf(
        mutableListOf(
            CropItem("Maize", 120, "bales"),
            CropItem("Beans", 80, "bags"),
            CropItem("Wheat", 60, "bags"),
            CropItem("Sukuma Wiki", 200, "bunches"),
            CropItem("Tomatoes", 150, "crates"),
            CropItem("Cabbage", 90, "heads")
        )
    ) }
    var firebaseCrops by remember { mutableStateOf(listOf<CropItem>()) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showRequestDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var cropToEdit by remember { mutableStateOf<CropItem?>(null) }
    var newCropName by remember { mutableStateOf("") }
    var newCropCount by remember { mutableStateOf("") }
    var newCropUnit by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var totalCount by remember { mutableStateOf(0) }
    var averageCount by remember { mutableStateOf(0.0) }
    var maxCrop by remember { mutableStateOf<CropItem?>(null) }
    var minCrop by remember { mutableStateOf<CropItem?>(null) }

    // Listen for Firebase updates
    LaunchedEffect(Unit) {
        firebaseDb.get().addOnSuccessListener { snapshot ->
            val items = snapshot.children.mapNotNull { it.getValue(CropItem::class.java) }
            firebaseCrops = items
            val allCrops = (crops + items)
            totalCount = allCrops.sumOf { it.count }
            averageCount = if (allCrops.isNotEmpty()) allCrops.map { it.count }.average() else 0.0
            maxCrop = allCrops.maxByOrNull { it.count }
            minCrop = allCrops.minByOrNull { it.count }
        }.addOnFailureListener {
            errorMessage = "Failed to load crops from Firebase."
        }
    }

    fun addCropToFirebase(crop: CropItem) {
        firebaseDb.push().setValue(crop)
            .addOnSuccessListener {
                firebaseCrops = firebaseCrops + crop
                val allCrops = (crops + firebaseCrops)
                totalCount = allCrops.sumOf { it.count }
                averageCount = if (allCrops.isNotEmpty()) allCrops.map { it.count }.average() else 0.0
                maxCrop = allCrops.maxByOrNull { it.count }
                minCrop = allCrops.minByOrNull { it.count }
            }
            .addOnFailureListener {
                errorMessage = "Failed to add crop to Firebase."
            }
    }

    fun removeCropFromFirebase(crop: CropItem) {
        firebaseDb.get().addOnSuccessListener { snapshot ->
            snapshot.children.find { it.getValue(CropItem::class.java) == crop }?.ref?.removeValue()
            firebaseCrops = firebaseCrops.filter { it != crop }
            val allCrops = (crops + firebaseCrops)
            totalCount = allCrops.sumOf { it.count }
            averageCount = if (allCrops.isNotEmpty()) allCrops.map { it.count }.average() else 0.0
            maxCrop = allCrops.maxByOrNull { it.count }
            minCrop = allCrops.minByOrNull { it.count }
        }
    }

    fun editCropInFirebase(oldCrop: CropItem, newCrop: CropItem) {
        firebaseDb.get().addOnSuccessListener { snapshot ->
            snapshot.children.find { it.getValue(CropItem::class.java) == oldCrop }?.ref?.setValue(newCrop)
            firebaseCrops = firebaseCrops.map { if (it == oldCrop) newCrop else it }
            val allCrops = (crops + firebaseCrops)
            totalCount = allCrops.sumOf { it.count }
            averageCount = if (allCrops.isNotEmpty()) allCrops.map { it.count }.average() else 0.0
            maxCrop = allCrops.maxByOrNull { it.count }
            minCrop = allCrops.minByOrNull { it.count }
        }
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            Row(modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)) {
                FloatingActionButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.Agriculture, contentDescription = "Add Crop")
                }
                Spacer(modifier = Modifier.width(16.dp))
                FloatingActionButton(onClick = { showRequestDialog = true }) {
                    Icon(Icons.Default.SupportAgent, contentDescription = "Request Inventory")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Crop Inventory", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Total Inventory: $totalCount", fontWeight = FontWeight.Medium)
            Text("Average Count: ${"%.2f".format(averageCount)}", fontWeight = FontWeight.Medium)
            maxCrop?.let { Text("Highest: ${it.name} (${it.count} ${it.unit})", color = Color.Green) }
            minCrop?.let { Text("Lowest: ${it.name} (${it.count} ${it.unit})", color = Color.Red) }
            Spacer(modifier = Modifier.height(12.dp))
            (crops + firebaseCrops).forEach { crop ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(crop.name, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                            Text("${crop.count} ${crop.unit}", color = Color.Gray)
                        }
                        Row {
                            IconButton(onClick = {
                                cropToEdit = crop
                                showEditDialog = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Crop", tint = Color.Blue)
                            }
                            IconButton(onClick = {
                                // Remove from local or Firebase
                                if (firebaseCrops.contains(crop)) removeCropFromFirebase(crop)
                                else {
                                    crops.remove(crop)
                                    val allCrops = (crops + firebaseCrops)
                                    totalCount = allCrops.sumOf { it.count }
                                    averageCount = if (allCrops.isNotEmpty()) allCrops.map { it.count }.average() else 0.0
                                    maxCrop = allCrops.maxByOrNull { it.count }
                                    minCrop = allCrops.minByOrNull { it.count }
                                }
                            }) {
                                Icon(Icons.Default.Store, contentDescription = "Remove Crop", tint = Color.Red)
                            }
                        }
                    }
                }
            }
            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = Color.Red)
            }
        }
        if (showAddDialog) {
            Dialog(onDismissRequest = { showAddDialog = false }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 8.dp
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Add Crop", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = newCropName,
                            onValueChange = { newCropName = it },
                            label = { Text("Crop Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newCropCount,
                            onValueChange = { newCropCount = it },
                            label = { Text("Count") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newCropUnit,
                            onValueChange = { newCropUnit = it },
                            label = { Text("Unit (e.g. bales, bags, heads)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    val count = newCropCount.toIntOrNull() ?: 0
                                    if (newCropName.isNotBlank() && count > 0 && newCropUnit.isNotBlank()) {
                                        val crop = CropItem(newCropName, count, newCropUnit)
                                        addCropToFirebase(crop)
                                        showAddDialog = false
                                        newCropName = ""
                                        newCropCount = ""
                                        newCropUnit = ""
                                    }
                                },
                                enabled = newCropName.isNotBlank() && newCropCount.toIntOrNull() != null && newCropUnit.isNotBlank()
                            ) {
                                Text("Add")
                            }
                        }
                    }
                }
            }
        }
        if (showEditDialog && cropToEdit != null) {
            Dialog(onDismissRequest = { showEditDialog = false }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 8.dp
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Edit Crop", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = newCropName.takeIf { it.isNotBlank() } ?: cropToEdit!!.name,
                            onValueChange = { newCropName = it },
                            label = { Text("Crop Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newCropCount.takeIf { it.isNotBlank() } ?: cropToEdit!!.count.toString(),
                            onValueChange = { newCropCount = it },
                            label = { Text("Count") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newCropUnit.takeIf { it.isNotBlank() } ?: cropToEdit!!.unit,
                            onValueChange = { newCropUnit = it },
                            label = { Text("Unit (e.g. bales, bags, heads)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(onClick = { showEditDialog = false }) { Text("Cancel") }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    val count = newCropCount.toIntOrNull() ?: cropToEdit!!.count
                                    val editedCrop = CropItem(
                                        name = newCropName.takeIf { it.isNotBlank() } ?: cropToEdit!!.name,
                                        count = count,
                                        unit = newCropUnit.takeIf { it.isNotBlank() } ?: cropToEdit!!.unit
                                    )
                                    if (firebaseCrops.contains(cropToEdit)) {
                                        editCropInFirebase(cropToEdit!!, editedCrop)
                                    } else {
                                        crops = crops.map { if (it == cropToEdit) editedCrop else it }.toMutableList()
                                    }
                                    showEditDialog = false
                                    newCropName = ""
                                    newCropCount = ""
                                    newCropUnit = ""
                                },
                                enabled = (newCropName.isNotBlank() || newCropCount.isNotBlank() || newCropUnit.isNotBlank())
                            ) {
                                Text("Save")
                            }
                        }
                    }
                }
            }
        }
        if (showRequestDialog) {
            Dialog(onDismissRequest = { showRequestDialog = false }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 8.dp
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Request Inventory", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = newCropName,
                            onValueChange = { newCropName = it },
                            label = { Text("Crop Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newCropCount,
                            onValueChange = { newCropCount = it },
                            label = { Text("Amount Needed") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(onClick = { showRequestDialog = false }) { Text("Cancel") }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    // Simulate request (could store in Firebase or show a Toast)
                                    showRequestDialog = false
                                    errorMessage = "Request sent for $newCropCount of $newCropName!"
                                    newCropName = ""
                                    newCropCount = ""
                                },
                                enabled = newCropName.isNotBlank() && newCropCount.isNotBlank()
                            ) {
                                Text("Request")
                            }
                        }
                    }
                }
            }
        }
    }
}

data class CropItem(
    val name: String = "",
    val count: Int = 0,
    val unit: String = ""
)

data class MarketInventory(
    val marketName: String = "",
    val inventoryType: String = "",
    val countNeeded: Int = 0,
    val countSupplied: Int = 0,
    val supplierContact: String = ""
)

@Composable
fun MarketScreen(navController: NavHostController) {
    val context = LocalContext.current
    val firebaseDb = remember { FirebaseDatabase.getInstance().reference.child("market_inventory") }
    var marketInventories by remember { mutableStateOf(listOf(
        MarketInventory("Wakulima Market", "Maize", 100, 40, "+254712345678"),
        MarketInventory("Gikomba Market", "Beans", 80, 30, "+254734567890"),
        MarketInventory("Kongowea Market", "Fertilizer", 50, 20, "+254701234567"),
        MarketInventory("City Market", "Tomatoes", 120, 60, "+254798765432"),
        MarketInventory("Kibuye Market", "Cabbage", 90, 45, "+254722334455"),
        MarketInventory("Muthurwa Market", "Onion Sets", 70, 25, "+254733445566")
    )) }
    var firebaseInventories by remember { mutableStateOf(listOf<MarketInventory>()) }
    var showTenderDialog by remember { mutableStateOf(false) }
    var showContactDialog by remember { mutableStateOf(false) }
    var selectedMarket by remember { mutableStateOf<MarketInventory?>(null) }
    var tenderName by remember { mutableStateOf("") }
    var tenderContact by remember { mutableStateOf("") }
    var tenderAmount by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var newMarketName by remember { mutableStateOf("") }
    var newInventoryType by remember { mutableStateOf("") }
    var newCountNeeded by remember { mutableStateOf("") }
    var newCountSupplied by remember { mutableStateOf("") }
    var newSupplierContact by remember { mutableStateOf("") }

    // Listen for Firebase updates
    LaunchedEffect(Unit) {
        firebaseDb.get().addOnSuccessListener { snapshot ->
            val items = snapshot.children.mapNotNull { it.getValue(MarketInventory::class.java) }
            firebaseInventories = items
        }.addOnFailureListener {
            errorMessage = "Failed to load market inventory from Firebase."
        }
    }

    fun fileTenderForMarket(market: MarketInventory, name: String, contact: String, amount: String) {
        // Simulate tender application (could store in Firebase or show a Toast)
        showTenderDialog = false
        errorMessage = "Tender application sent to ${market.marketName} for $amount units by $name."
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                FloatingActionButton(onClick = { showAddDialog = true }, modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)) {
                    Icon(Icons.Default.Add, contentDescription = "Add Market Inventory")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Market Inventory", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            // Make market cards scrollable
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items((marketInventories + firebaseInventories).distinctBy { it.marketName + it.inventoryType }) { market ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(market.marketName, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                            Text("Inventory: ${market.inventoryType}", color = Color.Gray)
                            Text("Needed: ${market.countNeeded} | Supplied: ${market.countSupplied}", color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                Button(onClick = {
                                    selectedMarket = market
                                    showTenderDialog = true
                                }, modifier = Modifier.weight(1f)) {
                                    Text("File Tender")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = {
                                    selectedMarket = market
                                    showContactDialog = true
                                }, modifier = Modifier.weight(1f)) {
                                    Text("Contact Supplier")
                                }
                            }
                        }
                    }
                }
            }
            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = Color.Red)
            }
        }
        if (showTenderDialog && selectedMarket != null) {
            Dialog(onDismissRequest = { showTenderDialog = false }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 8.dp
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("File Tender for ${selectedMarket!!.marketName}", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = tenderName,
                            onValueChange = { tenderName = it },
                            label = { Text("Your Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = tenderContact,
                            onValueChange = { tenderContact = it },
                            label = { Text("Contact Info") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = tenderAmount,
                            onValueChange = { tenderAmount = it },
                            label = { Text("Amount to Supply") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(onClick = { showTenderDialog = false }) { Text("Cancel") }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    fileTenderForMarket(selectedMarket!!, tenderName, tenderContact, tenderAmount)
                                    tenderName = ""
                                    tenderContact = ""
                                    tenderAmount = ""
                                },
                                enabled = tenderName.isNotBlank() && tenderContact.isNotBlank() && tenderAmount.isNotBlank()
                            ) {
                                Text("Submit")
                            }
                        }
                    }
                }
            }
        }
        if (showContactDialog && selectedMarket != null) {
            Dialog(onDismissRequest = { showContactDialog = false }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 8.dp
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Supplier Contact for ${selectedMarket!!.marketName}", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Contact: ${selectedMarket!!.supplierContact}", fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { showContactDialog = false }, modifier = Modifier.align(Alignment.End)) {
                            Text("Close")
                        }
                    }
                }
            }
        }
        if (showAddDialog) {
            Dialog(onDismissRequest = { showAddDialog = false }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 8.dp
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Add Market Inventory", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = newMarketName,
                            onValueChange = { newMarketName = it },
                            label = { Text("Market Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newInventoryType,
                            onValueChange = { newInventoryType = it },
                            label = { Text("Inventory Type") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newCountNeeded,
                            onValueChange = { newCountNeeded = it },
                            label = { Text("Count Needed") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newCountSupplied,
                            onValueChange = { newCountSupplied = it },
                            label = { Text("Count Supplied") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newSupplierContact,
                            onValueChange = { newSupplierContact = it },
                            label = { Text("Supplier Contact") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (newMarketName.isNotBlank() && newInventoryType.isNotBlank() && newCountNeeded.isNotBlank() && newCountSupplied.isNotBlank() && newSupplierContact.isNotBlank()) {
                                        marketInventories = marketInventories + MarketInventory(
                                            newMarketName,
                                            newInventoryType,
                                            newCountNeeded.toIntOrNull() ?: 0,
                                            newCountSupplied.toIntOrNull() ?: 0,
                                            newSupplierContact
                                        )
                                        showAddDialog = false
                                        newMarketName = ""
                                        newInventoryType = ""
                                        newCountNeeded = ""
                                        newCountSupplied = ""
                                        newSupplierContact = ""
                                    }
                                },
                                enabled = newMarketName.isNotBlank() && newInventoryType.isNotBlank() && newCountNeeded.isNotBlank() && newCountSupplied.isNotBlank() && newSupplierContact.isNotBlank()
                            ) {
                                Text("Add")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController, onShowOfficers: (() -> Unit)? = null) {
    val context = LocalContext.current
    val items = listOf(
        NavItem("home", Icons.Default.Home, "Home"),
        NavItem("crops", Icons.Default.Agriculture, "Crops"),
        NavItem("market", Icons.Default.Store, "Market"),
        NavItem("support", Icons.Default.SupportAgent, "Extension Officers")
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
                    if (item.route == "support") {
                        onShowOfficers?.invoke()
                    } else if (currentRoute != item.route) {
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