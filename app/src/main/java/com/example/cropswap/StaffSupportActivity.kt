import android.os.Bundle
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cropswap.ui.theme.CROPSWAPTheme
import androidx.core.net.toUri
import com.example.cropswap.WelcomeActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import com.google.firebase.database.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.example.cropswap.MainActivity

data class Officer(val name: String, val role: String, val phone: String)

class StaffSupportActivity : ComponentActivity() {
    private val officers = listOf(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CROPSWAPTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column {
                        Button(
                            onClick = {
                                startActivity(Intent(this@StaffSupportActivity, WelcomeActivity::class.java))
                                finish()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text("Back to Welcome")
                        }
                        OfficerList(officers)
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

@Composable
fun OfficerList(officers: List<Officer>) {
    val context = LocalContext.current
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(officers) { officer ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(officer.name, style = MaterialTheme.typography.titleMedium)
                    Text(officer.role, color = MaterialTheme.colorScheme.primary)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Contact: ${officer.phone}")
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${officer.phone}")
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
}
