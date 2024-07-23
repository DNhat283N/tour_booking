package com.project17.tourbooking.activities.home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.project17.tourbooking.R
import com.project17.tourbooking.ui.theme.BlackDefault500
import com.project17.tourbooking.ui.theme.BlackLight100
import com.project17.tourbooking.ui.theme.BlackLight200
import com.project17.tourbooking.ui.theme.BlackLight300
import com.project17.tourbooking.ui.theme.BlackLight400
import com.project17.tourbooking.ui.theme.BlackWhite0
import com.project17.tourbooking.ui.theme.TourBookingTheme
import com.project17.tourbooking.ui.theme.Typography
import com.project17.tourbooking.utils.Category
import com.project17.tourbooking.utils.Trip

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TourBookingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var inputText by remember {mutableStateOf("")}
    var isActive by remember {mutableStateOf(false)}

    Column(modifier = modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(top = 25.dp, start = 16.dp, end = 16.dp)
        .verticalScroll(rememberScrollState())) {
        AvatarWithWelcomeUserTextAndNotificationIcon()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.home_question_text),
            modifier = Modifier
                .fillMaxWidth(),
            style = Typography.headlineMedium,
            maxLines = 2,
            overflow = TextOverflow.Clip
        )
        Spacer(modifier = Modifier.height(16.dp))
        SearchBar(
            query = inputText,
            onQueryChange ={
                inputText = it
            },
            onSearch = {
                isActive = false
            },
            active = isActive,
            onActiveChange = {
                isActive = it
            },
            colors = SearchBarDefaults.colors(
                containerColor = BlackLight100
            ),
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.search_hint_text),
                    style = Typography.bodyMedium,
                    color = BlackLight400
                )
            },
            leadingIcon = {
                if(isActive){
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.search_icon_description_text),
                        modifier = Modifier.clickable(onClick = {}),
                        tint = Color.Black)
                }
            },
            trailingIcon = {
                if(isActive){
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(id = R.string.search_icon_description_text),
                        modifier = Modifier.clickable(onClick = {
                            if(inputText.isNotEmpty()){
                                inputText = ""
                            }
                            else {
                                isActive = false
                            }
                        }),
                        tint = BlackDefault500)
                }
                else{
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.search_icon_description_text),
                        modifier = Modifier.clickable(onClick = {
                            isActive = true
                        }),
                        tint = Color.Black)
                }
            }
        ) {
            //Add history to recommend on search
        }
        Spacer(modifier = Modifier
            .height(25.dp)
            .fillMaxWidth())
        ChooseCategoryBLock()
        Spacer(modifier = Modifier
            .height(25.dp)
            .fillMaxWidth())
        FavoritePlaceBlock()
    }
}

@Composable
fun AvatarWithWelcomeUserTextAndNotificationIcon(
    modifier: Modifier = Modifier,
    hasUnreadNotification: Boolean = false
) {
    val context = LocalContext.current
    Row(modifier = modifier
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically)
    {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = R.drawable.default_avatar,
                contentDescription = stringResource(id = R.string.avatar_description_text),
                placeholder = painterResource(id = R.drawable.default_avatar),
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .clickable(onClick = {
                        Toast
                            .makeText(context, "Navigate to Profile Activity", Toast.LENGTH_SHORT)
                            .show()
                    }))

            Text(
                text = stringResource(id = R.string.welcome_text),
                modifier = Modifier.padding(start = 10.dp),
                style = Typography.titleMedium)
        }

        if(hasUnreadNotification){
            BadgedBox(
                badge = {
                    Badge()
                },
                modifier = Modifier.wrapContentSize()
            ){
                Icon(
                    painter = painterResource(id = R.drawable.ic_notification),
                    contentDescription = stringResource(id = R.string.icon_notification_description_text),
                    modifier = Modifier.clickable( onClick = {
                        //Navigate to NotificationActivity
                    })
                )
            }
        }
        else{
            Icon(
                painter = painterResource(id = R.drawable.ic_notification),
                contentDescription = stringResource(id = R.string.icon_notification_description_text),
                modifier = Modifier.clickable( onClick = {
                    Toast.makeText(context, "Navigate to NotificationActivity", Toast.LENGTH_SHORT).show()
                })
            )
        }
    }
}

@Composable
fun ChooseCategoryBLock(modifier: Modifier = Modifier){
    val categories = listOf(
        Category(R.string.discovery_category_name_text, R.drawable.ic_discovery),
        Category(R.string.discovery_category_name_text, R.drawable.ic_discovery),
        Category(R.string.discovery_category_name_text, R.drawable.ic_discovery),
        Category(R.string.discovery_category_name_text, R.drawable.ic_discovery),
        Category(R.string.discovery_category_name_text, R.drawable.ic_discovery),
        Category(R.string.discovery_category_name_text, R.drawable.ic_discovery),
        Category(R.string.discovery_category_name_text, R.drawable.ic_discovery)
    )
    val context = LocalContext.current
    Column(
        modifier = modifier
        .fillMaxWidth()){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.choose_category_text),
                style = Typography.headlineMedium
                )
            Text(
                text = stringResource(id = R.string.see_all_text),
                style = Typography.labelSmall,
                color = BlackLight300,
                modifier = Modifier.clickable(onClick = {
                    Toast.makeText(context, "See All text", Toast.LENGTH_SHORT).show()
                })
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
           items(categories){ category ->
               Box(
                   modifier = Modifier
                       .background(
                           color = BlackWhite0,
                           shape = RoundedCornerShape(16.dp)
                       )
                       .border(
                           width = 1.dp,
                           color = BlackLight200,
                           shape = RoundedCornerShape(16.dp)
                       )
                       .padding(4.dp)
               ){
                   Row(
                       modifier = Modifier
                           .fillMaxSize()
                           .clickable(onClick = {
                               Toast
                                   .makeText(
                                       context,
                                       "Navigate to Search Activity with Category",
                                       Toast.LENGTH_SHORT
                                   )
                                   .show()
                           }),
                       verticalAlignment = Alignment.CenterVertically
                   ) {
                       Image(
                           painter = painterResource(id = category.icon),
                           contentDescription = "",
                           modifier = Modifier
                               .background(
                                   color = BlackLight100,
                                   shape = RoundedCornerShape(16.dp)
                               )
                               .fillMaxSize()

                       )
                       Spacer(modifier = Modifier.width(8.dp))
                       Text(
                           text = stringResource(id = category.name),
                           style = Typography.titleLarge
                       )
                       Spacer(modifier = Modifier.width(8.dp))
                   }
               }
               Spacer(modifier = Modifier.width(5.dp))
           }
        }
    }
}

@Composable
fun FavoritePlaceBlock(modifier: Modifier = Modifier) {
    val trips = remember {
        mutableStateListOf(
            Trip("Fuji Mountain", R.drawable.fuji_mountain, 4.5, "Japan"),
            Trip("Fuji Mountain", R.drawable.fuji_mountain, 4.5, "Japan"),
            Trip("Fuji Mountain", R.drawable.fuji_mountain, 4.5, "Japan"),
            Trip("Fuji Mountain", R.drawable.fuji_mountain, 4.5, "Japan"),
            Trip("Fuji Mountain", R.drawable.fuji_mountain, 4.5, "Japan")
        )
    }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.favorite_place_text),
                style = Typography.headlineMedium
            )
            Text(
                text = stringResource(id = R.string.explore_text),
                style = Typography.labelSmall,
                color = BlackLight300,
                modifier = Modifier.clickable(onClick = {
                    Toast.makeText(context, "Explore text", Toast.LENGTH_SHORT).show()
                })
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            items(trips){ trip ->
                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .height(200.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable(onClick = {
                            Toast
                                .makeText(
                                    context,
                                    "Navigate to Trip Detail Activity",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        })
                ){
                    Image(
                        painter = painterResource(id = R.drawable.fuji_mountain),
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                    )
                    var isWishlisted by remember {
                        mutableStateOf(trip.isAddedToWishList)
                    }
                    Icon(
                        painter = painterResource(
                            id =
                            if(isWishlisted)
                                R.drawable.ic_added_to_wishlist
                            else
                                R.drawable.ic_to_add_to_wishlist),
                        tint =
                        if(isWishlisted)
                            Color.Red
                        else
                            Color.Unspecified,
                        contentDescription = "Favorite",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .clip(CircleShape)
                            .background(BlackWhite0)
                            .padding(8.dp)
                            .clickable(onClick = {
                                isWishlisted = !isWishlisted
                                trip.isAddedToWishList = isWishlisted
                                Toast
                                    .makeText(
                                        context,
                                        if (trip.isAddedToWishList) "Added to Wishlist" else "Removed from Wishlist",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            })
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Bottom
                    ){
                        Text(
                            text = trip.name,
                            style = Typography.titleLarge,
                            color = BlackWhite0
                        )
                        Spacer(modifier = Modifier.height(4.dp))
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
                                text = trip.location,
                                style = Typography.bodyMedium,
                                color = BlackWhite0
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        GenerateStarFromRating(rating = trip.rate)
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Composable
fun GenerateStarFromRating(
    rating: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        if(rating.toInt() == 5){
            repeat(5) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_yellow_star),
                    contentDescription = ""
                )
            }
        }
        else{
            val filledStars = 5 - rating.toInt()
            repeat(rating.toInt()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_yellow_star),
                    contentDescription = ""
                )
            }
            repeat(filledStars){
                Image(
                    painter = painterResource(id = R.drawable.ic_white_star),
                    contentDescription = ""
                )
            }
        }
        Text(
            text = rating.toString(),
            style = Typography.bodyMedium,
            color = BlackWhite0
        )
    }
}


@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreen()
}