package org.example.dateconverter

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import org.example.dateconverter.util.DateConverter

@Composable
@Preview
fun App() {
    MaterialTheme {

        DateConverterScreen()
    }
}


@Composable
fun DateConverterScreen() {
    val todayJalali = remember { DateConverter() }
    val todayGregorian = remember { todayJalali.toGregorian() }

    // رشته‌ها برای ورودی جلالی
    var jalaliYear by remember { mutableStateOf(todayJalali.year.toString()) }
    var jalaliMonth by remember { mutableStateOf(todayJalali.month.toString()) }
    var jalaliDay by remember { mutableStateOf(todayJalali.day.toString()) }

    // رشته‌ها برای ورودی میلادی
    var gregorianYear by remember { mutableStateOf(todayGregorian.year.toString()) }
    var gregorianMonth by remember { mutableStateOf(todayGregorian.monthNumber.toString()) }
    var gregorianDay by remember { mutableStateOf(todayGregorian.dayOfMonth.toString()) }

    // نتایج نمایش
    var gregorianResult by remember { mutableStateOf("") }
    var jalaliResult by remember { mutableStateOf("") }
    var jalaliDetails by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // بخش 1: نمایش تاریخ امروز
        Text("📅 امروز (جلالی): $todayJalali", style = MaterialTheme.typography.titleLarge)
        Text("📅 امروز (میلادی): $todayGregorian", style = MaterialTheme.typography.bodyLarge)

        // بخش 2: تبدیل جلالی → میلادی
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("ضروردی: جلالی → میلادی", style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = jalaliYear,
                        onValueChange = { jalaliYear = it },
                        label = { Text("سال (جلالی)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = jalaliMonth,
                        onValueChange = { jalaliMonth = it },
                        label = { Text("ماه") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = jalaliDay,
                        onValueChange = { jalaliDay = it },
                        label = { Text("روز") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                Button(onClick = {
                    runCatching {
                        val dc = DateConverter(jalaliYear.toInt(), jalaliMonth.toInt(), jalaliDay.toInt())
                        val g = dc.toGregorian()
                        gregorianResult = g.toString()
                    }.onFailure {
                        gregorianResult = "ورودی نامعتبر!"
                    }
                }) {
                    Text("تبدیل به میلادی")
                }
                if (gregorianResult.isNotEmpty()) {
                    Text("نتیجه: $gregorianResult")
                }
            }
        }

        // بخش 3: تبدیل میلادی → جلالی
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("ضروردی: میلادی → جلالی", style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = gregorianYear,
                        onValueChange = { gregorianYear = it },
                        label = { Text("سال (میلادی)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = gregorianMonth,
                        onValueChange = { gregorianMonth = it },
                        label = { Text("ماه") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = gregorianDay,
                        onValueChange = { gregorianDay = it },
                        label = { Text("روز") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                Button(onClick = {
                    runCatching {
                        val gDate = LocalDate(gregorianYear.toInt(), gregorianMonth.toInt(), gregorianDay.toInt())
                        val dc = DateConverter(gDate)
                        jalaliResult = dc.toString()
                        jalaliDetails = """
                            ماه: ${dc.getMonthName()}
                            کبیسه: ${if (dc.isLeap()) "بله" else "خیر"}
                            طول سال: ${dc.getYearLength()} روز
                            طول ماه: ${dc.getMonthLength()} روز
                            روز هفته: ${dc.getDayOfWeekName()}
                        """.trimIndent()
                    }.onFailure {
                        jalaliResult = "ورودی نامعتبر!"
                        jalaliDetails = ""
                    }
                }) {
                    Text("تبدیل به جلالی")
                }
                if (jalaliResult.isNotEmpty()) {
                    Text("نتیجه: $jalaliResult")
                    Text(jalaliDetails)
                }
            }
        }
    }
}
