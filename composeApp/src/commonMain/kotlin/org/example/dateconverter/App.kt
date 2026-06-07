package org.example.dateconverter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import org.example.dateconverter.util.DateConverter
import org.example.dateconverter.util.IsoDateTime
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            //DateConverterScreen()
            MainScreen()
        }
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
    var gregorianMonth by remember { mutableStateOf(todayGregorian.month.number.toString()) }
    var gregorianDay by remember { mutableStateOf(todayGregorian.day.toString()) }

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




@Composable
fun MainScreen() {

    // ===== Jalali =====
    val jalaliYears = (1300..1500).toList()
    val jalaliMonths = (1..12).toList()

    var jy by remember { mutableStateOf(1403) }
    var jm by remember { mutableStateOf(1) }
    var jd by remember { mutableStateOf(1) }

    // ===== Gregorian =====
    val gregorianYears = (1900..2100).toList()
    val gregorianMonths = (1..12).toList()

    var gy by remember { mutableStateOf(2025) }
    var gm by remember { mutableStateOf(1) }
    var gd by remember { mutableStateOf(1) }

    // ===== Results =====
    var resultGregorian by remember { mutableStateOf("") }
    var resultJalali by remember { mutableStateOf("") }
    var resultIso by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    // ===== ISO =====
    var isoInput by remember { mutableStateOf("") }
    var isoGregorian by remember { mutableStateOf("") }
    var isoJalali by remember { mutableStateOf("") }

    // تاریخ اول
    var gy1 by remember { mutableStateOf(2025) }
    var gm1 by remember { mutableStateOf(1) }
    var gd1 by remember { mutableStateOf(1) }

    // تاریخ دوم
    var gy2 by remember { mutableStateOf(2025) }
    var gm2 by remember { mutableStateOf(12) }
    var gd2 by remember { mutableStateOf(20) }

    var now by remember { mutableStateOf("") }

    LaunchedEffect(Unit){
        while (true){
            now = IsoDateTime.nowIso()
            delay(100L)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Text( "ISO-8601: " + now, style = MaterialTheme.typography.titleLarge)

        // ================= Jalali Section =================
        Text("📍 ورود تاریخ جلالی", style = MaterialTheme.typography.titleLarge)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SimpleDropdown(
                "سال",
                jalaliYears,
                jy,
                onSelect = { jy = it }
            )
            SimpleDropdown(
                "ماه",
                jalaliMonths,
                jm,
                onSelect = { jm = it }
            )

            val maxDay = DateConverter(jy, jm, 1).getMonthLength()
            jd = IsoDateTime.safeJalaliDay(jy, jm, jd)

            SimpleDropdown(
                "روز",
                (1..maxDay).toList(),
                jd,
                onSelect = { jd = it }
            )

        }

        Button(onClick = {
            runCatching {
                val safeDay = IsoDateTime.safeJalaliDay(jy, jm, jd)
                val dc = DateConverter(jy, jm, safeDay)
                val gDate = dc.toGregorian()


                resultJalali = dc.toString()
                resultGregorian = gDate.toString()
                resultIso = IsoDateTime.jalaliToIso(dc)

                error = ""
            }.onFailure {
                error = "تاریخ جلالی نامعتبر"
            }
        }) {
            Text("تبدیل جلالی → ISO + نمایش")
        }

        Divider()

        // ================= Gregorian Section =================
        Text("📍 ورود تاریخ میلادی", style = MaterialTheme.typography.titleLarge)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SimpleDropdown(
                "سال",
                gregorianYears,
                gy,
                onSelect = { gy = it }
            )
            SimpleDropdown(
                "ماه",
                gregorianMonths,
                gm,
                onSelect = { gm = it }
            )


            val maxDay = IsoDateTime.gregorianMonthLength(gy, gm)
            gd = IsoDateTime.safeGregorianDay(gy, gm, gd)

            SimpleDropdown(
                "روز",
                (1..maxDay).toList(),
                gd,
                onSelect = { gd = it }
            )
        }

        Button(onClick = {
            runCatching {
                val safeDay = IsoDateTime.safeGregorianDay(gy, gm, gd)
                val gDate = LocalDate(gy, gm, safeDay)


                resultGregorian = gDate.toString()
                resultJalali = gDate.toString()
                resultIso = IsoDateTime.gregorianToIso(gDate)

                error = ""
            }.onFailure {
                error = "تاریخ میلادی نامعتبر"
            }
        }) {
            Text("تبدیل میلادی → ISO + نمایش")
        }

        Divider()

        // ================= ISO Section =================
        Text("📍 ورود تاریخ ISO", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = isoInput,
            onValueChange = { isoInput = it },
            label = { Text("YYYY-MM-DDTHH:mm:ss") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("مثال: 2025-01-14T08:30:00") }
        )

        Button(onClick = {
            runCatching {
                val gDate = IsoDateTime.isoToGregorianDate(isoInput)
                val jDate = IsoDateTime.isoToJalali(isoInput)

                isoGregorian = gDate.toString()
                isoJalali = jDate.toString()

                error = ""
            }.onFailure {
                error = "فرمت ISO نامعتبر است"
                isoGregorian = ""
                isoJalali = ""
            }
        }) {
            Text("تبدیل ISO → نمایش")
        }

        if (isoGregorian.isNotEmpty()) {
            Text("📅 میلادی (از ISO): $isoGregorian")
            Text("📅 جلالی (از ISO): $isoJalali")
        }


        Divider()

        // ================= Results =================
        Text("📊 خروجی نهایی", style = MaterialTheme.typography.titleLarge)

        if (error.isNotEmpty()) {
            Text(error, color = MaterialTheme.colorScheme.error)
        }

        Text("📅 میلادی: $resultGregorian")
        Text("📅 جلالی: $resultJalali")
        Text("💾 ISO (برای دیتابیس):")
        Text(resultIso, style = MaterialTheme.typography.bodyMedium)

        Divider()

        // ================= محاسبه فاصله بین دو تاریخ میلادی (ورودی دستی) =================
        Text("⏱ محاسبه فاصله بین دو تاریخ میلادی", style = MaterialTheme.typography.titleLarge)

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            // --- تاریخ اول ---
            Text("تاریخ اول", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SimpleDropdown(
                    "سال",
                    gregorianYears,
                    gy1,
                    onSelect = { gy1 = it },
                    modifier = Modifier.weight(1f)
                )
                SimpleDropdown(
                    "ماه",
                    gregorianMonths,
                    gm1,
                    onSelect = { gm1 = it },
                    modifier = Modifier.weight(1f)
                )

                val maxDay1 = IsoDateTime.gregorianMonthLength(gy1, gm1)
                val safeGd1 = gd1.coerceIn(1, maxDay1)

                SimpleDropdown(
                    "روز",
                    (1..maxDay1).toList(),
                    safeGd1,
                    onSelect = { gd1 = it },
                    modifier = Modifier.weight(1f)
                )
            }

            // --- تاریخ دوم ---
            Text("تاریخ دوم", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SimpleDropdown(
                    "سال",
                    gregorianYears,
                    gy2,
                    onSelect = { gy2 = it },
                    modifier = Modifier.weight(1f)
                )
                SimpleDropdown(
                    "ماه",
                    gregorianMonths,
                    gm2,
                    onSelect = { gm2 = it },
                    modifier = Modifier.weight(1f)
                )

                val maxDay2 = IsoDateTime.gregorianMonthLength(gy2, gm2)
                val safeGd2 = gd2.coerceIn(1, maxDay2)

                SimpleDropdown(
                    "روز",
                    (1..maxDay2).toList(),
                    safeGd2,
                    onSelect = { gd2 = it },
                    modifier = Modifier.weight(1f)
                )
            }

            // محاسبه تاریخ‌ها
            val date1 = remember(gy1, gm1, gd1) {
                runCatching { LocalDate(gy1, gm1, gd1.coerceIn(1, IsoDateTime.gregorianMonthLength(gy1, gm1))) }.getOrNull()
            }

            val date2 = remember(gy2, gm2, gd2) {
                runCatching { LocalDate(gy2, gm2, gd2.coerceIn(1, IsoDateTime.gregorianMonthLength(gy2, gm2))) }.getOrNull()
            }

            if (date1 != null && date2 != null) {
                val differenceText = IsoDateTime.gregorianDateDifference(date1, date2)
                val daysCount = IsoDateTime.gregorianDaysBetween(date1, date2)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "فاصله تقریبی:",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = differenceText,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "دقیق: $daysCount روز",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        if (daysCount == 0L) {
                            Text("➡️ دو تاریخ یکسان هستند", color = Color.Gray)
                        }
                    }
                }
            } else {
                Text("یکی از تاریخ‌ها نامعتبر است", color = MaterialTheme.colorScheme.error)
            }
        }

    }
}


@Composable
fun <T> SimpleDropdown(
    label: String,
    items: List<T>,
    selected: T,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { expanded = true },
        modifier = modifier
    ) {
        Text("$label: $selected")
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        items.forEach {
            DropdownMenuItem(
                text = { Text(it.toString()) },
                onClick = {
                    onSelect(it)
                    expanded = false
                }
            )
        }
    }
}
