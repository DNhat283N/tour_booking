package com.project17.tourbooking.activities.home

import android.content.Context
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.project17.tourbooking.R
import com.project17.tourbooking.navigates.NavigationGraph
import com.project17.tourbooking.navigates.NavigationItems
import com.project17.tourbooking.utils.iconWithBackgroundModifier
import com.project17.tourbooking.ui.theme.BlackDefault500
import com.project17.tourbooking.ui.theme.BlackLight100
import com.project17.tourbooking.ui.theme.BlackLight200
import com.project17.tourbooking.ui.theme.BlackLight300
import com.project17.tourbooking.ui.theme.BlackLight400
import com.project17.tourbooking.ui.theme.BlackWhite0
import com.project17.tourbooking.ui.theme.ErrorDark600
import com.project17.tourbooking.ui.theme.TourBookingTheme
import com.project17.tourbooking.ui.theme.Typography
import com.project17.tourbooking.utils.AddToWishList
import com.project17.tourbooking.utils.Category
import com.project17.tourbooking.utils.TourPackage
import com.project17.tourbooking.utils.Tour

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

@Composable
fun HomeScreen(navController: NavHostController = rememberNavController(), modifier: Modifier = Modifier) {
    var inputText by remember {mutableStateOf("")}
    var isActive by remember {mutableStateOf(false)}

    Column(modifier = modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(
            top = 25.dp,
            start = 16.dp,
            end = 16.dp
        )
        .verticalScroll(rememberScrollState())) {
        HeaderSection()
        Spacer(modifier = Modifier.height(16.dp))
        SearchBarSection(
            inputText = inputText,
            isActive = isActive,
            onQueryChange = { inputText = it },
            onSearch = { isActive = false },
            onActiveChange = { isActive = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier
            .height(25.dp)
            .fillMaxWidth())
        ChooseCategorySection()
        Spacer(modifier = Modifier
            .height(25.dp)
            .fillMaxWidth())
        FavoritePlaceSection(navController = navController)
        Spacer(modifier = Modifier
            .height(25.dp)
            .fillMaxWidth())
        PopularPackageSection()
    }
}

@Composable
fun HeaderSection(
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
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = stringResource(id = R.string.home_question_text),
        modifier = Modifier
            .fillMaxWidth(),
        style = Typography.headlineMedium,
        maxLines = 2,
        overflow = TextOverflow.Clip
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarSection(
    inputText:String,
    isActive: Boolean,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
){
    SearchBar(
        query = inputText,
        onQueryChange =onQueryChange,
        onSearch = { onSearch() },
        active = isActive,
        onActiveChange = onActiveChange,
        colors = SearchBarDefaults.colors(
            containerColor = BlackLight100
        ),
        modifier = modifier.fillMaxWidth(),
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
                            onQueryChange("")
                        }
                        else {
                            onActiveChange(false)
                        }
                    }),
                    tint = BlackDefault500)
            }
            else{
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search_icon_description_text),
                    modifier = Modifier.clickable(onClick = {
                        onActiveChange(true)
                    }),
                    tint = Color.Black)
            }
        }
    ) {
        //Add history to recommend on search
    }

}

@Composable
fun ChooseCategorySection(modifier: Modifier = Modifier){
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
    var isSeeAllClicked by remember{
        mutableStateOf(false)
    }
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
                text = if(!isSeeAllClicked) stringResource(id = R.string.see_all_text)
                else stringResource(id = R.string.collapse_text),
                style = Typography.labelSmall,
                color = BlackLight300,
                modifier = Modifier.clickable(onClick = {
                    isSeeAllClicked = !isSeeAllClicked
                })
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        if(!isSeeAllClicked){
            LazyRow {
                items(categories){ category ->
                    CategoryItem(category = category, context = context, imageModifier = Modifier.fillMaxSize())
                }
            }
        }
        else{
            CategoryGrid(items = categories, columns = 2 )
        }
    }
}

@Composable
fun CategoryGrid(items: List<Category>, columns: Int) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        items.chunked(columns).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val itemWeight = 1f / columns
                rowItems.forEach { item ->
                    Surface(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .weight(itemWeight)
                    ) {
                        CategoryItem(category = item)
                    }
                }
                repeat(columns - rowItems.size){
                    Spacer(modifier = Modifier.weight(itemWeight))
                }
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    context: Context = LocalContext.current,
    imageModifier: Modifier = Modifier
){
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
                    // TODO: Navigate to Search Activity with Category
                }),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = category.icon),
                contentDescription = "",
                modifier = imageModifier
                    .background(
                        color = BlackLight100,
                        shape = RoundedCornerShape(16.dp)
                    )
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

@Composable
fun FavoritePlaceSection(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val tours = remember {
        mutableStateListOf(
            Tour("Fuji Mountain", R.drawable.fuji_mountain, 4.5, "Japan",4.5,  true),
            Tour("Fuji Mountain", R.drawable.fuji_mountain, 4.5, "Japan",6.8,  true),
            Tour("Fuji Mountain", R.drawable.fuji_mountain, 4.5, "Japan",8.7,  true),
            Tour("Fuji Mountain", R.drawable.fuji_mountain, 4.5, "Japan",0.5,  true),
            Tour("Fuji Mountain", R.drawable.fuji_mountain, 4.5, "Japan")
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
                    navController.navigate(NavigationItems.WishList.route)
                })
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            items(tours){ trip ->
                if(trip.isAddedToWishList){
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
                                // TODO: Navigate to Trip Detail Activity
                                navController.navigate(NavigationItems.TripDetail.route)
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
                        AddToWishList(
                            tour = trip,
                            context = context,
                            modifier = Modifier
                                .iconWithBackgroundModifier()
                                .align(Alignment.TopEnd)

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
}


@Composable
fun GenerateStarFromRating(
    rating: Double,
    textColor: Color = BlackWhite0
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        if(rating.toInt() == 5){
            repeat(5) {
                Image(
                    painter = painterResource(id = R.drawable.ic_yellow_star),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
        else{
            val filledStars = 5 - rating.toInt()
            repeat(rating.toInt()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_yellow_star),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(4.dp))

            }
            repeat(filledStars){
                Image(
                    painter = painterResource(id = R.drawable.ic_white_star),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(4.dp))

            }
        }
        Text(
            text = rating.toString(),
            style = Typography.titleMedium,
            color = textColor
        )
    }
}

@Composable
fun PopularPackageSection(modifier: Modifier = Modifier) {
    val packages = remember {
        mutableStateListOf(
            TourPackage("Kuta Resort", R.drawable.kuta_resort, 250.0, 4.5),
            TourPackage("Kuta Resort", R.drawable.kuta_resort, 250.0, 4.5),
            TourPackage("Kuta Resort", R.drawable.kuta_resort, 250.0, 4.5),
            TourPackage("Kuta Resort", R.drawable.kuta_resort, 250.0, 4.5)
        )
    }
    var isSeeAllClicked by remember{
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.popular_package_text),
                style = Typography.headlineMedium
            )
            Text(
                text = if(!isSeeAllClicked) stringResource(id = R.string.see_all_text)
                else stringResource(id = R.string.collapse_text),
                style = Typography.labelSmall,
                color = BlackLight300,
                modifier = Modifier.clickable(onClick = {
                    isSeeAllClicked = !isSeeAllClicked
                })
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .wrapContentHeight()
        ) {
            if(!isSeeAllClicked){
                items(packages.take(1)) { item ->
                    ResortCard(tourPackage = item)
                }
            }
            else{
                items(packages) { item ->
                    ResortCard(tourPackage = item)
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun ResortCard(
    tourPackage: TourPackage
               ) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
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
                // TODO: Navigate to Trip Detail Activity
            })
            .border(
                width = 1.dp,
                color = BlackLight200,
                shape = RoundedCornerShape(16.dp)
            )
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(BlackWhite0)
                .padding(8.dp)
        ){
            Image(
                painter = painterResource(tourPackage.image),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxSize()
                    .weight(2f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .weight(3f)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BlackWhite0)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = tourPackage.name,
                        style = Typography.titleLarge,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = String.format("$%.2f", (tourPackage.price ?: 0.0)),
                        style = Typography.bodyLarge,
                        color = ErrorDark600
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    GenerateStarFromRating(rating = tourPackage.rate, Color.Black)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = tourPackage.description,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = BlackLight400,
                        style = Typography.bodyMedium,
                        lineHeight = 24.sp
                    )
                }
                AddToWishList(
                    tour = tourPackage,
                    context = context,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp))
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Suppress("VisualLintBounds")
@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreen()
}