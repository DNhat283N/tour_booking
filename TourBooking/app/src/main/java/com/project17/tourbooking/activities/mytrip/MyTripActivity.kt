import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.project17.tourbooking.activities.search.SearchBarSection
import com.project17.tourbooking.activities.search.SearchViewModel
import com.project17.tourbooking.ui.theme.Typography
import com.project17.tourbooking.R

data class Trip(val name: String, val price: String, val startDate: String, val duration: String)

@Composable
fun MyTripScreen(searchViewModel: SearchViewModel = viewModel(), navController: NavController) {
    val trips = remember {
        listOf(
            Trip("Trip to Ha Long Bay", "$500", "2024-09-15", "3 days"),
            Trip("Explore Sapa", "$300", "2024-10-01", "2 days"),
            Trip("Adventure in Mekong Delta", "$400", "2024-11-10", "4 days"),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "List Your Trip",
            style = Typography.headlineLarge,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        SearchBarSection(searchViewModel)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(trips) { trip ->
                TripItem(trip, navController)
            }
        }
    }
}

@Composable
fun TripItem(trip: Trip, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                navController.navigate("login")
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = trip.name, style = Typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = trip.price, style = Typography.bodyLarge, color = Color(0xFFFFA500))
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = "Calendar",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Start Date: ${trip.startDate}", style = Typography.bodyMedium, color = Color.Gray)
            }
            Text(text = "Duration: ${trip.duration}", style = Typography.bodyMedium, color = Color.Gray)
        }
    }
}