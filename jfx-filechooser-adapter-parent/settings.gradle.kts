rootProject.name = "jfx-filechooser-adapter"

include(":app", ":jfx-filechooser-adapter-api")
project(":jfx-filechooser-adapter-api").projectDir = File(settingsDir, "../jfx-filechooser-adapter-api")

include(":app", ":jfx-filechooser-adapter-impl")
project(":jfx-filechooser-adapter-impl").projectDir = File(settingsDir, "../jfx-filechooser-adapter-impl")