object Sdk {
    const val MIN_SDK_VERSION = 21
    const val TARGET_SDK_VERSION = 30
    const val COMPILE_SDK_VERSION = 30
}

object Versions {
    const val ANDROIDX_TEST_EXT = "1.1.2"
    const val ANDROIDX_TEST = "1.3.0"
    const val APPCOMPAT = "1.2.0"
    const val CONSTRAINT_LAYOUT = "2.0.4"
    const val CORE_KTX = "1.2.0"
    const val ESPRESSO_CORE = "3.3.0"
    const val JUNIT = "4.13.1"
    const val KTLINT = "0.39.0"
    const val MATERIAL = "1.2.1"
    const val NAVIGATION = "2.3.2"
    const val LIFECYCLE = "2.2.0"
    const val COROUTINE = "1.3.9"
    const val ROOM = "2.2.6"
    const val FRAGMENT = "1.2.5"
    const val TIMBER = "4.7.1"
    const val RETROFIT = "2.7.2"
    const val OK_HTTP = "4.0.0"
    const val GLIDE = "4.12.0"
    const val KOIN = "2.2.2"
}

object BuildPluginsVersion {
    const val AGP = "4.1.0"
    const val DETEKT = "1.14.2"
    const val KOTLIN = "1.4.10"
    const val KTLINT = "9.4.1"
    const val VERSIONS_PLUGIN = "0.33.0"
}

object SupportLibs {
    const val ANDROIDX_APPCOMPAT = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
    const val ANDROIDX_CONSTRAINT_LAYOUT =
        "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
    const val ANDROIDX_CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"
    const val ANDROIDX_NAVIGATION_FRAGMENT_KTX =
        "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
    const val ANDROIDX_NAVIGATION_UI_KTX =
        "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"
    const val ANDROIDX_FRAGMENT_KTX = "androidx.fragment:fragment-ktx:${Versions.FRAGMENT}"
    const val ANDROIDX_LIFECYCLE_EXTENSIONS =
        "androidx.lifecycle:lifecycle-extensions:${Versions.LIFECYCLE}"
    const val ANDROIDX_LIFECYCLE_VIEWMODEL_KTX =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}"
    const val ANDROIDX_LIFECYCLE_LIVEDATA_KTX =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE}"
    const val ANDROIDX_ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"
    const val ANDROIDX_ROOM_COMPILER = "androidx.room:room-compiler:${Versions.ROOM}"
    const val COROUTINES_CORE =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINE}"
    const val COROUTINES_ANDROID =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINE}"
    const val TIMBER = "com.jakewharton.timber:timber:${Versions.TIMBER}"
    const val RETROFIT2 = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val RETROFIT2_GSON = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}"
    const val OK_HTTP = "com.squareup.okhttp3:logging-interceptor:${Versions.OK_HTTP}"
    const val GLIDE = "com.github.bumptech.glide:glide:${Versions.GLIDE}"
    const val GLIDE_COMPILER = "com.github.bumptech.glide:compiler:${Versions.GLIDE}"
    const val KOIN_ANDROID = "org.koin:koin-android:${Versions.KOIN}"
    const val KOIN_VIEW_MODEL = "org.koin:koin-androidx-viewmodel:${Versions.KOIN}"
    const val KOIN_SCOPE = "org.koin:koin-androidx-scope:${Versions.KOIN}"
}

object TestingLib {
    const val JUNIT = "junit:junit:${Versions.JUNIT}"
}

object AndroidTestingLib {
    const val ANDROIDX_TEST_RULES = "androidx.test:rules:${Versions.ANDROIDX_TEST}"
    const val ANDROIDX_TEST_RUNNER = "androidx.test:runner:${Versions.ANDROIDX_TEST}"
    const val ANDROIDX_TEST_EXT_JUNIT = "androidx.test.ext:junit:${Versions.ANDROIDX_TEST_EXT}"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE}"
}
