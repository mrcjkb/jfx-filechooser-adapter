plugins {
  java
  idea
  id ("java-library")
}

group = "com.github.mrcjkb"

repositories {
  mavenCentral()
}

dependencies {
  testImplementation("com.tngtech.archunit:archunit-junit5:0.14.1")
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
  modularity.inferModulePath.set(true)
  toolchain {
    languageVersion = JavaLanguageVersion.of(11)
  }
}

tasks.test {
  useJUnitPlatform()
}