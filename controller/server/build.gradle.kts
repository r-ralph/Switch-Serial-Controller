plugins {
    application
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.serialization") version Dependencies.Versions.Kotlin
}

group = "ms.ralph"
version = "0.0.1-SNAPSHOT"

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Dependencies.Versions.Kotlin}")
    implementation("io.ktor:ktor-server-netty:${Dependencies.Versions.Ktor}")
    implementation("ch.qos.logback:logback-classic:${Dependencies.Versions.Logback}")
    implementation("io.ktor:ktor-server-core:${Dependencies.Versions.Ktor}")
    implementation("io.ktor:ktor-html-builder:${Dependencies.Versions.Ktor}")
    implementation("org.jetbrains:kotlin-css-jvm:1.0.0-pre.31-kotlin-1.2.41")
    implementation("io.ktor:ktor-server-host-common:${Dependencies.Versions.Ktor}")
    implementation("io.ktor:ktor-serialization:${Dependencies.Versions.Ktor}")
    implementation("io.ktor:ktor-websockets:${Dependencies.Versions.Ktor}")
    implementation("com.github.purejavacomm:purejavacomm:1.0.1.RELEASE")
    implementation("io.reactivex.rxjava3:rxjava:${Dependencies.Versions.RxJava}")
    testImplementation("io.ktor:ktor-server-tests:${Dependencies.Versions.Ktor}")
}
