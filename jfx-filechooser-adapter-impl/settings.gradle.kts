rootProject.name = "jfx-filechooser-adapter-impl"

include(":jfx-filechooser-adapter-api", ":javafx-wrapper")
project(":jfx-filechooser-adapter-api").projectDir = File(settingsDir, "../jfx-filechooser-adapter-api")
project(":javafx-wrapper").projectDir = File(settingsDir, "../javafx-wrapper")