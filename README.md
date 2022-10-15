# jfx-filechooser-adapter

> [![Maintenance](https://img.shields.io/badge/Maintained%3F-no-red.svg)](https://bitbucket.org/lbesson/ansi-colors)
> 
> __NOTE:__
>
> Since I hardly work with Java anymore, I will no longer actively maintain this repository.
> I will gladly review any PRs.

An adapter for a JavaFX File- and DirectoryChooser that allows for use in Swing

[![Maven Central](https://img.shields.io/maven-central/v/com.github.mrcjkb/jfx-filechooser-adapter.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.mrcjkb%22%20AND%20a:%22jfx-filechooser-adapter%22)

# Requirements

- Java 11
- JavaFX 11.0.2 Jmods

# Usage

The class `JfxFileChooserAdapterModuleContext` provides the module context, from which the file chooser builder can be accessed:

```
JfxFileChooserAdapterModuleContext.getFileChooserBuilder()
```


To begin invocation, call the `init()` or the `init(String identifier)` method. The optional identifier parameter can be used to remember the last selected directory. Without a parameter, a default identifier is used.
From there, the fluent API has been designed to guide you through the valid options. Depending on previous choices, invalid options are hidden from the interface.

See the demo project for usage examples.


## Setting a Swing parent window for the JavaFX FileChooser or DirectoryChooser

To set a Swing parent window, the `IFileChooserBuilder` interface provides the optional method `addToSwingParent(Consumer<JComponent> addToSwingParentCallback)`. If this method is called, and the `addToSwingParentCallback` adds the provided (invisible) `JComponent` to a Swing `Container`, the `Container`'s parent window is detected. Upon invocation, the file chooser builder attempts to position the file chooser on top of the detected Swing window. Unfortunately, this does not always work (e.g. on Windows 10, in a multi monitor setup).


# Contribution

## Architecture
This project is split into a parent project with three subprojects:

- jfx-filechooser-adapter-parent: Contains the module context, which provides the entry to the API
- jfx-filechooser-adapter-api: Contains the Java interfaces
- jfx-filechooser-adapter-impl: Contains the implementation
- javafx-wrapper: A wrapper for encapsulating JavaFX from the main projects

Additionally, the project jfx-filechooser-adapter-demo is included for demonstration purposes.

It is intended that a client accesses only the jfx-filechooser-adapter-parent project directly, which encapsulates the implementations.
Architecture rules are enforced using [ArchUnit](https://www.archunit.org/).

## Limitations
Due to a [bug](https://github.com/openjfx/javafx-gradle-plugin/issues/94) in Gradle JavaFX plugin, no tests can be added to the javafx-wrapper project.
