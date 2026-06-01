plugins {
    id("com.android.application")
}

android {
    namespace = "com.cookandroid.password"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.cookandroid.password"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    // 1. 기존 버전을 현재 환경에 맞게 안정적인 버전으로 낮춥니다.
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // 2. 숨어있던 에러의 원인(core-ktx)을 강제로 낮추는 코드를 한 줄 추가합니다. ⭐
    implementation("androidx.core:core-ktx:1.13.1")

    // --- 아래 3줄(테스트 코드)은 원래 적혀있던 내용 그대로 둡니다 ---
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.0")
}