package com.project17.tourbooking.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.Timestamp
import com.project17.tourbooking.R
import com.project17.tourbooking.models.Review
import com.project17.tourbooking.navigates.NavigationItems
import com.project17.tourbooking.ui.theme.BlackLight100
import com.project17.tourbooking.ui.theme.BlackLight200
import com.project17.tourbooking.ui.theme.BlackLight400
import com.project17.tourbooking.ui.theme.BlackWhite0
import com.project17.tourbooking.ui.theme.ErrorDark600
import com.project17.tourbooking.ui.theme.SuccessDefault500
import com.project17.tourbooking.ui.theme.Typography
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId

val addedWishListIcon2x = R.drawable.ic_added_to_wishlist_2x
val addedWishListIcon3x = R.drawable.ic_added_to_wishlist_3x

val toAddWishListIcon2x = R.drawable.ic_to_add_to_wishlist_2x
val toAddWishListIcon3x = R.drawable.ic_to_add_to_wishlist_3x

@Composable
fun AddToWishList(
    modifier: Modifier = Modifier.iconWithBackgroundModifier(),
    tour: WishListItem,
    addedIcon: Int = addedWishListIcon2x,
    toAddIcon: Int = toAddWishListIcon2x,
){
    var isInWishlist by remember {
        mutableStateOf(tour.isAddedToWishList)
    }
    Icon(
        painter = painterResource(
            id =
            if(isInWishlist)
                addedIcon
            else
                toAddIcon),
        tint =
        if(isInWishlist)
            Color.Red
        else
            Color.Unspecified,
        contentDescription = "Favorite",
        modifier = modifier
            .clickable(onClick = {
                isInWishlist = !isInWishlist
                tour.isAddedToWishList = isInWishlist
                // TODO: Add to Wishlist or Remove from WishList
            })
    )

}

@Composable
fun AlertDialogUtil(
    isDialogVisible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: Int,
    message: Int,
    confirmButtonText: Int,
    dismissButtonText: Int
){
    if(isDialogVisible){
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = stringResource(id = title),
                    style = Typography.titleLarge
                )
            },
            text = {
                Text(
                    text = stringResource(id = message),
                    style = Typography.titleMedium
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm()
                }) {
                    Text(
                        text = stringResource(id =confirmButtonText),
                        style = Typography.titleSmall
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDismiss()
                }) {
                    Text(
                        text = stringResource(id = dismissButtonText),
                        style = Typography.titleSmall
                    )
                }
            }
        )
    }
}

@Composable
fun TourCardInHorizontal(
    modifier: Modifier = Modifier,
    tour: TourPackage,
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
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(BlackWhite0)
                .padding(8.dp)
        ){
            Image(
                painter = painterResource(tour.image),
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
                        text = tour.name,
                        style = Typography.titleLarge,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = String.format("$" + "%.2f", (tour.price ?: 0.0)),
                        style = Typography.bodyLarge,
                        color = ErrorDark600
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    GenerateStarFromRating(rating = tour.rate, Color.Black)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = tour.description,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = BlackLight400,
                        style = Typography.bodyMedium,
                        lineHeight = 24.sp
                    )
                }
                AddToWishList(
                    tour = tour,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp))
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun GenerateStarFromRating(
    rating: Double,
    textColor: Color = BlackWhite0,
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
fun CategoryItem(
    category: Category,
    isSelected: Boolean = false,
    onClick:() -> Unit = {}
){
    Box(
        modifier = Modifier
            .background(
                color = BlackWhite0,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) SuccessDefault500 else BlackLight200,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(4.dp)
    ){
        Row(
            modifier = Modifier
                .clickable(onClick = {
                    onClick()
                }),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = category.icon),
                contentDescription = stringResource(id = R.string.image_description_text),
                modifier = Modifier
                    .background(
                        color = BlackLight100,
                        shape = RoundedCornerShape(16.dp)
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = category.name),
                style = Typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
    Spacer(modifier = Modifier.width(5.dp))
}

@Composable
fun TourCardInVertical(tour: Tour, navController: NavHostController){
    Box(
        modifier = Modifier
            .fillMaxSize()
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
                val tourId = "1"
                //TODO: get tour_id
                navController.navigate(NavigationItems.TripDetail.route + "/${tourId}")
            })
    ){


        Image(
            painter = painterResource(id = R.drawable.fuji_mountain),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
        )

        AddToWishList(
            tour = tour,
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
                    text = tour.location,
                    style = Typography.bodyMedium,
                    color = BlackWhite0
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            GenerateStarFromRating(rating = tour.rate)
        }
    }
    Spacer(modifier = Modifier.width(16.dp))
}

@Composable
fun TourSummaryCard(tour: Tour) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .paint(painter = painterResource(id = R.drawable.fuji_mountain), contentScale = ContentScale.Crop)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)) {
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.ic_wishlist_verify),
                        contentDescription = stringResource(id = R.string.image_description_text),
                        modifier = Modifier.alpha(if (tour.isAddedToWishList) 1f else 0f)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                Column(Modifier.fillMaxWidth()) {
                    Text(
                        text = tour.name,
                        style = Typography.headlineMedium,
                        color = BlackWhite0
                    )
                    Spacer(modifier = Modifier.height(16.dp))
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
                            style = Typography.titleLarge,
                            color = BlackWhite0
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(id = R.string.people_have_explored_text, 100),
                        style = Typography.bodyLarge,
                        color = BlackWhite0
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    GenerateStarFromRating(rating = tour.rate, textColor = BlackWhite0)
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review){
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        // Avatar
        Image(
            painter = painterResource(id = R.drawable.default_avatar),
            contentDescription = "User Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Yelena Belova",
                    style = Typography.titleLarge,
                )
                Text(
                    text = calculateReviewDateDisplay(review.createdDate),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }

            GenerateStarFromRating(rating = review.rating.toDouble())

            Text(
                text = stringResource(id = R.string.sample_text),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

fun calculateReviewDateDisplay(reviewDate: Timestamp): String {
    val reviewLocalDate = reviewDate.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    val currentDate = LocalDate.now()

    val period = Period.between(reviewLocalDate, currentDate)

    return when {
        period.years > 0 -> {
            val years = period.years
            val months = period.months
            if (months > 0) {
                "$years years $months months ago"
            } else {
                "$years years ago"
            }
        }

        period.months > 0 -> "${period.months} months ago"
        period.days > 0 -> "${period.days} days ago"
        else -> "Today"
    }
}