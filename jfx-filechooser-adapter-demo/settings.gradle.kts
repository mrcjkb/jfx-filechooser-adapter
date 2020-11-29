rootProject.name = "jfx-filechooser-adapter-demo"

include(":jfx-filechooser-adapter", ":jfx-filechooser-adapter-api", ":jfx-filechooser-adapter-impl", ":javafx-wrapper")
project(":jfx-filechooser-adapter").projectDir = File(settingsDir, "..")
project(":jfx-filechooser-adapter-api").projectDir = File(settingsDir, "../jfx-filechooser-adapter-api")
project(":jfx-filechooser-adapter-impl").projectDir = File(settingsDir, "../jfx-filechooser-adapter-impl")
project(":javafx-wrapper").projectDir = File(settingsDir, "../javafx-wrapper")