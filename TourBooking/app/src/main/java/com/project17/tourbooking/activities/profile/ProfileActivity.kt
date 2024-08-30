import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project17.tourbooking.R
import com.project17.tourbooking.ui.theme.Typography


@Composable
fun ProfileActivity(navController: NavController) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val name = "Cristiano"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "List Your Trip",
            style = Typography.headlineLarge,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
        ) {
            Image(
                painter = painterResource(id = R.drawable.avatar_placeholder),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )


            Column {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Hello, $name", style = Typography.headlineSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Địa chỉ người dùng", style = Typography.bodyMedium)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ProfileCard(
            "Personal Information",
            R.drawable.ic_person,
            onClick = { navController.navigate("personal_information") })

        Spacer(modifier = Modifier.height(16.dp))

        ProfileCard(
            "Notification",
            R.drawable.ic_notification,
            onClick = { navController.navigate("notification") })

        Spacer(modifier = Modifier.height(16.dp))

        ProfileCard("FAQ", R.drawable.ic_help, onClick = { navController.navigate("faq") })

        Spacer(modifier = Modifier.height(16.dp))

        ProfileCard(
            "Language",
            R.drawable.ic_language,
            onClick = { navController.navigate("language") })

        Spacer(modifier = Modifier.height(16.dp))

        ProfileCard("Logout", R.drawable.ic_logout) {
            showLogoutDialog = true
        }

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = {
                    Text(
                        text = "Logout",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = Typography.titleLarge
                    )
                },
                text = {
                    Text(
                        text = buildAnnotatedString {
                            append("Are you sure you want to log out of ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(name)
                            }
                            append("'s account?")
                        },
                        style = Typography.bodyLarge
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            navController.navigate("login")
                            showLogoutDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCD240))
                    ) {
                        Text("Logout")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showLogoutDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun ProfileCard(title: String, iconResId: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, style = Typography.titleLarge)
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = title,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}