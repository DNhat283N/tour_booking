package com.project17.tourbooking.utils.composables

import android.content.Context
import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.project17.tourbooking.R
import com.project17.tourbooking.helper.FirestoreHelper
import com.project17.tourbooking.models.Tour
import com.project17.tourbooking.models.TourWithId
import com.project17.tourbooking.navigates.NavigationItems
import com.project17.tourbooking.ui.theme.BlackWhite0
import com.project17.tourbooking.ui.theme.Typography
import com.project17.tourbooking.utils.modifiers.iconWithBackgroundModifier

@Composable
fun TourCardInVertical(
    tour: Tour,
    navController: NavHostController,
    context: Context = LocalContext.current
) {

    val toursWithIds = remember { mutableStateListOf<TourWithId>() }
    val tourId = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val loadedToursWithIds = FirestoreHelper.loadToursWithIds()
        toursWithIds.clear()
        toursWithIds.addAll(loadedToursWithIds)

        val tourWithId = toursWithIds.find { it.tour == tour }
        if (tourWithId != null) {
            tourId.value = tourWithId.id
        }
    }

    Box(
        modifier = Modifier
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
                navController.navigate(NavigationItems.TripDetail.route + "/${tourId.value}")
            })
    ) {
        AsyncImage(
            model = tour.image,
            contentDescription = "Tour Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth()
                .height(200.dp)
        )
        AddToWishList(
            initiallyAddedToWishList = false,
            modifier = Modifier
                .iconWithBackgroundModifier()
                .align(Alignment.TopEnd)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Log.d("TourCardInVertical", "Tour name: ${tour.name}")
            Text(
                text = tour.name,
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
                    text = tour.destination,
                    style = Typography.bodyMedium,
                    color = BlackWhite0
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            GenerateStarFromRating(rating = tour.averageRating)
        }
    }
    Spacer(modifier = Modifier.width(16.dp))
}