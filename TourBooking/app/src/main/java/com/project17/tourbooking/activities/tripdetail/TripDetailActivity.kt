package com.project17.tourbooking.activities.tripdetail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project17.tourbooking.R
import com.project17.tourbooking.R.drawable.fuji_mountain
import com.project17.tourbooking.models.Review
import com.project17.tourbooking.ui.theme.BlackDark900
import com.project17.tourbooking.ui.theme.BlackLight400
import com.project17.tourbooking.ui.theme.BlackWhite0
import com.project17.tourbooking.ui.theme.BrandDefault500
import com.project17.tourbooking.ui.theme.ErrorDefault500
import com.project17.tourbooking.ui.theme.TourBookingTheme
import com.project17.tourbooking.ui.theme.Typography
import com.project17.tourbooking.utils.AddToWishList
import com.project17.tourbooking.utils.Category
import com.project17.tourbooking.utils.CategoryItem
import com.project17.tourbooking.utils.ReviewItem
import com.project17.tourbooking.utils.Tour
import com.project17.tourbooking.utils.TourSummaryCard
import com.project17.tourbooking.utils.addedWishListIcon3x
import com.project17.tourbooking.utils.iconWithoutBackgroundModifier
import com.project17.tourbooking.utils.toAddWishListIcon3x

class TripDetailActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TourBookingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    TripDetailScreen()
                }
            }
        }
    }
}

@Composable
fun TripDetailScreen(navController: NavHostController = rememberNavController(), tourId: String = "") {
    var tour = Tour("Fuji Mountain", fuji_mountain, 4.5, "Japan", 245.0,true)

    if(!tourId.equals("")){
    }
    Box(
        Modifier
            .fillMaxSize()
    ){
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ){
            NavBarSection(tour)
            Spacer(modifier = Modifier.height(32.dp))
            TourSummaryCard(tour = tour)
            Spacer(modifier = Modifier.height(32.dp))
            CategoryListSection(tour = tour)
            Spacer(modifier = Modifier.height(32.dp))
            AboutTripSection(tour = tour)
            Spacer(modifier = Modifier.height(32.dp))
            ReviewSection(tour = tour)
        }
        FooterSection(tour = tour, modifier = Modifier.align(Alignment.BottomStart))
    }
}

@Composable
fun CategoryListSection(tour: Tour){
    val categories = listOf(
        Category(R.string.discovery_category_name_text, R.drawable.ic_discovery),
        Category(R.string.discovery_category_name_text, R.drawable.ic_discovery))
    Column {
        Text(
            text = stringResource(R.string.category_text),
            style = Typography.headlineMedium,)
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(Modifier.fillMaxWidth()){
            items(categories){
                    category ->
                CategoryItem(category = category, onClick = {})
            }
        }

    }
}

@Composable
fun AboutTripSection(tour: Tour){
    Column {
        Text(
            text = stringResource(R.string.about_trip_text),
            style = Typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.sample_text),
            style = Typography.bodyLarge,
        )
    }
}

@Composable
fun ReviewSection(tour: Tour){
    val reviews = listOf(
        Review(5, "", accountId = "1", tourId = "1" ),
        Review(4, "", accountId = "1", tourId = "1" ),
        Review(3, "", accountId = "1", tourId = "1" ),
        Review(1, "", accountId = "1", tourId = "1" )
    )
    Column {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
            Text(
                text = stringResource(R.string.review_text, 99),
                style = Typography.headlineMedium,
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.ic_yellow_star_3x),
                contentDescription = stringResource(
                    id = R.string.image_description_text)
            )
            Text(
                text = String.format("%.1f", tour.rate),
                style = Typography.headlineSmall
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(Modifier.height(if(reviews.size == 0) 0.dp else 300.dp)) {
            items(reviews){
                review ->
                ReviewItem(review = review)
            }
        }
    }
}

@Composable
fun NavBarSection(
    tour: Tour
){
    Row(
        Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "",
            tint = BlackDark900,
            modifier = Modifier.padding(start = 8.dp)
        )
        val context = LocalContext.current
        Text(
            //TODO: get tour name and put here
            text = tour.name,
            style = Typography.titleLarge,
            color = BlackDark900,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        AddToWishList(
            tour = tour,
            addedIcon = addedWishListIcon3x,
            toAddIcon = toAddWishListIcon3x,
            context = context,
            modifier = Modifier
                .iconWithoutBackgroundModifier()
        )
    }

}


@Composable
fun FooterSection(tour: Tour,  modifier: Modifier){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BlackWhite0),
        verticalAlignment = Alignment.CenterVertically
    ){
        Row(Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically){
            Row{
                Text(
                    text = String.format("$%.2f", tour.price),
                    style = Typography.headlineMedium,
                    color = ErrorDefault500
                )

                Text(
                    text = " /Person",
                    color = BlackLight400,
                    style = Typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { /*TODO*/ },
                colors = ButtonColors(BrandDefault500, BlackDark900, BrandDefault500, BlackDark900),
                modifier = Modifier
                    .width(150.dp)
            ){
                Text(
                    text = stringResource(id = R.string.booking_button_text),
                    style = Typography.titleLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun TripDetailPreview() {
    TourBookingTheme {
        TripDetailScreen()
    }
}