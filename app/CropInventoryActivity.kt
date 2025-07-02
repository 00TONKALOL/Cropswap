import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Crop(val name: String, val stock: Int, val fertilizers: List<String>)

class CropsInventoryActivity : ComponentActivity() {
    private val crops = listOf(
        Crop("Tomatoes", 120, listOf("DAP", "CAN", "Compost")),
        Crop("Cabbage", 90, listOf("NPK 17:17:17", "Manure")),
        Crop("Zucchini", 60, listOf("Urea", "Compost")),
        Crop("Grapes", 40, listOf("Superphosphate", "Potassium Sulfate")),
        Crop("Spinach", 150, listOf("Ammonium Nitrate", "Organic mulch"))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CROPNOVATheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CropList(crops)
                }
            }
        }
    }
}

@Composable
fun CropList(crops: List<Crop>) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(crops) { crop ->
            var showFertilizers by remember { mutableStateOf(false) }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(crop.name, style = MaterialTheme.typography.titleMedium)
                    Text("Stock Available: ${crop.stock} units")

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = { showFertilizers = !showFertilizers }) {
                        Text(if (showFertilizers) "Hide Fertilizers" else "Show Fertilizers")
                    }

                    if (showFertilizers) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Required Fertilizers:", style = MaterialTheme.typography.bodyMedium)
                        crop.fertilizers.forEach {
                            Text("- $it", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
