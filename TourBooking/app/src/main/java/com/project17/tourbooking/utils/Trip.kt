package com.project17.tourbooking.utils

data class Trip(
    val name: String,
    val image: Int,
    val rate: Double,
    val location: String,
    var isAddedToWishList: Boolean = false
)
