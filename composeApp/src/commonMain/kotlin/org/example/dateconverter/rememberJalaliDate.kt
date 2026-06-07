package org.example.dateconverter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.example.dateconverter.util.DateConverter

@Composable
fun rememberJalaliDate(): DateConverter {
    return remember {
        DateConverter()
    }
}