package com.project17.tourbooking.activities.search

import android.annotation.SuppressLint
import android.os.Bundle
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.project17.tourbooking.R
import com.project17.tourbooking.ui.theme.BlackDark900
import com.project17.tourbooking.ui.theme.BlackLight100
import com.project17.tourbooking.ui.theme.BlackLight200
import com.project17.tourbooking.ui.theme.BlackWhite0
import com.project17.tourbooking.ui.theme.BrandDefault500
import com.project17.tourbooking.ui.theme.SuccessDefault500
import com.project17.tourbooking.ui.theme.TourBookingTheme
import com.project17.tourbooking.ui.theme.Typography
import com.project17.tourbooking.viewmodels.AppViewModel

class SearchFilterActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TourBookingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    SearchFilterScreen()
                }
            }
        }
    }
}

const val MIN_PRICE = 0f
const val MAX_PRICE = 7000f

@Composable
fun SearchFilterScreen(navController: NavHostController = rememberNavController(), appViewModel: AppViewModel = viewModel()){
    val searchViewModel: SearchViewModel = viewModel()
    val range = remember {
        mutableStateOf(100f..7000f)
    }
    var rateMoreThan by remember {
        mutableIntStateOf(-1)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(BlackLight100)
    ){
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
            )

            SearchFilterHeaderSection(navController = navController, viewModel = searchViewModel)

            Spacer(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
            )

            RangePriceFilterSection(range = range)

            Spacer(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
            )

            StarReviewFilterSection(selectedItem = rateMoreThan, onItemSelected = {
                newItem -> rateMoreThan = newItem
            })
        }
        SearchFilterFooterSection(
            modifier = Modifier.align(Alignment.BottomStart),
            onClearAllButtonClick = {
                range.value = MIN_PRICE..MAX_PRICE
                rateMoreThan = -1
            },
            onApplyButtonClick = {
                onApplyButtonClick(range.value, rateMoreThan, navController, appViewModel)
            }
        )
    }
}

fun onApplyButtonClick(value: ClosedFloatingPointRange<Float>, rateMoreThan: Int, navController: NavHostController, viewModel: AppViewModel) {
    viewModel.priceRange.value = value
    viewModel.rateMoreThan.value = rateMoreThan
    navController.popBackStack()
}

@Composable
fun SearchFilterFooterSection(
    modifier: Modifier = Modifier,
    onClearAllButtonClick:() -> Unit,
    onApplyButtonClick:() -> Unit
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BlackWhite0),
        horizontalArrangement = Arrangement.SpaceAround
    ){
        Button(
            onClick = {
                onClearAllButtonClick()
            },
            modifier = Modifier
                .padding(8.dp)
                .border(1.dp, BlackLight200, RoundedCornerShape(16.dp))
                .background(BlackWhite0, RoundedCornerShape(16.dp))
                .width(150.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BlackWhite0,
                contentColor = BlackDark900
            )
        ) {
            Text(
                text = stringResource(id = R.string.clear_all_text),
                style = Typography.titleLarge
            )
        }

        Button(
            onClick = { onApplyButtonClick() },
            modifier = Modifier
                .padding(8.dp)
                .border(0.dp, BlackLight200, RoundedCornerShape(16.dp))
                .background(BrandDefault500, RoundedCornerShape(16.dp))
                .width(150.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandDefault500,
                contentColor = BlackDark900
            )
        ) {
            Text(
                text = stringResource(id = R.string.apply_text),
                style = Typography.titleLarge
            )
        }
    }
}

@Composable
fun SearchFilterHeaderSection(navController: NavHostController, viewModel: SearchViewModel){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Icon(
            painter = painterResource(id = R.drawable.ic_back),
            contentDescription = stringResource(id = R.string.back_button_description_text),
            tint = BlackDark900,
            modifier = Modifier.clickable(onClick = {
                viewModel.onBackButtonPress(navController)
            })
        )

        Text(
            text = stringResource(id = R.string.search_filter_screen_name_text),
            style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = BlackDark900
        )

        Spacer(modifier = Modifier)
    }
}

@Composable
fun RangePriceFilterSection(range: MutableState<ClosedFloatingPointRange<Float>>){
    Text(
        text = stringResource(id = R.string.range_price_text),
        style = Typography.headlineSmall,
        color = BlackDark900
    )
    
    RangeSlider(
        value = range.value,
        onValueChange = { range.value = it },
        valueRange = MIN_PRICE..MAX_PRICE,
        steps = 100,
        colors = SliderDefaults.colors(
            activeTrackColor = BlackDark900,
            inactiveTrackColor = BlackLight200,
            thumbColor = BlackWhite0
        )
    )

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = stringResource(id = R.string.from_text),
                style = Typography.bodyLarge,
                color = BlackDark900
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$",
                style = Typography.bodyLarge,
                color = BlackDark900
            )
            TextField(
                value = range.value.start.toInt().toString(),
                onValueChange = { newValue ->
                    val newStartValue = newValue.toFloatOrNull() ?: MIN_PRICE
                    if(newStartValue <= range.value.endInclusive && newStartValue >= MIN_PRICE){
                        range.value = newStartValue..range.value.endInclusive
                    }
                },
                modifier = Modifier
                    .width(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = BlackLight100,
                    unfocusedContainerColor = BlackLight200,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                    ),
                maxLines = 1
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = stringResource(id = R.string.to_text),
                style = Typography.bodyLarge,
                color = BlackDark900
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$",
                style = Typography.bodyLarge,
                color = BlackDark900
            )
            TextField(
                value = range.value.endInclusive.toInt().toString(),
                onValueChange = { newValue ->
                    val newEndInclusiveValue = newValue.toFloatOrNull() ?: MAX_PRICE
                    if(newEndInclusiveValue >= range.value.start && newEndInclusiveValue <= MAX_PRICE){
                        range.value = range.value.start..newEndInclusiveValue
                    }
                },
                modifier = Modifier
                    .width(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = BlackLight100,
                    unfocusedContainerColor = BlackLight200,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 1
            )
        }
    }
}

@Composable
fun StarReviewFilterSection(selectedItem: Int, onItemSelected: (Int) -> Unit = {}){
    Column(Modifier.fillMaxWidth()
    ){
        Text(
            text = stringResource(id = R.string.star_review_filter_text),
            style = Typography.headlineSmall,
            color = BlackDark900,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        for(i in 5 downTo 1){
            StarFilterSectionItem(
                numberYellowStar = i,
                isSelected = selectedItem == i,
                onClick = {
                    onItemSelected( if(selectedItem != i) i else -1 )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun StarFilterSectionItem(
    numberYellowStar: Int = 5,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
){
    Box(
        modifier = Modifier
            .border(
                2.dp,
                if (!isSelected) BlackLight200 else SuccessDefault500,
                RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
            .clickable { onClick() }
    ){
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row{
                if(numberYellowStar in 0..5){
                    repeat(numberYellowStar){
                        Image(
                            painter = painterResource(id = R.drawable.ic_yellow_star_3x),
                            contentDescription = stringResource(id = R.string.yellow_star_description_text)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    repeat(5 - numberYellowStar){
                        Image(
                            painter = painterResource(id = R.drawable.ic_white_star_3x),
                            contentDescription = stringResource(id = R.string.yellow_star_description_text)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
            if(isSelected){
                Image(
                    painter = painterResource(id = R.drawable.ic_success),
                    contentDescription = stringResource(
                        id = R.string.success_icon_description_text
                    )
                )
            }
            else{
                Spacer(modifier = Modifier.weight(1f))
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TourBookingTheme {
        SearchFilterScreen()
    }
}