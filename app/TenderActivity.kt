package com.example.cropswap

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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

class TenderActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CROPNOVATheme {
                TenderScreen()
            }
        }
    }
}

data class Tender(
    val county: String,
    val item: String,
    val description: String
)

@Composable
fun TenderScreen() {
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

    Scaffold(
        topBar = {
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
            items(tenders) { tender ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
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
    }
}
