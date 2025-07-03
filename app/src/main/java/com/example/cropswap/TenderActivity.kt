package com.example.cropswap

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.window.Dialog
import com.example.cropswap.ui.theme.CROPSWAPTheme

class TenderActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CROPSWAPTheme {
                TenderScreen(onBackToWelcome = {
                    startActivity(Intent(this, WelcomeActivity::class.java))
                    finish()
                })
            }
        }
    }
}

data class Tender(
    val county: String,
    val item: String,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TenderScreen(onBackToWelcome: () -> Unit) {
    val context = LocalContext.current
    val tenders = remember {
        listOf(
            Tender("Kiambu", "Fertilizer - NPK", "Supply of 200 bags for maize farms."),
            Tender("Mombasa", "Tomato Seeds", "Hybrid seed distribution for coastal climate."),
            Tender("Nakuru", "D.A.P Fertilizer", "Support for large-scale wheat farmers."),
            Tender("Busia", "Onion Sets", "Procurement for dry season planting."),
            Tender("Embu", "Pesticides", "For pest control in coffee plantations."),
            Tender("Meru", "Cabbage Seeds", "Highland climate suitable hybrid seeds."),
            Tender("Narok", "Wheat Fertilizer", "Subsidized tender for Narok farmers."),
            Tender("Kakamega", "Bean Seeds", "Drought-resistant variety required."),
            Tender("Kilifi", "Drip Irrigation Kits", "Installation for mango farms."),
            Tender("Uasin Gishu", "Tractor Services", "Mechanical plowing for maize farms."),
            Tender("Kirinyaga", "Greenhouse Tunnels", "Tender for 10 tunnel installations."),
            Tender("Baringo", "Sorghum Seeds", "Resilient seeds for semi-arid zones.")
        )
    }

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val filteredTenders = tenders.filter {
        it.county.contains(searchQuery.text, ignoreCase = true) ||
        it.item.contains(searchQuery.text, ignoreCase = true)
    }

    var selectedTender by remember { mutableStateOf<Tender?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column {
                Button(
                    onClick = onBackToWelcome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text("Back to Welcome")
                }
                TopAppBar(
                    title = { Text("Tender Opportunities") },
                    actions = {
                        TextButton(onClick = {
                            context.startActivity(Intent(context, MainActivity::class.java))
                            (context as? Activity)?.finish()
                        }) {
                            Text("Home", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                )
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    placeholder = { Text("Search by county or item") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        },
        bottomBar = {
            Button(
                onClick = {
                    Toast.makeText(
                        context,
                        "Tender application submitted to the Ministry of Agriculture.",
                        Toast.LENGTH_LONG
                    ).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Submit Application")
            }
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(filteredTenders) { tender ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            selectedTender = tender
                            showDialog = true
                        },
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${tender.item} - ${tender.county}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = tender.description,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
        // Registration Dialog
        if (showDialog && selectedTender != null) {
            RegistrationDialog(
                tender = selectedTender!!,
                onDismiss = { showDialog = false },
                onRegister = { name, contact ->
                    showDialog = false
                    showConfirmation = true
                    // Simulate saving to DB (show Toast)
                    Toast.makeText(
                        context,
                        "Registered $name for ${selectedTender!!.item} in ${selectedTender!!.county}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            )
        }
        // Confirmation Dialog
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
fun RegistrationDialog(
    tender: Tender,
    onDismiss: () -> Unit,
    onRegister: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Apply for ${tender.item} - ${tender.county}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(tender.description, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = contact,
                    onValueChange = { contact = it },
                    label = { Text("Contact (Phone/Email)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (name.isNotBlank() && contact.isNotBlank()) {
                                onRegister(name, contact)
                            }
                        },
                        enabled = name.isNotBlank() && contact.isNotBlank()
                    ) {
                        Text("Register")
                    }
                }
            }
        }
    }
}
