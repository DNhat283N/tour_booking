package com.project17.tourbooking.utils

data class TourPackage(
    val name: String,
    val image: Int,
    val price: Double,
    val rate: Double,
    override var isAddedToWishList: Boolean = false,
    val description: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
): WishListItem
