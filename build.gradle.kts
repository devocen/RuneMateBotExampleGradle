plugins {
    java
    idea
    `java-library`
    id("io.freefair.lombok") version "6.3.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.openjfx.javafxplugin") version "0.0.10"
}

group = "com.YOURNAME"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://gitlab.com/api/v4/projects/10471880/packages/maven")
    maven("https://gitlab.com/api/v4/projects/32972353/packages/maven")
    mavenCentral()
}

val runemate: Configuration by configurations.creating {
    configurations["compileOnly"].extendsFrom(this)
    configurations["testCompileOnly"].extendsFrom(this)
}

dependencies {
    runemate("com.runemate:runemate-client:3.14.6.0")
    runemate("com.runemate:runemate-game-api:1.18.2")
    runemate("org.projectlombok:lombok:1.18.28")
}

val requiredOpens = arrayOf(
    "java.base/java.lang.reflect",
    "java.base/java.nio",
    "java.base/sun.nio.ch",
    "java.base/java.util.regex"
)

val javafxVersion = "20"
val javafxModules = arrayOf(
    "javafx.base", "javafx.fxml", "javafx.controls", "javafx.media", "javafx.web", "javafx.graphics", "javafx.swing"
)

javafx {
    version = javafxVersion
    configuration = "runemate"
    modules(*javafxModules)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

val bootClass by extra("com.runemate.client.boot.Boot")

val launch = task("launch", JavaExec::class) {
    group = "runemate"
    classpath = files(runemate, tasks.shadowJar)
    jvmArgs = mutableListOf("-Xmx4G")
    args = mutableListOf("--dev")
    mainClass.set(bootClass)
    requiredOpens.forEach { jvmArgs("--add-opens=${it}=ALL-UNNAMED") }
}
