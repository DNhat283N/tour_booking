package com.project17.tourbooking.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.project17.tourbooking.R

val addedWishListIcon2x = R.drawable.ic_added_to_wishlist_2x
val addedWishListIcon3x = R.drawable.ic_added_to_wishlist_3x

val toAddWishListIcon2x = R.drawable.ic_to_add_to_wishlist_2x
val toAddWishListIcon3x = R.drawable.ic_to_add_to_wishlist_3x

@Composable
fun AddToWishList(
    tour: WishListItem,
    addedIcon: Int = addedWishListIcon2x,
    toAddIcon: Int = toAddWishListIcon2x,
    modifier: Modifier = Modifier.iconWithBackgroundModifier(),
    context: Context = LocalContext.current
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
                Toast
                    .makeText(
                        context,
                        if (tour.isAddedToWishList) "Added to Wishlist" else "Removed from Wishlist",
                        Toast.LENGTH_SHORT
                    )
                    .show()

                // TODO: Add to Wishlist or Remove from WishList
            })
    )

}

