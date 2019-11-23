@file:Suppress("MayBeConstant")

package dependencies

object Versions {
    val kotlin = "1.3.60"
    val glide = "4.10.0"
    val dagger = "2.23.2"
    val retrofit = "2.6.1"
    val robolectric = "4.3.1"
    val mockito = "3.1.0"

    object AndroidX {
        // https://developer.android.com/jetpack/androidx/versions
        val appcompat = "1.1.0"
        val core = "1.1.0"
        val lifecycle = "2.2.0-rc02"
        val navigation = "2.2.0-rc02"
        val fragment = "1.2.0-rc02"
        val arch = "2.1.0"

        object Test {
            // https://developer.android.com/jetpack/androidx/releases/test
            val core = "1.2.0"
            val espresso = "3.2.0"
            val junit = "1.1.1"
            val runner = "1.2.0"
            val orchestrator = "1.2.0"
        }
    }
}

object Deps {
    object GradlePlugin {
        val android = "com.android.tools.build:gradle:3.5.0"
        val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        val kotlinAllOpen = "org.jetbrains.kotlin:kotlin-allopen:${Versions.kotlin}"
        val navigationSafeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.AndroidX.navigation}"
        val versionsPlugin = "com.github.ben-manes:gradle-versions-plugin:0.27.0"
    }

    object Kotlin {
        val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    }

    object AndroidX {
        val appcompat = "androidx.appcompat:appcompat:${Versions.AndroidX.appcompat}"
        val coreKtx = "androidx.core:core-ktx:${Versions.AndroidX.core}"
        val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.AndroidX.lifecycle}"
        val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.AndroidX.lifecycle}"
        val navigationRuntimeKtx = "androidx.navigation:navigation-runtime-ktx:${Versions.AndroidX.navigation}"
        val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.AndroidX.navigation}"
        val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.AndroidX.navigation}"
    }

    object Glide {
        val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
        val annotations = "com.github.bumptech.glide:annotations:${Versions.glide}"
        val okhttp3Integration = "com.github.bumptech.glide:okhttp3-integration:${Versions.glide}"
        val compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    }

    object Dagger {
        val dagger = "com.google.dagger:dagger:${Versions.dagger}"
        val compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
        val android = "com.google.dagger:dagger-android:${Versions.dagger}"
        val androidSupport = "com.google.dagger:dagger-android-support:${Versions.dagger}"
        val androidProcessor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"
    }

    val photoView = "com.github.chrisbanes:PhotoView:2.3.0"

    object Retrofit {
        val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
        val converterMoshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
        val retrofitMock = "com.squareup.retrofit2:retrofit-mock:${Versions.retrofit}"
    }

    val moshi = "com.squareup.moshi:moshi:1.8.0"

    object Test {
        val archCoreTesting = "androidx.arch.core:core-testing:${Versions.AndroidX.arch}"
        val fragmentTesting = "androidx.fragment:fragment-testing:${Versions.AndroidX.fragment}"

        val androidJunit = "androidx.test.ext:junit:${Versions.AndroidX.Test.junit}"
        val runner = "androidx.test:runner:${Versions.AndroidX.Test.runner}"
        val espressoCore = "androidx.test.espresso:espresso-core:${Versions.AndroidX.Test.espresso}"
        val espressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.AndroidX.Test.espresso}"
        val coreKtx = "androidx.test:core-ktx:${Versions.AndroidX.Test.core}"
        val orchestrator = "androidx.test:orchestrator:${Versions.AndroidX.Test.orchestrator}"

        val junit = "junit:junit:4.12"

        val mockitoCore = "org.mockito:mockito-core:${Versions.mockito}"
        val mockitoAndroid = "org.mockito:mockito-android:${Versions.mockito}"

        val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
        val robolectricAnnotations = "org.robolectric:annotations:${Versions.robolectric}"
    }
}
