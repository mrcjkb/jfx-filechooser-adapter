# jfx-filechooser-adapter
An adapter for a JavaFX File- and DirectoryChooser that allows for use in Swing

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
This project is split into three main sub-projects

- jfx-filechooser-adapter-parent: Contains the module context, which provides the entry to the API
- jfx-filechooser-adapter-api: Contains the Java interfaces
- jfx-filechooser-adapter-impl: Contains the implementation

It is intended that a client accesses only the jfx-filechooser-adapter-parent project directly, which hides the implementations.
Architecture rules are enforced using [ArchUnit](https://www.archunit.org/).

## Limitations
Due to a [bug](https://github.com/openjfx/javafx-gradle-plugin/issues/94) in Gradle JavaFX plugin, no arch unit tests can be added to the jfx-filechooser-adapter-impl project at te moment.

