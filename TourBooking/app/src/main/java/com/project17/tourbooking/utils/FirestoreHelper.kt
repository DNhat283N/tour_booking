
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.project17.tourbooking.models.*
import kotlinx.coroutines.tasks.await
import org.mindrot.jbcrypt.BCrypt
import java.util.UUID


object FirestoreHelper {

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()


    suspend fun uploadImageToFirebase(uri: Uri): String? {
        val storageRef = storage.reference
        val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

        return try {
            val uploadTask = imageRef.putFile(uri).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            null
        }
    }

    private val categoriesCollection = db.collection("categories")

    // --------------------- Category ---------------------
    // Add a new category
    suspend fun addCategory(category: Category): String {
        val docRef = categoriesCollection.add(category).await()
        return docRef.id
    }

    // Update an existing category
    suspend fun updateCategory(id: String, updatedCategory: Category) {
        categoriesCollection.document(id).set(updatedCategory).await()
    }

    // Delete a category
    suspend fun deleteCategory(id: String) {
        categoriesCollection.document(id).delete().await()
    }

    // Get all categories
    suspend fun getCategories(): List<Pair<String, Category>> {
        return categoriesCollection.get().await().map { document ->
            document.id to document.toObject(Category::class.java)
        }
    }

    // --------------------- Tour ---------------------
    private val toursCollection = db.collection("tours")

    suspend fun getTours(): List<Pair<String, Tour>> {
        return toursCollection.get().await().documents.map { doc ->
            doc.id to doc.toObject(Tour::class.java)!!
        }
    }

    suspend fun getTourById(id: String): Tour? {
        return try {
            val document = toursCollection.document(id).get().await()
            document.toObject(Tour::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun addTour(tour: Tour): String {
        val docRef = toursCollection.add(tour).await()
        return docRef.id
    }

    suspend fun updateTour(id: String, tour: Tour) {
        toursCollection.document(id).set(tour).await()
    }

    suspend fun deleteTour(id: String) {
        toursCollection.document(id).delete().await()


    }

    // --------------------- Ticket ---------------------
    suspend fun getTickets(): Result<Map<String, Ticket>> {
        return try {
            val snapshot = db.collection("tickets").get().await()
            val tickets = snapshot.documents.associate { it.id to it.toObject(Ticket::class.java)!! }
            Result.success(tickets)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addTicket(ticket: Ticket): Result<Unit> {
        return try {
            val newDocRef = db.collection("tickets").document()
            ticket.copy().let {
                newDocRef.set(it).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTicket(docId: String, ticket: Ticket): Result<Unit> {
        return try {
            db.collection("tickets").document(docId).set(ticket).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTicket(docId: String): Result<Unit> {
        return try {
            db.collection("tickets").document(docId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --------------------- Bill ---------------------
    suspend fun getBills(): Result<Map<String, Bill>> {
        return try {
            val snapshot = db.collection("bills").get().await()
            val bills = snapshot.documents.associate { it.id to it.toObject(Bill::class.java)!! }
            Result.success(bills)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addBill(bill: Bill): Result<Unit> {
        return try {
            val newDocRef = db.collection("bills").document()
            bill.copy().let {
                newDocRef.set(it).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateBill(docId: String, bill: Bill): Result<Unit> {
        return try {
            db.collection("bills").document(docId).set(bill).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteBill(docId: String): Result<Unit> {
        return try {
            db.collection("bills").document(docId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --------------------- BillDetail ---------------------
    suspend fun getBillDetails(): Result<Map<String, BillDetail>> {
        return try {
            val snapshot = db.collection("bill_details").get().await()
            val billDetails = snapshot.documents.associate { it.id to it.toObject(BillDetail::class.java)!! }
            Result.success(billDetails)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addBillDetail(billDetail: BillDetail): Result<Unit> {
        return try {
            val newDocRef = db.collection("bill_details").document()
            billDetail.copy().let {
                newDocRef.set(it).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateBillDetail(docId: String, billDetail: BillDetail): Result<Unit> {
        return try {
            db.collection("bill_details").document(docId).set(billDetail).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteBillDetail(docId: String): Result<Unit> {
        return try {
            db.collection("bill_details").document(docId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --------------------- Account ---------------------

    private val accountsCollection = db.collection("accounts")

    suspend fun addAccount(account: Account) {
        val hashedPassword = hashPassword(account.password)
        val accountWithHashedPassword = account.copy(password = hashedPassword)
        accountsCollection.document(account.username).set(accountWithHashedPassword).await()
    }

    suspend fun updateAccount(account: Account) {
        val accountFromDb = getAccount(account.username)
        val updatedAccount = if (account.password.isNotEmpty()) {
            val hashedPassword = hashPassword(account.password)
            account.copy(password = hashedPassword)
        } else {
            account.copy(password = accountFromDb?.password ?: "")
        }
        accountsCollection.document(account.username).set(updatedAccount).await()
    }

    suspend fun deleteAccount(username: String) {
        accountsCollection.document(username).delete().await()
    }

    suspend fun getAccount(username: String): Account? {
        val document = accountsCollection.document(username).get().await()
        return document.toObject(Account::class.java)
    }

    suspend fun getAllAccounts(): List<Account> {
        val snapshot = accountsCollection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(Account::class.java) }
    }

    // Mã hóa mật khẩu
    private fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    // Kiểm tra mật khẩu
    private fun checkPassword(password: String, hashed: String): Boolean {
        return BCrypt.checkpw(password, hashed)
    }

    // Kiểm tra mật khẩu khi đăng nhập
    suspend fun verifyPassword(username: String, password: String): Boolean {
        val account = getAccount(username)
        return account?.let { checkPassword(password, it.password) } ?: false
    }

    suspend fun authenticateUser(emailOrUsername: String, password: String): Boolean {
        return try {
            val query = db.collection("accounts")
                .whereEqualTo("username", emailOrUsername)
                .get().await()

            if (query.documents.isNotEmpty()) {
                val document = query.documents.first()
                val hashedPassword = document.getString("password")

                if (hashedPassword != null) {
                    checkPassword(password, hashedPassword)
                } else {
                    false
                }
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun isEmailExists(email: String): Boolean {
        return try {
            val query = db.collection("accounts")
                .whereEqualTo("email", email)
                .get()
                .await()

            query.documents.isNotEmpty() // Trả về true nếu có ít nhất một tài liệu khớp
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updatePassword(email: String, newPassword: String): Boolean {
        return try {
            val userRef = db.collection("accounts")
                .whereEqualTo("email", email)
                .get()
                .await()
                .documents
                .firstOrNull()
                ?.reference

            userRef?.update("password", newPassword)?.await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // --------------------- Customer ---------------------
    suspend fun getCustomers(): Result<Map<String, Customer>> {
        return try {
            val snapshot = db.collection("customers").get().await()
            val customers = snapshot.documents.associate { it.id to it.toObject(Customer::class.java)!! }
            Result.success(customers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addCustomer(customer: Customer): Result<Unit> {
        return try {
            val newDocRef = db.collection("customers").document()
            customer.copy().let {
                newDocRef.set(it).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateCustomer(docId: String, customer: Customer): Result<Unit> {
        return try {
            db.collection("customers").document(docId).set(customer).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteCustomer(docId: String): Result<Unit> {
        return try {
            db.collection("customers").document(docId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --------------------- Review ---------------------
    suspend fun getReviews(): Result<Map<String, Review>> {
        return try {
            val snapshot = db.collection("reviews").get().await()
            val reviews = snapshot.documents.associate { it.id to it.toObject(Review::class.java)!! }
            Result.success(reviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addReview(review: Review): Result<Unit> {
        return try {
            val newDocRef = db.collection("reviews").document()
            review.copy().let {
                newDocRef.set(it).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateReview(docId: String, review: Review): Result<Unit> {
        return try {
            db.collection("reviews").document(docId).set(review).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteReview(docId: String): Result<Unit> {
        return try {
            db.collection("reviews").document(docId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --------------------- CustomerOrderTicket ---------------------
    suspend fun getCustomerOrderTickets(): Result<Map<String, CustomerOrderTicket>> {
        return try {
            val snapshot = db.collection("customer_order_tickets").get().await()
            val customerOrderTickets = snapshot.documents.associate { it.id to it.toObject(CustomerOrderTicket::class.java)!! }
            Result.success(customerOrderTickets)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addCustomerOrderTicket(customerOrderTicket: CustomerOrderTicket): Result<Unit> {
        return try {
            val newDocRef = db.collection("customer_order_tickets").document()
            customerOrderTicket.copy().let {
                newDocRef.set(it).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateCustomerOrderTicket(docId: String, customerOrderTicket: CustomerOrderTicket): Result<Unit> {
        return try {
            db.collection("customer_order_tickets").document(docId).set(customerOrderTicket).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteCustomerOrderTicket(docId: String): Result<Unit> {
        return try {
            db.collection("customer_order_tickets").document(docId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}