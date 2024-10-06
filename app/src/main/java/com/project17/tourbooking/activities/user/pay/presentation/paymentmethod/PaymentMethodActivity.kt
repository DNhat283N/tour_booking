package com.project17.tourbooking.activities.user.pay.presentation.paymentmethod

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Timestamp
import com.project17.tourbooking.R
import com.project17.tourbooking.activities.user.pay.viewmodel.PayViewModel
import com.project17.tourbooking.activities.user.pay.viewmodel.PaymentItem
import com.project17.tourbooking.constant.PAYMENT_STATUS
import com.project17.tourbooking.constant.VALUE_TYPE
import com.project17.tourbooking.helper.firestore_helper.FirestoreHelper
import com.project17.tourbooking.models.Account
import com.project17.tourbooking.models.Bill
import com.project17.tourbooking.models.TourBooking
import com.project17.tourbooking.ui.theme.BlackDark900
import com.project17.tourbooking.ui.theme.BlackLight300
import com.project17.tourbooking.ui.theme.BlackLight400
import com.project17.tourbooking.ui.theme.BlackWhite0
import com.project17.tourbooking.ui.theme.BrandDefault500
import com.project17.tourbooking.ui.theme.ErrorDefault500
import com.project17.tourbooking.ui.theme.SuccessDefault500
import com.project17.tourbooking.ui.theme.Typography
import com.project17.tourbooking.viewmodels.AuthViewModel

class PaymentMethodActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaymentMethodScreen(payViewModel = PayViewModel())
        }
    }
}

@Composable
fun PaymentMethodScreen(
    navController: NavController = rememberNavController(),
    authViewModel: AuthViewModel = AuthViewModel(),
    payViewModel: PayViewModel
) {

    var moneyPrice by remember { mutableStateOf<Double?>(null) }
    var coinPrice by remember { mutableStateOf<Int?>(null) }
    var account by remember {
        mutableStateOf<Account?>(null)
    }

    val paymentItem = payViewModel.paymentItem

    var paymentMethodSelected by remember {
        mutableStateOf(PaymentMethod.ZALO_PAY)
    }

    var isCoinPaymentEnable by remember {
        mutableStateOf(true)
    }

    val context = LocalContext.current

    LaunchedEffect(paymentItem) {
        account = FirestoreHelper.getAccountByAccountId(authViewModel.getCurrentUser()?.uid!!)
        when (val item = paymentItem.value) {
            is PaymentItem.TicketItem -> {
                moneyPrice = item.ticket.moneyPrice.toDouble().times(payViewModel.quantity.value!!)
                isCoinPaymentEnable = item.ticket.coinPrice.toInt()
                    .times(payViewModel.quantity.value!!) <= account?.coin!!
                Log.d(
                    "PaymentMethodScreen",
                    "Selected TicketItem: MoneyPrice = $moneyPrice, CoinPrice = $coinPrice"
                )
            }

            is PaymentItem.CoinPackageItem -> {
                moneyPrice = item.coinPackage.price.toDouble().times(payViewModel.quantity.value!!)
                coinPrice = null
                Log.d("PaymentMethodScreen", "Selected CoinPackageItem: MoneyPrice = $moneyPrice")
            }

            null -> {
                coinPrice = null
                moneyPrice = null
                Log.d("PaymentMethodScreen", "No payment item selected.")
            }
        }
    }

    val isCoinPaymentAllowed = payViewModel.isCoinPaymentAllowed()

    Box(
        Modifier
            .fillMaxSize()
            .background(BlackWhite0)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            HeaderSection(navController)

            PaymentMethodItem(
                "ZaloPay",
                R.drawable.ic_zalo_pay,
                paymentMethodSelected == PaymentMethod.ZALO_PAY,
                {
                    paymentMethodSelected = PaymentMethod.ZALO_PAY
                    moneyPrice = when (val item = paymentItem.value) {
                        is PaymentItem.TicketItem -> {
                            item.ticket.moneyPrice.toDouble().times(payViewModel.quantity.value!!)
                        }

                        is PaymentItem.CoinPackageItem -> {
                            item.coinPackage.price.toDouble().times(payViewModel.quantity.value!!)
                        }

                        null -> null
                    }
                    coinPrice = null
                    Log.d(
                        "PaymentMethodScreen",
                        "Selected payment method: ZaloPay, MoneyPrice = $moneyPrice"
                    )
                },
                null
            )

            if (isCoinPaymentAllowed) {
                PaymentMethodItem(
                    "Traver Coin",
                    R.drawable.coin,
                    paymentMethodSelected == PaymentMethod.TRAVER_COIN,
                    {
                        paymentMethodSelected = PaymentMethod.TRAVER_COIN
                        coinPrice = paymentItem.value?.let {
                            if (it is PaymentItem.TicketItem) {
                                it.ticket.coinPrice.toInt().times(payViewModel.quantity.value!!)
                            } else {
                                null
                            }
                        }
                        moneyPrice = null
                        Log.d(
                            "PaymentMethodScreen",
                            "Selected payment method: Traver Coin, CoinPrice = $coinPrice"
                        )
                    },
                    account?.coin.toString(),
                    isCoinPaymentEnable
                )
            }
        }

        FooterSection(
            moneyPrice = moneyPrice,
            coinPrice = coinPrice,
            modifier = Modifier.align(Alignment.BottomStart),
            onBookingButtonClick = {
                if (paymentMethodSelected == PaymentMethod.ZALO_PAY) {
                    val totalAmount = (moneyPrice?.times(payViewModel.quantity?.value!!))?.toInt() ?: 0
                    payViewModel.payWithZaloPay(
                        amount = totalAmount,
                        onSuccess = {
                            when (val item = paymentItem.value) {
                                is PaymentItem.TicketItem -> {
                                    FirestoreHelper.createTourBookingBill(
                                        TourBooking(
                                            id = "",
                                            accountId = authViewModel.getCurrentUser()?.uid!!,
                                            ticketId = item.ticket.id,
                                            quantity = payViewModel.quantity.value?.toInt() ?: 1,
                                            total = totalAmount.toLong(),
                                            valueType = VALUE_TYPE.MONEY,
                                            bookingDate = Timestamp.now(),
                                            paymentStatus = PAYMENT_STATUS.SUCCESS
                                        )
                                    )
                                }

                                is PaymentItem.CoinPackageItem -> {
                                    if(authViewModel.getCurrentUser()?.uid != null){
                                        FirestoreHelper.createCoinPackageBill(
                                            Bill(
                                                id = "",
                                                accountId = authViewModel.getCurrentUser()?.uid!!,
                                                coinPackageId = item.coinPackage.id,
                                                totalAmount = item.coinPackage.price.toDouble(),
                                                createdDate =Timestamp.now(),
                                                paymentStatus = PAYMENT_STATUS.SUCCESS,
                                            ), onSuccess = {
                                                FirestoreHelper.updateAccountCoin(
                                                    authViewModel.getCurrentUser()?.uid!!,
                                                    item.coinPackage.coinValue.toInt(),
                                                    onSuccess = {
                                                        //navigate to paysuccess
                                                    }
                                                )
                                            })
                                    }
                                }

                                null -> null
                            }
                        },
                        context = context
                    )
                } else if (paymentMethodSelected == PaymentMethod.TRAVER_COIN) {
                    val totalAmount = (coinPrice?.times(payViewModel.quantity?.value!!)) ?: 0
                    when (val item = paymentItem.value) {
                        is PaymentItem.TicketItem -> {
                            authViewModel.getCurrentUser()
                                ?.let {
                                    FirestoreHelper.updateAccountCoin(
                                        it.uid,
                                        totalAmount,
                                        onSuccess = {
                                            FirestoreHelper.createTourBookingBill(
                                                TourBooking(
                                                    id = "",
                                                    accountId = authViewModel.getCurrentUser()?.uid!!,
                                                    ticketId = item.ticket.id,
                                                    quantity = payViewModel.quantity.value?.toInt()
                                                        ?: 1,
                                                    total = totalAmount.toLong(),
                                                    valueType = VALUE_TYPE.COIN,
                                                    bookingDate = Timestamp.now(),
                                                    paymentStatus = PAYMENT_STATUS.SUCCESS
                                                )
                                            )
                                            //navigate to paymentsuccess
                                        })
                                }
                        }

                        is PaymentItem.CoinPackageItem -> {}
                        null -> {}
                    }
                }
            },
            authViewModel = authViewModel
        )
    }
}

@Composable
fun HeaderSection(navController: NavController) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                painterResource(id = R.drawable.ic_back),
                contentDescription = "Back"
            )
        }

        Text(
            text = stringResource(id = R.string.payment_method_text),
            style = Typography.headlineSmall,
            color = BlackDark900
        )

        Spacer(Modifier)
    }
}

@Composable
fun PaymentMethodItem(
    methodName: String,
    methodIcon: Int,
    isSelected: Boolean,
    onSelect: (String) -> Unit,
    coin: String? = null,
    isEnable: Boolean = true,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        border = if (isSelected) BorderStroke(1.dp, SuccessDefault500) else BorderStroke(
            1.dp,
            BlackLight300
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                if (isEnable) onSelect(methodName) else {
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = if (isEnable) BlackWhite0 else BlackLight300,
            contentColor = BlackDark900
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = methodIcon), contentDescription = "",
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = methodName, style = Typography.titleLarge)
                if (!coin.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Image(
                            painter = painterResource(id = R.drawable.coin),
                            contentDescription = stringResource(
                                id = R.string.image_description_text
                            ),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = coin, style = Typography.bodyMedium)
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            if (isSelected) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_success),
                    contentDescription = "Selected",
                    tint = SuccessDefault500
                )
            }
        }
    }
}

@Composable
fun FooterSection(
    moneyPrice: Double?,
    coinPrice: Int?,
    modifier: Modifier,
    onBookingButtonClick: () -> Unit,
    authViewModel: AuthViewModel
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(BlackWhite0),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.total_text),
                    color = BlackLight400,
                    style = Typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    if (moneyPrice != null) {
                        Row {
                            Text(
                                text = String.format("$" + "%.2f", moneyPrice),
                                style = Typography.headlineMedium,
                                color = ErrorDefault500
                            )
                        }
                    }
                    if (coinPrice != null) {
                        Row {
                            if (moneyPrice != null) {
                                Text(
                                    text = "or",
                                    style = Typography.titleSmall,
                                    color = BlackLight300
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.coin),
                                    contentDescription = stringResource(
                                        id = R.string.image_description_text
                                    ),
                                    modifier = Modifier
                                        .size(20.dp)
                                )

                                Text(
                                    text = String.format("%d", coinPrice),
                                    style = Typography.headlineMedium,
                                    color = ErrorDefault500
                                )
                            }

                        }
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))
            }
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    onBookingButtonClick()
                },
                colors = ButtonColors(BrandDefault500, BlackDark900, BrandDefault500, BlackDark900),
                modifier = Modifier
                    .width(150.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.pay_text),
                    style = Typography.titleLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

enum class PaymentMethod {
    ZALO_PAY,
    TRAVER_COIN
}

