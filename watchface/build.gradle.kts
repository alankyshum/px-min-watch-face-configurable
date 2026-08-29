plugins { id("com.android.application") }
android {
    namespace = "dev.alanshum.configurableminimal"
    compileSdk = 34
    defaultConfig { applicationId = "dev.alanshum.configurableminimal"; minSdk = 34; targetSdk = 34; versionCode = 1; versionName = "1.0.0" }
    buildTypes { release { isMinifyEnabled = true; isShrinkResources = false; signingConfig = signingConfigs.getByName("debug") } }
}
