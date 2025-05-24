plugins {
    alias(libs.plugins.android) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.compose) apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.1") // 공식문서 최신 버전 사용
    }
}