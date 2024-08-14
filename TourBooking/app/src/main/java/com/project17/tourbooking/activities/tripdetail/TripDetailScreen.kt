package com.project17.tourbooking.activities.tripdetail

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project17.tourbooking.R
import com.project17.tourbooking.R.drawable.fuji_mountain
import com.project17.tourbooking.ui.theme.BlackDark900
import com.project17.tourbooking.ui.theme.BlackLight100
import com.project17.tourbooking.ui.theme.BlackLight300
import com.project17.tourbooking.ui.theme.BlackLight400
import com.project17.tourbooking.ui.theme.BlackWhite0
import com.project17.tourbooking.ui.theme.BrandDefault500
import com.project17.tourbooking.ui.theme.ErrorDefault500
import com.project17.tourbooking.ui.theme.TourBookingTheme
import com.project17.tourbooking.ui.theme.Typography
import com.project17.tourbooking.utils.AddToWishList
import com.project17.tourbooking.utils.Tour
import com.project17.tourbooking.utils.addedWishListIcon3x
import com.project17.tourbooking.utils.iconWithoutBackgroundModifier
import com.project17.tourbooking.utils.toAddWishListIcon3x

class TripDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TourBookingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TripDetailScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TripDetailScreen(modifier: Modifier = Modifier) {
    var isScrolled by remember {
        mutableStateOf(false)
    }
    val tour = Tour("Fuji Mountain", fuji_mountain, 4.5, "Japan", 245.0,true)
    val backgroundModifier = if(!isScrolled){
        Modifier.paint(painterResource(id = tour.image), contentScale = ContentScale.Crop)
    }
    else{
        Modifier
    }
    Column(
        Modifier
            .fillMaxSize()
            .then(backgroundModifier)

    ){
        NavBarSection(tour, isScrolled)
        TourSummarySection(tour, Modifier.weight(1f))
        Box(){
            FooterSection(tour = tour, isScrolled = isScrolled, modifier = Modifier.align(Alignment.BottomStart))
        }
    }
}


@Composable
fun NavBarSection(
    tour: Tour,
    isScrolled: Boolean = false
){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = "",
            tint = if(isScrolled)
                 BlackDark900
            else
                BlackWhite0,
            modifier = Modifier.padding(start = 8.dp)
        )
        val context = LocalContext.current
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
fun TourSummarySection(
    tour: Tour,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Column(
            Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Bottom
        ) {
            if(tour.isAddedToWishList){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = stringResource(id = R.string.favorite_place_text),
                        style = Typography.titleLarge,
                        color = BlackWhite0
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_wishlist_verify),
                        contentDescription = ""
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
            Text(
                text = tour.name,
                style = Typography.headlineLarge,
                color = BlackWhite0
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = "",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = tour.location,
                    style = Typography.bodyLarge,
                    color = BlackWhite0
                )
            }
            Spacer(Modifier.height(16.dp))
            Row{
                Text(
                    text = "100+ ",
                    color = BlackWhite0,
                    style = Typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "people have explored",
                    color = BlackLight100,
                    style = Typography.bodyLarge
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.sample_text),
                style = Typography.bodySmall,
                color = BlackWhite0,
                textAlign = TextAlign.Left
            )
            Spacer(Modifier.height(32.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(id = R.drawable.ic_yellow_star_3x),
                    contentDescription = ""
                )

                Text(
                    text = tour.rate.toString(),
                    style = Typography.headlineMedium,
                    color = BlackWhite0
                )
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.ic_scroll_down_arrow_3x),
                    contentDescription = "",
                    tint = BlackWhite0
                )
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun FooterSection(tour: Tour, isScrolled: Boolean = false,  modifier: Modifier){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(if (isScrolled) BlackWhite0 else Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ){
        Row{
            Text(
                text = String.format("$%.2f", tour.price),
                style = Typography.headlineMedium,
                color = if(isScrolled) ErrorDefault500 else BlackWhite0
            )

            Text(
                text = " /Person",
                color = if(isScrolled) BlackLight400 else BlackLight300,
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

@Preview(showBackground = true)
@Composable
fun TripDetailPreview() {
    TourBookingTheme {
        TripDetailScreen()
    }
}