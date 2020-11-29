plugins {
  java
  idea
  id ("java-library")
  `maven-publish`
}

group = "com.github.mrcjkb"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation("com.tngtech.archunit:archunit-junit5:0.14.1")
}

java {
  modularity.inferModulePath.set(true)
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
  }
  withJavadocJar()
  withSourcesJar()
}

tasks.test {
  useJUnitPlatform()
}