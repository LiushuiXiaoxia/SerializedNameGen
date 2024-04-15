plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
    id("org.jetbrains.intellij") version "1.17.2"
}

group = "cn.mycommons"
version = "1.0"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    pluginName.set("JSON Annotation Tool")
    version.set("2023.2.5")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf("com.intellij.java", "org.jetbrains.kotlin"))
    // plugins.set(listOf("java", "kotlin"))
    // sandboxDirectory.set("${project.rootDir}/sandbox")
}

//intellij {
//    pluginName 'JSON Annotation Tool'
//    version '2019.3'
//    plugins = ['java', 'Kotlin']
//    updateSinceUntilBuild false
//    sandboxDirectory = "${project.rootDir}/sandbox"
//}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    patchPluginXml {
//        changeNotes """Auto add or remove json annotation plugin, such as gson SerializedName,
// fastjson JSONField, jackson JsonProperty. It also support java and kotlin file."""
        sinceBuild.set("232")
        untilBuild.set("242.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
