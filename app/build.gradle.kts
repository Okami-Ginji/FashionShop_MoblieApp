plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.fashionshop"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.fashionshop"
        minSdk = 24
        targetSdk = 35
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
    buildFeatures{
        viewBinding= true
        dataBinding = true
    }
}

dependencies {
    implementation ("com.cloudinary:cloudinary-android:2.3.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(libs.firebase.database)
    implementation(libs.activity)
    implementation("androidx.activity:activity:1.10.1")
    implementation(fileTree(mapOf(
        "dir" to "D:\\PRM392\\FashionShop_FP\\FashionShopAndroid\\ZaloPayLib",
        "include" to listOf("*.aar", "*.jar")

    )))




    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.google.android.flexbox:flexbox:3.0.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.google.code.gson:gson:2.9.1")
    implementation("com.github.ismaeldivita:chip-navigation-bar:1.4.0")
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
}
