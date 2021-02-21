plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android-extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-android")
}

android {
    compileSdkVersion(Sdk.COMPILE_SDK_VERSION)

    defaultConfig {
        minSdkVersion(Sdk.MIN_SDK_VERSION)
        targetSdkVersion(Sdk.TARGET_SDK_VERSION)

        applicationId = AppCoordinates.APP_ID
        versionCode = AppCoordinates.APP_VERSION_CODE
        versionName = AppCoordinates.APP_VERSION_NAME
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    lintOptions {
        isWarningsAsErrors = true
        isAbortOnError = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk7"))
    implementation(project(":androidutils"))

    implementation(SupportLibs.ANDROIDX_APPCOMPAT)
    implementation(SupportLibs.ANDROIDX_CONSTRAINT_LAYOUT)
    implementation(SupportLibs.ANDROIDX_CORE_KTX)
    implementation(SupportLibs.MATERIAL)
    implementation(SupportLibs.ANDROIDX_NAVIGATION_FRAGMENT_KTX)
    implementation(SupportLibs.ANDROIDX_NAVIGATION_UI_KTX)
    implementation(SupportLibs.ANDROIDX_FRAGMENT_KTX)
    implementation(SupportLibs.ANDROIDX_LIFECYCLE_EXTENSIONS)
    implementation(SupportLibs.ANDROIDX_LIFECYCLE_VIEWMODEL_KTX)
    implementation(SupportLibs.ANDROIDX_LIFECYCLE_LIVEDATA_KTX)
    implementation(SupportLibs.ANDROIDX_ROOM_KTX)
    implementation(SupportLibs.ANDROIDX_ROOM_RUNTIME)
    implementation(SupportLibs.ANDROIDX_ROOM_COMPILER)
    implementation(SupportLibs.COROUTINES_CORE)
    implementation(SupportLibs.COROUTINES_ANDROID)
    implementation(SupportLibs.TIMBER)
    implementation(SupportLibs.RETROFIT2)
    implementation(SupportLibs.RETROFIT2_GSON)
    implementation(SupportLibs.OK_HTTP)
    implementation(SupportLibs.GLIDE)
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0")
    kapt(SupportLibs.GLIDE_COMPILER)
    implementation(SupportLibs.KOIN_ANDROID)
    implementation(SupportLibs.KOIN_VIEW_MODEL)
    implementation(SupportLibs.KOIN_SCOPE)

    testImplementation(TestingLib.JUNIT)

    androidTestImplementation(AndroidTestingLib.ANDROIDX_TEST_EXT_JUNIT)
    androidTestImplementation(AndroidTestingLib.ANDROIDX_TEST_RULES)
    androidTestImplementation(AndroidTestingLib.ESPRESSO_CORE)
}
