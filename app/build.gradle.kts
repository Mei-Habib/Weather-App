import java.io.FileInputStream
import java.util.Properties


val localProperties = Properties()
val localPropertiesFile = File(rootDir, "secrets.properties")
if (localPropertiesFile.exists() && localPropertiesFile.isFile) {
    localPropertiesFile.inputStream().use {
        localProperties.load(it)
    }
}

val file = File(rootProject.rootProject.rootDir, ("local.properties"))
val properties = Properties()
properties.load(FileInputStream(file))

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
//    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")


    kotlin("plugin.serialization") version "2.1.10"
//    id("org.jetbrains.kotlin.plugin.compose") version "2.1.10"
    id("kotlin-kapt")
}

android {
    namespace = "com.example.weather_app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.weather_app"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        manifestPlaceholders["MAP_API_KEY"] = project.findProperty("MAP_API_KEY") ?: ""
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "API_KEY", properties.getProperty("API_KEY"))
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            val googleMapsApiKey = localProperties.getProperty("MAP_API_KEY") ?: ""
            resValue("string", "map_api_key", googleMapsApiKey)
        }

        debug {
            val googleMapsApiKey = localProperties.getProperty("MAP_API_KEY") ?: ""
            resValue("string", "map_api_key", googleMapsApiKey)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //curved bottom navigation bar
    implementation(libs.curved.bottom.navigation)

    //compose navigation & serialization
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.1.0")
    implementation("androidx.navigation:navigation-compose:2.8.8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    //To use constraintlayout in compose
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.1")

    //gson
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //okhttp3
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //Glide
    implementation("com.github.bumptech.glide:compose:1.0.0-beta01")

    //Room Database
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")


    //LiveData & Compose
    val compose_version = "1.0.0"
    implementation("androidx.compose.runtime:runtime-livedata:$compose_version")

    //compose viewmodel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose-android:2.8.7")

    //lottie
    implementation("com.airbnb.android:lottie-compose:6.1.0")


    // Google maps Compose
    implementation(libs.maps.compose)

    // Google Maps SDK for Android
    implementation(libs.places)
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)

    //workManger
    implementation("androidx.work:work-runtime:2.9.0")

    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    implementation("androidx.compose.material:material:1.5.4")
//    ksp("androidx.room:room-compiler:2.6.1")

    //Swipe
    implementation("me.saket.swipe:swipe:1.1.1")

    implementation("com.google.accompanist:accompanist-permissions:0.31.1-alpha")

    //////Test
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation("androidx.test:core-ktx:1.5.0")
    testImplementation("org.robolectric:robolectric:4.11.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")
    testImplementation("app.cash.turbine:turbine:1.0.0")


    // InstantTaskExecutorRule
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation("androidx.arch.core:core-testing:2.1.0")
}