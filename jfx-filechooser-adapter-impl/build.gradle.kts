group = "com.github.mrcjkb"

plugins {
  java
  idea
  kotlin("jvm") version "1.4.10"
  id("org.jetbrains.kotlin.plugin.jpa") version "1.4.10"
  id("org.jetbrains.kotlin.plugin.noarg") version "1.4.10"
  `maven-publish`
}

tasks.compileJava {
  // Workaround for adding the src/kotlin classes to the java modulepath
  options.compilerArgs = listOf("--patch-module", "mrcjkb.jfxfilechooseradapter.impl=${sourceSets.main.get().output.asPath}")
}

repositories {
  mavenCentral()
  jcenter()
}

dependencies {
  implementation(project(":jfx-filechooser-adapter-api"))
  implementation(project(":javafx-wrapper"))
  implementation(kotlin("stdlib"))
  testImplementation("com.tngtech.archunit:archunit-junit5:0.14.1")
  testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
  testImplementation("com.tngtech.archunit:archunit-junit5:0.14.1")
  testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
}

java {
  modularity.inferModulePath.set(true)
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
  }
  withSourcesJar()
}

tasks.test {
  useJUnitPlatform()
}