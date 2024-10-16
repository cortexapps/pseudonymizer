plugins {
    kotlin("multiplatform") version "2.0.20"
    id("maven-publish")
    id("dev.petuska.npm.publish") version "3.4.3"
}

group = "com.brainera"
version = "0.0.1"

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

npmPublish {
    registries {
        register("github") {
            uri.set("https://npm.pkg.github.com")
            authToken.set(System.getenv("GITHUB_PASSWORD"))
        }
    }

    packages {
       named("js") {
           packageJson {
               license = "Apache-2.0"
               name = "@cortexapps/pseudonymizer"
           }
       }
    }
}

publishing {
    repositories {
        maven {
            name = "GithubPackages"
            url = uri("https://maven.pkg.github.com/cortexapps/pseudonymizer")
            credentials {
                username = project.findProperty("gpr.user") as? String ?: System.getenv("GITHUB_USERNAME")
                password = project.findProperty("gpr.key") as? String ?: System.getenv("GITHUB_PASSWORD")
            }
        }
    }
}
