import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kotlinKsp)
}

val secretsFile = rootProject.file("secrets.properties")
val properties = Properties().apply {
    if (secretsFile.exists()) {
        load(secretsFile.inputStream())
    }
}

// Never interpolate `properties[key]` raw: missing keys yield Kotlin "null" in the string → invalid Retrofit baseUrl at runtime (E2E/CI crash).
private fun sanitizeApiBaseUrl(raw: String?): String {
    val t = raw?.trim().orEmpty()
    if (t.isEmpty()) return ""

    val noScheme =
        !(t.startsWith("http://", ignoreCase = true) || t.startsWith("https://", ignoreCase = true))
    val withScheme =
        if (noScheme) "https://$t" else t
    return withScheme.trimEnd('/') + '/'
}

val apiBaseUrlForBuildConfig: String = sanitizeApiBaseUrl(properties.getProperty("API_BASE_URL"))
    .ifEmpty { "https://example.com/" }

android {
    namespace = "br.com.ccortez.core.network"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            android.buildFeatures.buildConfig = true
            buildConfigField("String", "API_BASE_URL", "\"$apiBaseUrlForBuildConfig\"")
        }
        release {
            isMinifyEnabled = false
            android.buildFeatures.buildConfig = true
            buildConfigField("String", "API_BASE_URL", "\"$apiBaseUrlForBuildConfig\"")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
    tasks.withType<Test> {
        useJUnitPlatform()
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)

    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx.converter)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.google.gson)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.engine)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
