package org.pouyakaviyani.dateconverter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.pouyakaviyani.dateconverter.util.DateConverter

@Composable
fun rememberJalaliDate(): DateConverter {
    return remember {
        DateConverter()
    }
}