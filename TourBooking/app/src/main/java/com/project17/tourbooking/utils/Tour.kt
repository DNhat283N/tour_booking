package com.project17.tourbooking.utils

data class Tour(
    val name: String,
    val image: Int,
    val rate: Double,
    val location: String,
    val price: Double = 0.0,
    override var isAddedToWishList: Boolean = false
):WishListItem
