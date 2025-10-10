plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
}

android {
    namespace = "upc.edu.pe.levelupjourney"
    compileSdk = 36

    defaultConfig {
        applicationId = "upc.edu.pe.levelupjourney"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    
    // --- Jetpack Compose (UI base) ---
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
    
    // --- Google Fonts ---
    implementation("androidx.compose.ui:ui-text-google-fonts:1.7.5")

    // --- Navegación en Compose ---
    implementation("androidx.navigation:navigation-compose:2.8.3")

    // --- Corrutinas (para tareas asíncronas, sockets, peticiones) ---
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    // --- HTTP y WebSockets (Ktor client) ---
    implementation("io.ktor:ktor-client-android:3.0.0")
    implementation("io.ktor:ktor-client-content-negotiation:3.0.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.0")
    implementation("io.ktor:ktor-client-websockets:3.0.0")

    // --- Autenticación con Google ---
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // --- Autenticación con GitHub (OAuth mediante navegador) ---
    implementation("androidx.browser:browser:1.8.0")

    // --- Base de datos local (Room) ---
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // --- Manejo de imágenes (Coil) ---
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("io.coil-kt:coil-svg:2.6.0")

    // --- JSON más flexible (si prefieres Gson en lugar de kotlinx) ---
    implementation("com.google.code.gson:gson:2.11.0")

    // --- Networking with Retrofit (for IAM integration) ---
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // --- DataStore for secure token storage ---
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // --- Animaciones y transiciones fluidas ---
    implementation("androidx.compose.animation:animation")
    
    // --- CameraX para escaneo de QR ---
    implementation("androidx.camera:camera-core:1.3.1")
    implementation("androidx.camera:camera-camera2:1.3.1")
    implementation("androidx.camera:camera-lifecycle:1.3.1")
    implementation("androidx.camera:camera-view:1.3.1")
    
    // --- ZXing para procesamiento de códigos QR ---
    implementation("com.google.zxing:core:3.5.2")
    
    // --- Accompanist para permisos ---
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")

    // --- Testing dependencies ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
