plugins {
    kotlin("multiplatform") version "2.0.20"
    id("maven-publish")
    id("io.github.gciatto.kt-npm-publish") version "0.3.2"
}

group = "com.brainera"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm()

    js(IR) {
        nodejs {}
        binaries.library()
        generateTypeScriptDefinitions()
    }

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.js.ExperimentalJsExport")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation("org.kotlincrypto.hash:md:0.5.3")
                implementation("com.ionspin.kotlin:bignum:0.3.10")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting {}
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }

        val jsMain by getting {
            dependencies {}
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}

npmPublishing {
    token.set(System.getenv("NPM_AUTH_TOKEN"))

    liftPackageJson {
        license = "Apache-2.0"
        name = "@cortexapps/pseudonymizer"
    }
}
