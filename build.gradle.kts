import java.text.SimpleDateFormat
import java.util.*

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
  description = "JavaFX FileChooser and DirectoryChooser adapter that can be used in a Swing application."
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
  withJavadocJar()
  withSourcesJar()
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
}

signing {
  sign(configurations.archives.get())
}

val allProjects = listOf(rootProject) + subprojects

publishing {
  publications {
    allProjects.forEach {
      create<MavenPublication>(it.name) {
        groupId = group.toString()
        artifactId = rootProject.name
        version = version
        from(components["java"])
        versionMapping {
          usage("java-api") {
            fromResolutionOf("runtimeClasspath")
          }
          usage("java-runtime") {
            fromResolutionResult()
          }
        }
        pom {
          name.set(rootProject.name)
          description.set(rootProject.description)
          url.set("https://github.com/MrcJkb/jfx-filechooser-adapter/")
          developers() {
            developer {
              id.set("MrcJkb")
              name.set("Marc Jakobi")
            }
          }
          issueManagement {
            system.set("GitHub")
            url.set("https://github.com/MrcJkb/jfx-filechooser-adapter/issues")
          }
          scm {
            url.set("https://github.com/MrcJkb/jfx-filechooser-adapter/")
            connection.set("scm:git:git@github.com:MrcJkb/jfx-filechooser-adapter.git")
            developerConnection.set("scm:git:ssh://git@github.com:MrcJkb/jfx-filechooser-adapter.git")
          }
          licenses {
            license {
              name.set("GPLv2 with Classpath Exception")
              url.set("https://github.com/MrcJkb/jfx-filechooser-adapter/blob/main/LICENSE")
              distribution.set("repo")
            }
          }
        }
      }
    }
  }
  repositories {
    maven {
      val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
      val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
      url = if (version.toString().contains("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
      credentials {
        username = project.properties["ossrhUser"].toString()
        password = project.properties["ossrhPassword"].toString()
      }
    }
  }
}

signing {
  allProjects.forEach {
    publishing.publications[it.name]
  }
}

tasks.javadoc {
  if (JavaVersion.current().isJava9Compatible) {
    (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
  }
}
