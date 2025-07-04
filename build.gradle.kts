import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("org.jetbrains.intellij") version "1.17.2"
}

group = "cn.mycommons"
version = "1.2"

repositories {
    maven { setUrl("https://maven.aliyun.com/repository/public/") }
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
    sandboxDir.set("${project.rootDir}/sandbox")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

}
kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

tasks {
    val n = """Auto add or remove json annotation plugin, such as gson SerializedName,
 fastjson JSONField, jackson JsonProperty. It also support java and kotlin file."""

    patchPluginXml {
        changeNotes.set(n)
        sinceBuild.set("242")
        untilBuild.set("252.*")
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
