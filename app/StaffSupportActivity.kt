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

data class Officer(val name: String, val role: String, val phone: String)

class StaffSupportActivity : ComponentActivity() {
    private val officers = listOf(
        Officer("James Kariuki", "Tender Officer", "+254712345678"),
        Officer("Alice Wambui", "Extension Officer", "+254734567890"),
        Officer("John Otieno", "Tender Officer", "+254701234567"),
        Officer("Grace Mwikali", "Extension Officer", "+254798765432")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CROPNOVATheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    OfficerList(officers)
                }
            }
        }
    }
}

@Composable
fun OfficerList(officers: List<Officer>) {
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
                    Text("Contact: ${officer.phone}")

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${officer.phone}")
                        }
                        it.context.startActivity(intent)
                    }) {
                        Text("Contact Officer")
                    }
                }
            }
        }
    }
}
