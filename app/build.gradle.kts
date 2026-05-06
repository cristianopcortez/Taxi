// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kotlinKsp)
    alias(libs.plugins.compose.compiler)
}

fun sharedDebugKeystorePath(): String? =
    System.getenv("SHARED_DEBUG_KEYSTORE_PATH")?.trim()?.takeIf { it.isNotEmpty() }

android {
    namespace = "br.com.ccortez.taxi"
    compileSdk = 36

    defaultConfig {
        applicationId = "br.com.ccortez.taxi"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["androidx.test.uiautomator.debug.UiAutomatorTimeout"] = "60000" // Increase timeout in milliseconds (default 20000)
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    signingConfigs {
        sharedDebugKeystorePath()?.also { ksPath ->
            create("sharedDebug") {
                storeFile = file(ksPath)
                storePassword = System.getenv("KEYSTORE_PASSWORD") ?: ""
                keyAlias = System.getenv("KEY_ALIAS") ?: ""
                keyPassword = System.getenv("KEY_PASSWORD") ?: ""
            }
        }
    }

    buildTypes {
        debug {
            sharedDebugKeystorePath()?.takeIf { file(it).exists() }?.also {
                signingConfig = signingConfigs.getByName("sharedDebug")
            }
        }

        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    lint {
        baseline = file("lint-baseline.xml")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }
    tasks.withType<Test> {
        useJUnitPlatform()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
        }
    }
}

composeCompiler {
    enableStrongSkippingMode = true
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
}

dependencies {
    // Core Common
    implementation(project(":core:common"))
    implementation(project(":core:feature"))

    // Feature Taxi Travel Options
    implementation(project(":feature:taxi_travel_options:ui"))
    implementation(project(":feature:taxi_travel_options:domain"))
    implementation(project(":feature:taxi_travel_options:data"))

    // Feature taxi_travel_available_riders
    implementation(project(":feature:taxi_travel_available_riders:ui"))
    implementation(project(":feature:taxi_travel_available_riders:domain"))
    implementation(project(":feature:taxi_travel_available_riders:data"))

    // Libraries
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.material.icons.extended)
    implementation(libs.navigation.compose)
    implementation(libs.splashscreen)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.hilt)
    implementation(libs.hilt.testing)
    implementation(libs.google.gson)
    implementation(libs.androidx.uiautomator)
    testImplementation(project(":core:network"))
    ksp(libs.hilt.compiler)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.api)
    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.robolectric)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.junit.jupiter)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(libs.core.testing)
    testImplementation(libs.nhaarman.mockito.kotlin)
    testImplementation(libs.lifecycle.common)
    androidTestImplementation(libs.kotlinx.coroutines.core)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.junit.api)
    androidTestImplementation(libs.junit.engine)
    androidTestImplementation(libs.mannodermaus.android.test.core)
    androidTestImplementation(libs.mannodermaus.android.test.runner)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    runtimeOnly(libs.androidx.test.uiautomator) // exclude group: 'androidx.test', module: 'uiautomator'
    androidTestImplementation(libs.androidx.test.orchestrator)
    androidTestUtil(libs.androidx.test.orchestrator)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.mockito.core)
    androidTestImplementation(libs.mockito.android)
    androidTestImplementation(libs.mockito.junit.jupiter)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(project(":core:network"))
    androidTestImplementation(libs.navigation.testing)
    // Add this line:
    androidTestImplementation(libs.ui.test.junit4)

    // You likely already have this, but make sure it's present:
    androidTestImplementation(libs.ui.test.manifest)

    //if you have problems add this too
    debugImplementation(libs.ui.test.manifest)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.androidx.test.monitor) {
        isTransitive = false
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
