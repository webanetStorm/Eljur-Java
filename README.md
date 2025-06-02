# Электронный дневник

## Описание проекта
Мобильное приложение «Электронный дневник» реализовано на Java с использованием Android SDK и Firebase.  
Приложение позволяет ученикам и родителям:
- Просматривать оценки за учебный год;
- Следить за расписанием уроков и перемен;
- Просматривать домашние задания;
- Управлять личным профилем.

## Основные возможности
- Аутентификация через Firebase Auth (Email/Password);
- Хранение данных успеваемости и расписания в Firebase;
- Просмотр динамики успеваемости;
- Поддержка тёмной темы.

## Зависимости и плагины
Проект использует Gradle Kotlin DSL (версия Gradle и плагины указаны в `settings.gradle.kts` и `build.gradle.kts`). Ниже—выдержка из `app/build.gradle.kts`:

```kotlin
plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.eljur"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.eljur"
        minSdk = 27
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
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // UI и архитектура
    implementation(libs.appcompat.v161)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.cardview)
    implementation(libs.flexbox)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // JSON и загрузка изображений
    implementation(libs.gson)
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

    // Lifecycle & ViewModel
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)

    // Firebase (BOM + компоненты)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.core)
    implementation(libs.firebase.crashlytics.buildtools)

    // Тестирование
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
