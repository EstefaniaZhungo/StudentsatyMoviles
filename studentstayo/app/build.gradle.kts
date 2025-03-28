plugins {
    id("com.android.application")
    //id("com.google.gms.google-services")
}

android {
    namespace = "com.tefa.studentstayo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tefa.studentstayo"
        minSdk = 33
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
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.android.volley:volley:1.2.0")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation("androidx.compose.animation:animation-graphics-android:1.6.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.google.android.material:material:1.4.0")
    implementation ("com.google.code.gson:gson:2.8.9")
    implementation ("com.google.android.material:material:1.4.0")
    implementation ("org.osmdroid:osmdroid-android:6.1.16")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

}