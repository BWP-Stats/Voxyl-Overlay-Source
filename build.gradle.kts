import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose") version "1.1.1"
}

group = "com.voxyl"
version = "1.3.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(compose.desktop.currentOs)

    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

    //api stuff
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.7")

    //logging
    implementation("io.github.aakira:napier:2.5.0")

    //native stuff
    implementation("com.github.kwhat:jnativehook:2.2.2")

    //drag 'n' drop reordering
    implementation("org.burnoutcrew.composereorderable:reorderable:0.7.4")

    //discord rpc
    implementation("com.github.JnCrMx:discord-game-sdk4j:v0.5.5")

    implementation("cc.popkorn:popkorn:2.2.0")

    //reflection
    implementation("org.reflections:reflections:0.10.2")
    implementation(kotlin("reflect"))
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
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "VoxylOverlay"
            packageVersion = version as String

            modules("java.sql")

            windows {
                menu = true
                upgradeUuid = "bdc7677a-1f35-467c-908e-00e0f4d572cf".toUpperCase()
                iconFile.set(project.file("src/main/resources/logos/VoxylLogoIcon.ico"))
                perUserInstall = true
            }

            linux {
                shortcut = true
                iconFile.set(project.file("src/main/resources/logos/VoxylLogoIcon.png"))
            }

            macOS {
                iconFile.set(project.file("src/main/resources/logos/VoxylLogoIcon.icns"))
            }
        }
    }
}
