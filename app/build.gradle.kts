import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.kotlinAndroid)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.kotlinKsp)
}

android {
  namespace = "com.acv.chat"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.acv.chat"
    minSdk = 29
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }

    val keystoreFile = project.rootProject.file("local.properties")
    val properties = Properties()
    properties.load(keystoreFile.inputStream())
    val apiKey = properties.getProperty("API_ENDPOINT") ?: ""
    buildConfigField("String", "API_ENDPOINT", apiKey)
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.3"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
      excludes += "META-INF/INDEX.LIST"
      excludes += "META-INF/DEPENDENCIES"
    }
  }
}

kotlin {
  compilerOptions {
    freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    freeCompilerArgs.add("-Xcontext-receivers")
    freeCompilerArgs.add("-Xenable-builder-inference")
  }
}

dependencies {

  implementation(libs.core.ktx)
  implementation(libs.lifecycle.runtime.ktx)
  implementation(libs.activity.compose)
  implementation(platform(libs.compose.bom))
  implementation(libs.ui)
  implementation(libs.ui.graphics)
  implementation(libs.ui.tooling.preview)
  implementation(libs.material3)
  implementation(libs.coil)

  implementation(libs.arrow.optics)
  ksp(libs.arrow.optics.ksp)

  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.serialization)
  implementation(libs.okio)

  implementation(libs.ktor.client.core)
  implementation(libs.ktor.client.cio)
  implementation(libs.ktor.client.logging)
  implementation(libs.ktor.client.auth)
  implementation(libs.ktor.client.contentNegotiation)
  implementation(libs.ktor.serialization.json)

  testImplementation(libs.junit)
  testImplementation(libs.kotlinx.coroutines.test)

  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.espresso.core)
  androidTestImplementation(platform(libs.compose.bom))
  androidTestImplementation(libs.ui.test.junit4)

  debugImplementation(libs.ui.tooling)
  debugImplementation(libs.ui.test.manifest)
}