import java.text.SimpleDateFormat
import java.util.*

plugins {
  java
  idea
  id ("java-library")
  `maven-publish`
  signing
}

group = "com.github.mrcjkb"


repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":jfx-filechooser-adapter"))
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
      from(components["java"])
      artifact(sourcesJar.get())
      artifact(javadocJar.get())
    }
  }
}
