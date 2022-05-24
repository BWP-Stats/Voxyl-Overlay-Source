import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val overlayVersion = "0.17.0"

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose") version "1.1.1"
}

group = "com.voxyl"
version = overlayVersion

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    //no clue
    testImplementation(kotlin("test"))
    implementation(compose.desktop.currentOs)

    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

    //api stuff
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    //logging
    implementation("io.github.aakira:napier:2.5.0")

    //native stuff
    implementation("com.github.kwhat:jnativehook:2.2.2")

    //drag 'n' drop reordering
    implementation("org.burnoutcrew.composereorderable:reorderable:0.7.4")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}

compose.desktop {
    application {
        mainClass = "$group.overlay.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Exe, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Voxyl Overlay"
            packageVersion = overlayVersion

            modules("java.sql")

            windows {
                menu = true
                upgradeUuid = "bdc7677a-1f35-467c-908e-00e0f4d572cf".toUpperCase()
                iconFile.set(project.file("src/main/resources/VoxylLogoIcon.ico"))
                perUserInstall = true
            }
        }
    }
}