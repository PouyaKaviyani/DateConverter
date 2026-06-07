import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)

    id("maven-publish")
}

group = "com.github.PouyaKaviyani"
version = "1.0.0"

kotlin {

    androidLibrary {
        namespace = "com.pouyakaviyani.jalalidateconverter"
        compileSdk = 36
        minSdk = 30

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }


    }

    val xcfName = "JalaliDateConverterKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    sourceSets {

        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)

            // DateTime
            implementation(libs.kotlinx.datetime)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {

        }

        getByName("androidDeviceTest").dependencies {
            implementation(libs.androidx.core)
            implementation(libs.androidx.runner)
            implementation(libs.androidx.testExt.junit)
        }

        iosMain.dependencies {

        }
    }
}

publishing {
    repositories {
        mavenLocal()
    }
}
