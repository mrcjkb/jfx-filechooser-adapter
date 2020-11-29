import java.text.SimpleDateFormat
import java.util.*
import org.gradle.api.publish.maven.MavenPom

plugins {
  java
  idea
  id ("java-library")
  id("com.palantir.git-version") version "0.12.3"
  `maven-publish`
  signing
}

group = "com.github.mrcjkb"

val gitVersion: groovy.lang.Closure<String> by extra

allprojects {
  version = gitVersion().replace(".dirty", "").replace("-", ".")
}

repositories {
  mavenCentral()
}

dependencies {
  api(project(":jfx-filechooser-adapter-api"))
  implementation(project(":jfx-filechooser-adapter-impl"))
  testImplementation("com.tngtech.archunit:archunit-junit5:0.14.1")
}

java {
  modularity.inferModulePath.set(true)
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
  }
}

tasks.test {
  useJUnitPlatform()
}

tasks.jar {
  val javaVersion = System.getProperty("java.version")
  val javaVendor = System.getProperty("java.vendor")
  val javaVmVersion = System.getProperty("java.vm.version")
  val osName = System.getProperty("os.name")
  val osArchitecture = System.getProperty("os.arch")
  val osVersion = System.getProperty("os.version")
  manifest {
    attributes["Library"] = rootProject.name
    attributes["Version"] = archiveVersion
    attributes["Website"] = "https://github.com/MrcJkb/jfx-filechooser-adapter"
    attributes["Built-By"] = System.getProperty("user.name")
    attributes["Build-Timestamp"] = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(Date())
    attributes["Created-by"] = "Gradle ${gradle.gradleVersion}"
    attributes["Build-OS"] = "$osName $osArchitecture $osVersion"
    attributes["Build-Jdk"] = "$javaVersion ($javaVendor $javaVmVersion)"
    attributes["Build-OS"] = "$osName $osArchitecture $osVersion"
  }
  from(configurations.compile.get()
          .onEach { println("add from dependencies: ${it.name}") }
          .map { if (it.isDirectory) it else zipTree(it) })
}

val sourcesJar by tasks.registering(Jar::class) {
  archiveClassifier.set("sources")
  from(sourceSets.main.get().allSource)
}

val javadocJar by tasks.registering(Jar::class) {
  archiveClassifier.set("javadoc")
  from(tasks["javadoc"])
}

artifacts {
  add("archives", sourcesJar)
  add("archives", javadocJar)
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      groupId = group.toString()
      artifactId = rootProject.name
      version = version
      customizePom(pom)
      from(components["java"])
      artifact(sourcesJar.get())
      artifact(javadocJar.get())
    }
  }
}

fun customizePom(pom: MavenPom) {
  with (pom) {
    name.set(rootProject.name)
    description.set(rootProject.description)
    url.set("https://github.com/MrcJkb/jfx-filechooser-adapter/")
    issueManagement {
      system.set("GitHub")
      url.set("https://github.com/MrcJkb/jfx-filechooser-adapter/issues")
    }
    scm {
      url.set("https://github.com/MrcJkb/jfx-filechooser-adapter/")
      connection.set("scm:git:git@github.com:MrcJkb/jfx-filechooser-adapter.git")
      developerConnection.set("scm:git:ssh://git@github.com:MrcJkb/jfx-filechooser-adapter.git")
    }
  }
}
