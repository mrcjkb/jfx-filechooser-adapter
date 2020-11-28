plugins {
  java
  idea
  id("org.openjfx.javafxplugin") version "0.0.9"
  kotlin("jvm") version "1.4.10"
  id("org.jetbrains.kotlin.plugin.jpa") version "1.4.10"
  id("org.jetbrains.kotlin.plugin.noarg") version "1.4.10"
}

group = "com.github.mrcjkb"

repositories {
  mavenCentral()
  jcenter()
}

// TODO re-enable when javafx plugin bug that forces everything on the module path is fixed
//tasks.compileJava {
//  options.compilerArgs = listOf("--patch-module", "mrcjkb.javafxwrapper=${sourceSets.main.get().output.asPath}")
//}

dependencies {
  implementation(kotlin("stdlib"))
  implementation("org.openjfx:javafx-swing:11.0.2")
  implementation("org.openjfx:javafx-graphics:11.0.2:win")
  implementation("org.openjfx:javafx-graphics:11.0.2:linux")
  implementation("org.openjfx:javafx-graphics:11.0.2:mac")
}

java {
  modularity.inferModulePath.set(true)
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
  }
}

javafx {
  version = "11"
  modules = listOf("javafx.base", "javafx.graphics", "javafx.swing")
}

tasks.test {
  useJUnitPlatform()
}