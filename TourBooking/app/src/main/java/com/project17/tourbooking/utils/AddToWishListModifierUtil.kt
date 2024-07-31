package com.project17.tourbooking.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.project17.tourbooking.ui.theme.BlackWhite0

fun Modifier.iconWithBackgroundModifier(): Modifier {
    return this
        .padding(16.dp)
        .clip(CircleShape)
        .background(BlackWhite0)
        .padding(8.dp)
}
fun Modifier.iconWithoutBackgroundModifier(): Modifier {
    return this
        .background(Color.Transparent)
        .padding(8.dp)
}
