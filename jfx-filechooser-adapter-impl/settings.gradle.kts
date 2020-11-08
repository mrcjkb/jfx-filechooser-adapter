rootProject.name = "jfx-filechooser-adapter-impl"

include(":app", ":jfx-filechooser-adapter-api")
project(":jfx-filechooser-adapter-api").projectDir = File(settingsDir, "../jfx-filechooser-adapter-api")