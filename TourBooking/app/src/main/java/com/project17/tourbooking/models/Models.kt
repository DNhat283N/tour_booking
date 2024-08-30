package com.project17.tourbooking.models

import com.google.firebase.Timestamp

data class Category(
    val name: String,
    val icon: String
) {
    // Add a no-argument constructor
    constructor() : this("", "")
}

data class Tour(
    val name: String,
    val openRegistrationDate: Timestamp,
    val closeRegistrationDate: Timestamp,
    val cancellationDeadline: Timestamp,
    val startDate: Timestamp,
    val destination: String,
    val slotQuantity: Int,
    val image: String,
    val averageRating: Double,
    val categoryId: String
){
    // Add a no-argument constructor
    constructor() : this("", Timestamp.now(), Timestamp.now(),
        Timestamp.now(), Timestamp.now(), "", 0, "",0.0, "")
}

data class Ticket(
    val ticketType: String,
    val price: Double,
    val tourId: String
){
    // Add a no-argument constructor
    constructor() : this("", 0.0, "")
}


data class Bill(
    val totalAmount: Double,
    val createdDate: Timestamp = Timestamp.now(),
    val accountId: String
){
    // Add a no-argument constructor
    constructor() : this(0.0, Timestamp.now(), "")
}

data class BillDetail(
    val ticketId: String,
    val quantity: Int,
    val billId: String
) {
    // Add a no-argument constructor
    constructor() : this( "", 0, "")
}

data class Account(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val avatar: String = "",
    val role: String = ""
) {
    // Default constructor for Firestore deserialization
    constructor() : this("", "", "", "", "")
}

data class Customer (
    val fullName: String,
    val gender: Boolean,
    val dateOfBirth: Timestamp,
    val address: String,
    val phoneNumber: String,
    val accountId: String,
){
    // Add a no-argument constructor
    constructor() : this("", true, Timestamp.now(), "", "", "")
}


data class Review(
    val rating: Double,
    val comment: String,
    val createdDate: Timestamp = Timestamp.now(),
    val accountId: String,
    val tourId: String,
){
    // Add a no-argument constructor
    constructor() : this(0.0, "", Timestamp.now(), "", "")
}

data class CustomerOrderTicket(
    val accountId: String,
    val ticketId: String,
) {
    // Add a no-argument constructor
    constructor() : this( "", "")
}


