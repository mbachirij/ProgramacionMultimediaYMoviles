plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.todoapp"
    compileSdk = 35 // Ajustado a 35 (36 es experimental/preview a veces y da problemas)

    defaultConfig {
        applicationId = "com.example.todoapp"
        minSdk = 26 // 34 es muy alto para minSdk (solo Pixel 8+), 24-26 es estándar
        targetSdk = 34 // Ajustado a estable
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
    // --- FIREBASE ---
    // Importante: Usamos el BOM para que gestione las versiones compatibles
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))

    // Al usar el BOM, NO ponemos número de versión en estas líneas:
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-analytics")

    // --- ANDROID & COMPOSE ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // BOM de Compose (Gestiona versiones de UI)
    implementation(platform(libs.androidx.compose.bom))

    // Librerías de UI (Sin versión, la cogen del BOM)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // Navegación
    implementation(libs.androidx.navigation.compose)

    // Iconos extendidos (Opcional, si lo usas)
    // Nota: Si 'libs.androidx...' da error, usa la línea comentada de abajo:
    // implementation("androidx.compose.material:material-icons-extended")

    // --- TESTING ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}