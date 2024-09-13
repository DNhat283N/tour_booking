package com.project17.tourbooking.utils.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.project17.tourbooking.ui.theme.Typography

@Composable
fun CommonAlertDialog(
    isDialogVisible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: Int,
    message: Int,
    confirmButtonText: Int,
    dismissButtonText: Int
){
    if(isDialogVisible){
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = stringResource(id = title),
                    style = Typography.titleLarge
                )
            },
            text = {
                Text(
                    text = stringResource(id = message),
                    style = Typography.titleMedium
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm()
                }) {
                    Text(
                        text = stringResource(id =confirmButtonText),
                        style = Typography.titleSmall
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDismiss()
                }) {
                    Text(
                        text = stringResource(id = dismissButtonText),
                        style = Typography.titleSmall
                    )
                }
            }
        )
    }
}