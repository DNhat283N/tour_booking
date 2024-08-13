package com.project17.tourbooking.models

import com.google.firebase.Timestamp

data class Category(
    val name: String
)

data class Tour(
    val name: String,
    val openRegistrationDate: Any,
    val closeRegistrationDate: Timestamp,
    val cancellationDeadline: Timestamp,
    val startDate: Timestamp,
    val destination: String,
    val slotQuantity: Int
)

data class Ticket(
    val ticketType: String,
    val price: Double,
    val tourId: String
)

data class CategoryTour(
    val categoryId: String,
    val tourId: String,
)

data class Bill(
    val totalAmount: Double,
    val createdDate: Timestamp = Timestamp.now(),
    val accountId: String
)

data class BillDetail(
    val ticketId: String,
    val quantity: Int,
    val billId: String
)

data class Account(
    var username: String = "",
    var email: String = "",
    var password: String = "",
    var avatar: String = ""
) {
    // Default constructor for Firestore deserialization
    constructor() : this("", "", "", "")
}

data class Role(
    val admin: Boolean = false,
    val editor: Boolean = false,
    val user: Boolean = false,
    val staff: Boolean = false
)

open class Customer(
    open val fullName: String,
    open val gender: Boolean,
    open val dateOfBirth: Timestamp,
    open val address: String,
    open val phoneNumber: String,
    open val accountId: String,
)

data class Staff(
    override val fullName: String,
    override val gender: Boolean,
    override val dateOfBirth: Timestamp,
    override val address: String,
    override val phoneNumber: String,
    override val accountId: String,
    val position: String
) : Customer(fullName, gender, dateOfBirth, address, phoneNumber, accountId)

data class Review(
    val rating: Int,
    val comment: String,
    val createdDate: Timestamp = Timestamp.now(),
    val accountId: String,
    val tourId: String,
)

data class CustomerOrderTicket(
    val accountId: String,
    val ticketId: String,
)


