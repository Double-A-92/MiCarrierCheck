plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.amedeo.micarriercheck"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.amedeo.micarriercheck"
        minSdk = 24
        targetSdk = 34

        // Use the passed property, fallback to "1.0.0" for normal builds
        versionName = project.findProperty("versionName") as String? ?: "1.0.0"

        // Auto-generate versionCode from versionName (e.g., 0.2 → 200)
        versionCode = versionName?.split(".")?.map { it.toIntOrNull() ?: 0 }
            .let { parts ->
                parts?.let {
                    (it.getOrElse(0) { 0 }) * 10000 +
                    (it.getOrElse(1) { 0 }) * 100 +
                    (it.getOrElse(2) { 0 })
                }
            }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    // --- Conditional signing config (only used when environment variables exist) ---
    signingConfigs {
        val keystorePath = System.getenv("KEYSTORE_FILE_PATH")
        if (keystorePath != null) {
            create("ciRelease") {
                storeFile = file(keystorePath)
                storePassword = System.getenv("KEYSTORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            // Use the CI signing config if available, otherwise leave unsigned
            signingConfigs.findByName("ciRelease")?.let {
                signingConfig = it
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.18.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
    implementation("androidx.activity:activity-compose:1.13.0")
    implementation(platform("androidx.compose:compose-bom:2026.04.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("io.coil-kt:coil-svg:2.7.0")
    implementation("com.materialkolor:material-kolor:4.1.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    androidTestImplementation(platform("androidx.compose:compose-bom:2026.04.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}