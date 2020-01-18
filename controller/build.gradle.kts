plugins {
    kotlin("jvm") version Dependencies.Versions.Kotlin apply false
}

subprojects {
    repositories {
        mavenLocal()
        jcenter()
        maven { url = uri("https://kotlin.bintray.com/ktor") }
        maven { url = uri("https://kotlin.bintray.com/kotlin-js-wrappers") }
    }
}
