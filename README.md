# DateConverter KMP

A lightweight Kotlin Multiplatform library for converting dates between Gregorian and Jalali (Persian) calendars.

## Features

* Gregorian → Jalali conversion
* Jalali → Gregorian conversion
* Jalali leap year detection
* Persian month names
* Persian day of week names
* ISO DateTime utilities
* Android & iOS support
* Compose Multiplatform compatible

---

## Installation

### Step 1: Add JitPack

Add JitPack to your `settings.gradle.kts`

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

### Step 2: Add Dependency

```kotlin
dependencies {
    implementation("com.github.PouyaKaviyani:DateConverter:v1.0.0")
}
```

## Supported Platforms

* Android
* iOS
* Compose Multiplatform

---

## License

MIT License
