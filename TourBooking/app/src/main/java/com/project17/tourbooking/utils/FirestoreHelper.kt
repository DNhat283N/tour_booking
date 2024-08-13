@file:Suppress("UNREACHABLE_CODE", "UNUSED_EXPRESSION")

package com.project17.tourbooking.utils

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.project17.tourbooking.models.*
import com.project17.tourbooking.models.Category
import com.project17.tourbooking.models.Tour
import kotlinx.coroutines.tasks.await
import org.mindrot.jbcrypt.BCrypt
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.UUID

object FirestoreHelper {
    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private suspend fun <T : Any> addDocument(collection: String, data: T): String? {
        return try {
            val docRef = db.collection(collection).add(data).await()
            docRef.id
        } catch (e: Exception) {
            Log.e("FirestoreHelper", "Error adding document: ${e.message}")
            null
        }
    }

    private suspend fun <T : Any> updateDocument(collection: String, documentId: String, data: T): Boolean {
        return try {
            db.collection(collection).document(documentId).set(data).await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreHelper", "Error updating document: ${e.message}")
            false
        }
    }

    private suspend fun deleteDocument(collection: String, documentId: String): Boolean {
        return try {
            db.collection(collection).document(documentId).delete().await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreHelper", "Error deleting document: ${e.message}")
            false
        }
    }

    private suspend fun documentExists(collection: String, documentId: String): Boolean {
        return try {
            val docSnapshot = db.collection(collection).document(documentId).get().await()
            docSnapshot.exists()
        } catch (e: Exception) {
            Log.e("FirestoreHelper", "Error checking document existence: ${e.message}")
            false
        }
    }

    private suspend fun isFieldUnique(collection: String, fieldName: String, value: String): Boolean {
        return try {
            val querySnapshot: QuerySnapshot = db.collection(collection)
                .whereEqualTo(fieldName, value)
                .get()
                .await()
            querySnapshot.isEmpty
        } catch (e: Exception) {
            Log.e("FirestoreHelper", "Error checking field uniqueness: ${e.message}")
            false
        }
    }

    private suspend fun uploadImageToFirebase(uri: Uri): String? {
        return try {
            val storageReference = storage.reference.child("avatars/${uri.lastPathSegment}")
            val inputStream: InputStream? = uri.let { db.app.applicationContext.contentResolver.openInputStream(it) }
            val byteArrayOutputStream = ByteArrayOutputStream()
            inputStream?.copyTo(byteArrayOutputStream)
            val data = byteArrayOutputStream.toByteArray()

            val uploadTask = storageReference.putBytes(data).await()
            storageReference.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e("FirestoreHelper", "Error uploading image: ${e.message}")
            null
        }
    }

    private var categoryListener: ListenerRegistration? = null

    suspend fun getAllCategoriesSnapshotListener(
        onCategoriesUpdated: (Map<String, Category>) -> Unit
    ) {
        categoryListener?.remove()  // Remove previous listener if any
        categoryListener = db.collection("categories")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    println("Error fetching categories: ${exception.message}")
                    return@addSnapshotListener
                }

                val categories = snapshot?.documents?.associate { doc ->
                    doc.id to Category(name = doc["name"] as String)
                } ?: emptyMap()

                onCategoriesUpdated(categories)
            }
    }

    // Add, Update, Delete for Category
    suspend fun addCategory(category: Category) = addDocument("categories", category)
    suspend fun updateCategory(documentId: String, category: Category) = updateDocument("categories", documentId, category)
    suspend fun deleteCategory(documentId: String) = deleteDocument("categories", documentId)


    // Add, Update, Delete for Tour
    suspend fun addTour(tour: Tour) = addDocument("tours", tour)
    suspend fun updateTour(documentId: String, tour: Tour) = updateDocument("tours", documentId, tour)
    suspend fun deleteTour(documentId: String) = deleteDocument("tours", documentId)

    // Add, Update, Delete for Ticket
    suspend fun addTicket(ticket: Ticket): String? {
        // Check if the referenced tour exists
        return if (documentExists("tours", ticket.tourId)) {
            addDocument("tickets", ticket)
        } else {
            Log.e("FirestoreHelper", "Tour ID ${ticket.tourId} does not exist")
            null
        }
    }

    suspend fun updateTicket(documentId: String, ticket: Ticket): Boolean {
        // Check if the referenced tour exists
        return if (documentExists("tours", ticket.tourId)) {
            updateDocument("tickets", documentId, ticket)
        } else {
            Log.e("FirestoreHelper", "Tour ID ${ticket.tourId} does not exist")
            false
        }
    }

    suspend fun deleteTicket(documentId: String) = deleteDocument("tickets", documentId)

    // Add, Update, Delete for CategoryTour
    suspend fun addCategoryTour(categoryTour: CategoryTour): String? {
        // Check if both category and tour exist
        if (documentExists("categories", categoryTour.categoryId) && documentExists("tours", categoryTour.tourId)) {
            // Check if the combination of categoryId and tourId is unique
            return if (isFieldUnique("category_tours", "categoryId", categoryTour.categoryId)
                && isFieldUnique("category_tours", "tourId", categoryTour.tourId)) {
                addDocument("category_tours", categoryTour)
            } else {
                Log.e("FirestoreHelper", "Combination of Category ID ${categoryTour.categoryId} and Tour ID ${categoryTour.tourId} already exists")
                null
            }
        } else {
            Log.e("FirestoreHelper", "Category ID ${categoryTour.categoryId} or Tour ID ${categoryTour.tourId} does not exist")
            null
        }
        return TODO("Provide the return value")
    }

    suspend fun updateCategoryTour(documentId: String, categoryTour: CategoryTour): Boolean {
        // Check if both category and tour exist
        return if (documentExists("categories", categoryTour.categoryId) && documentExists("tours", categoryTour.tourId)) {
            updateDocument("category_tours", documentId, categoryTour)
        } else {
            Log.e("FirestoreHelper", "Category ID ${categoryTour.categoryId} or Tour ID ${categoryTour.tourId} does not exist")
            false
        }
    }

    suspend fun deleteCategoryTour(documentId: String) = deleteDocument("category_tours", documentId)

    // Add, Update, Delete for Bill
    suspend fun addBill(bill: Bill) : String? {
        // Check if the referenced account exists
        return if (documentExists("accounts", bill.accountId)) {
            addDocument("bills", bill)
        } else {
            Log.e("FirestoreHelper", "Account ID ${bill.accountId} does not exist")
            null
        }
    }
    suspend fun updateBill(documentId: String, bill: Bill) : Boolean {
        // Check if the referenced account exists
        return if (documentExists("accounts", bill.accountId)) {
            updateDocument("bills", documentId, bill)
        } else {
            Log.e("FirestoreHelper", "Account ID ${bill.accountId} does not exist")
            false
        }
    }
    suspend fun deleteBill(documentId: String) = deleteDocument("bills", documentId)

    // Add, Update, Delete for BillDetail
    suspend fun addBillDetail(billDetail: BillDetail): String? {
        // Check if the referenced ticket and bill exist
        return if (documentExists("tickets", billDetail.ticketId) && documentExists("bills", billDetail.billId)) {
            addDocument("bill_details", billDetail)
        } else {
            Log.e("FirestoreHelper", "Ticket ID ${billDetail.ticketId} or Bill ID ${billDetail.billId} does not exist")
            null
        }
    }

    suspend fun updateBillDetail(documentId: String, billDetail: BillDetail): Boolean {
        // Check if the referenced ticket and bill exist
        return if (documentExists("tickets", billDetail.ticketId) && documentExists("bills", billDetail.billId)) {
            updateDocument("bill_details", documentId, billDetail)
        } else {
            Log.e("FirestoreHelper", "Ticket ID ${billDetail.ticketId} or Bill ID ${billDetail.billId} does not exist")
            false
        }
    }



    suspend fun deleteBillDetail(documentId: String) = deleteDocument("bill_details", documentId)

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun checkPassword(password: String, hashed: String): Boolean {
        return BCrypt.checkpw(password, hashed)
    }

    suspend fun authenticateUser(username: String, password: String): Boolean {
        val document = FirebaseFirestore.getInstance().collection("accounts").document(username).get().await()
        val storedPasswordHash = document.getString("password")

        return storedPasswordHash != null && FirestoreHelper.checkPassword(password, storedPasswordHash)
    }

    suspend fun addAccount(account: Account): Boolean {
        val hashedPassword = hashPassword(account.password)
        val newAccount = account.copy(password = hashedPassword)

        return try {
            db.collection("accounts").document(account.username).set(newAccount).await()
            true
        } catch (e: Exception) {
            println("Error adding account: ${e.message}")
            false
        }
    }

    suspend fun uploadImageToFirebaseStorage(imageUri: Uri): String? {
        return try {
            // Generate a unique filename for the image
            val filename = UUID.randomUUID().toString()
            val storageRef = Firebase.storage.reference.child("avatars/$filename")

            // Upload the image to Firebase Storage
            val uploadTask = storageRef.putFile(imageUri).await()

            // Get the URL of the uploaded image
            val downloadUrl = uploadTask.storage.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            Log.e("FirestoreHelper", "Error uploading image: ${e.message}")
            null
        }
    }

//    fun setCustomClaims(uid: String, role: Role) {
//        val firebaseAuth = FirebaseAuth.getInstance()
//        val userRecord = UserRecord.of(uid)
//
//        val gson = Gson() // Create a Gson instance
//        val customClaims = mapOf(
//            "role" to gson.toJson(role) // Use Gson to convert Role to JSON
//        )
//
//        firebaseAuth.updateAccount(userRecord.buildWithCustomClaims(customClaims))
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    println("Custom claims set successfully")
//                } else {
//                    println("Error setting custom claims: ${task.exception}")
//                }
//            }
//    }
//


    private suspend fun uploadImage(uri: Uri): String? {
        return try {
            val ref = storage.reference.child("avatars/${uri.lastPathSegment}")
            val uploadTask = ref.putFile(uri).await()
            ref.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e("FirestoreHelper", "Error uploading image: $e")
            null
        }
    }


    fun getAllAccountsSnapshotListener(onSnapshot: (List<Account>) -> Unit) {
        db.collection("accounts")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("FirestoreHelper", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val accounts = snapshot.documents.mapNotNull { it.toObject(Account::class.java) }
                    onSnapshot(accounts)
                } else {
                    Log.d("FirestoreHelper", "Current data: null")
                }
            }
    }


    suspend fun updateAccount(username: String, updatedAccount: Account): Boolean {
        return try {
            val hashedPassword = hashPassword(updatedAccount.password)
            val documentRef = db.collection("accounts").document(username)

            // Get current data to keep avatar if not provided
            val currentData = documentRef.get().await().data
            val currentAvatar = currentData?.get("avatar") as? String ?: ""

            // Prepare the data to be updated
            val accountData = mutableMapOf<String, Any>(
                "username" to updatedAccount.username,
                "email" to updatedAccount.email,
                "password" to hashedPassword // Use hashed password here
            )

            // Only add avatar field if it's not empty or if it needs to be deleted
            if (updatedAccount.avatar.isNotEmpty() || currentAvatar.isNotEmpty()) {
                accountData["avatar"] = if (updatedAccount.avatar.isNotEmpty()) {
                    updatedAccount.avatar
                } else {
                    FieldValue.delete()
                }
            }

            documentRef.update(accountData).await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreHelper", "Error updating account: ${e.message}")
            false
        }
    }


    suspend fun deleteAccount(username: String): Boolean {
        return try {
            db.collection("accounts").document(username).delete().await()
            true
        } catch (e: Exception) {
            Log.e("FirestoreHelper", "Error deleting account: $e")
            false
        }
    }

    suspend fun updateCustomer(documentId: String, customer: Customer) : Boolean {
        return if (isFieldUnique("accounts", "accountId", customer.accountId)) {
            updateDocument("customers", documentId, customer)
        } else {
            Log.e("FirestoreHelper", "Account ID ${customer.accountId} already exists")
            false
        }
    }
    suspend fun deleteCustomer(documentId: String) = deleteDocument("customers", documentId)

    // Add, Update, Delete for Staff
    suspend fun addStaff(staff: Staff): String? {
        // Check if the referenced account exists
        return if (documentExists("accounts", staff.accountId)
            && isFieldUnique("staffs", "accountId", staff.accountId)) {
            addDocument("staffs", staff)
        } else {
            Log.e("FirestoreHelper", "Account ID ${staff.accountId} does not exist")
            null
        }
    }

    suspend fun updateStaff(documentId: String, staff: Staff): Boolean {
        // Check if the referenced account exists
        return if (documentExists("accounts", staff.accountId)) {
            updateDocument("staffs", documentId, staff)
        } else {
            Log.e("FirestoreHelper", "Account ID ${staff.accountId} does not exist")
            false
        }
    }

    suspend fun deleteStaff(documentId: String) = deleteDocument("staffs", documentId)

    // Add, Update, Delete for Review
    suspend fun addReview(review: Review): String? {
        // Check if the referenced account and tour exist
        return if (documentExists("accounts", review.accountId) && documentExists("tours", review.tourId)
            && isFieldUnique("reviews", "accountId", review.accountId) && isFieldUnique("reviews", "tourId", review.tourId)) {
            addDocument("reviews", review)
        } else {
            Log.e("FirestoreHelper", "Account ID ${review.accountId} or Tour ID ${review.tourId} does not exist")
            null
        }
    }

    suspend fun updateReview(documentId: String, review: Review): Boolean {
        // Check if the referenced account and tour exist
        return if (documentExists("accounts", review.accountId) && documentExists("tours", review.tourId)
            && isFieldUnique("reviews", "accountId", review.accountId) && isFieldUnique("reviews", "tourId", review.tourId)) {
            updateDocument("reviews", documentId, review)
        } else {
            Log.e("FirestoreHelper", "Account ID ${review.accountId} or Tour ID ${review.tourId} does not exist")
            false
        }
    }

    suspend fun deleteReview(documentId: String) = deleteDocument("reviews", documentId)

    // Add, Update, Delete for CustomerOrderTicket
    suspend fun addCustomerOrderTicket(customerOrderTicket: CustomerOrderTicket): String? {
        // Check if the referenced account and ticket exist
        return if (documentExists("accounts", customerOrderTicket.accountId) && documentExists("tickets", customerOrderTicket.ticketId)
            && isFieldUnique("customer_order_tickets", "accountId", customerOrderTicket.accountId)
            && isFieldUnique("customer_order_tickets", "ticketId", customerOrderTicket.ticketId)) {
            addDocument("customer_order_tickets", customerOrderTicket)
        } else {
            Log.e("FirestoreHelper", "Account ID ${customerOrderTicket.accountId} or Ticket ID ${customerOrderTicket.ticketId} does not exist")
            null
        }
    }

    suspend fun updateCustomerOrderTicket(documentId: String, customerOrderTicket: CustomerOrderTicket): Boolean {
        // Check if the referenced account and ticket exist
        return if (documentExists("accounts", customerOrderTicket.accountId) && documentExists("tickets", customerOrderTicket.ticketId)
                && isFieldUnique("customer_order_tickets", "accountId", customerOrderTicket.accountId)
                && isFieldUnique("customer_order_tickets", "ticketId", customerOrderTicket.ticketId)) {
            updateDocument("customer_order_tickets", documentId, customerOrderTicket)
        } else {
            Log.e("FirestoreHelper", "Account ID ${customerOrderTicket.accountId} or Ticket ID ${customerOrderTicket.ticketId} does not exist")
            false
        }
    }

    suspend fun deleteCustomerOrderTicket(documentId: String) = deleteDocument("customer_order_tickets", documentId)
}
