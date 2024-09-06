package com.project17.tourbooking.activities.pay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.project17.tourbooking.api.CreateOrder
import com.project17.tourbooking.constant.CurrencyRate
import com.project17.tourbooking.ui.theme.BlackDark900
import kotlinx.coroutines.launch
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener

class PayActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ZaloPay SDK
        ZaloPaySDK.init(554, Environment.SANDBOX) // Ensure SDK is initialized

        setContent {
            val context = LocalContext.current
            var paymentStatus by remember { mutableStateOf("") }
            var token by remember { mutableStateOf("") }
            var isPayButtonEnabled by remember { mutableStateOf(false) } // Track button enabled state

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        val orderApi = CreateOrder()
                        lifecycleScope.launch {
                            try {
                                val data = orderApi.createOrder((200 * CurrencyRate.VND).toString())
                                Log.d("Amount", (200 * CurrencyRate.VND).toString())
                                Log.d("CreateOrderResponse", data.toString())
                                val code = data.getString("return_code")
                                token = data?.getString("zp_trans_token") ?: ""
                                Toast.makeText(context, "return_code: $code", Toast.LENGTH_LONG).show()

                                isPayButtonEnabled = code == "1"
                                paymentStatus = "Tạo thành công"
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                ) {
                    Text(text = "Thanh toán 200USD")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(paymentStatus, color = BlackDark900)

                Button(
                    onClick = {
                        if (token.isNotEmpty() && isPayButtonEnabled) {
                            ZaloPaySDK.getInstance().payOrder(
                                this@PayActivity,
                                token,
                                "demozpdk://app",
                                object : PayOrderListener {
                                    override fun onPaymentCanceled(zpTransToken: String?, appTransID: String?) {
                                        //Xử lý logic khi người dùng từ chối thanh toán
                                    }
                                    override fun onPaymentError(zaloPayError: ZaloPayError?, zpTransToken: String?, appTransID: String?) {
                                        if(zaloPayError == ZaloPayError.PAYMENT_APP_NOT_FOUND){
                                            ZaloPaySDK.getInstance().navigateToZaloOnStore(context)
                                            ZaloPaySDK.getInstance().navigateToZaloPayOnStore(context)
                                        }
                                    }
                                    override fun onPaymentSucceeded(transactionId: String, transToken: String, appTransID: String?) {
                                        //Xử lý logic khi thanh toán thành công
                                        paymentStatus = "Thanh toán thành công"

                                    }
                                }
                            )
                        } else {
                            Toast.makeText(context, "Unable to proceed with payment. Please try again.", Toast.LENGTH_LONG).show()
                        }
                    },
                    enabled = isPayButtonEnabled // Enable the button based on the flag
                ) {
                    Text(text = "Thanh toán bằng ZaloPay")
                }
            }

        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }
}
