package com.project17.tourbooking.utils.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.project17.tourbooking.models.Tour
import com.project17.tourbooking.navigates.NavigationItems
import com.project17.tourbooking.ui.theme.BlackLight200
import com.project17.tourbooking.ui.theme.BlackLight400
import com.project17.tourbooking.ui.theme.BlackWhite0
import com.project17.tourbooking.ui.theme.ErrorDark600
import com.project17.tourbooking.ui.theme.Typography

@Composable
fun TourCardInHorizontal(
    modifier: Modifier = Modifier,
    tour: Tour,
    navController: NavHostController,
    onMeasured: (Dp) -> Unit = {}
) {
    val density = LocalDensity.current
    val tourId = "1"
    Card(
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                val height = with(density) { coordinates.size.height.toDp() }
                onMeasured(height)
            }
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = {
                navController.navigate(NavigationItems.TripDetail.route + "/${tourId}")
            })
            .border(
                width = 1.dp,
                color = BlackLight200,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(BlackWhite0)
                .padding(8.dp)
        ) {
            Image(
                painter = rememberImagePainter(tour.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .fillMaxHeight()
                    .weight(2f)
                    .aspectRatio(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .weight(3f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BlackWhite0)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = tour.name,
                        style = Typography.titleLarge,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))

//                    Text(
//                        text = String.format("$" + "%.2f", tour.price),
//                        style = Typography.bodyLarge,
//                        color = ErrorDark600
//                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    GenerateStarFromRating(rating = tour.averageRating, Color.Black)
                    Spacer(modifier = Modifier.height(16.dp))

//                    Text(
//                        text = tour.description,
//                        maxLines = 2,
//                        overflow = TextOverflow.Ellipsis,
//                        color = BlackLight400,
//                        style = Typography.bodyMedium,
//                        lineHeight = 24.sp
//                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

