package com.github.mrcjkb.jfxfilechooseradapter.api;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public interface IFileChooserBuilder {

    interface Compound extends IFileChooserBuilder, Initialised, WithInitialDirectory, WithTitle, WithFile, WithFileList, WithDirectory {}

    /**
     * Pass a callback function that adds a {@link JComponent} to a Swing {@link Container} so that its parent window can be identified for the file chooser.
     * @param addToSwingParentCallback a callback that adds the provided {@link JComponent} to a child of the file chooser's parent window. The provided {@link JComponent} does not have any children and will not be shown.
     * @return this builder, with the parent window detected, provided that the callback is implemented as specified.
     */
    IFileChooserBuilder addToSwingParent(Consumer<JComponent> addToSwingParentCallback);

    /**
     * Initialises this builder for creating a file chooser.
     * @return this builder, in an initialised state
     */
    Initialised init();

    /**
     * Initialises this builder for creating a file chooser.
     * @param identifier an identifier, used for persisting the last selected directory
     * @return this builder, in an initialised state
     */
    Initialised init(String identifier);

    interface Initialised {
        /**
         * [Optional] Sets the file chooser's initial directory.
         * If this method is used, the initial directory is set to the last chosen directory, if applicable.
         * @param initialDirectory the initial directory to set
         * @return this builder, with the initial directory set
         */
        WithInitialDirectory withInitialDirectory(File initialDirectory);

        /**
         * Sets the title of the file chooser dialog.
         * @param title the title to set
         * @return this builder, with the title set
         */
        WithTitle withTitle(String title);
    }

    interface WithInitialDirectory {
        /**
         * Sets the title of the file chooser dialog.
         * @param title the title to set
         * @return this builder, with the title set
         */
        WithTitle withTitle(String title);
    }

    interface WithTitle {
        /**
         * Adds an extension filter to the file chooser.
         * @param extensionFilterDescription the description of the extension filter
         * @param extensions a {@link List} of extensions
         * @return this builder, with the extension filter
         */
        WithTitle addExtensionFilter(String extensionFilterDescription, List<String> extensions);

        /**
         * Shows a "save file" dialog, and blocks until the user has selected a file or cancelled.
         * @return this builder, with the selected file, or {@code null} if no file was selected or the user cancelled
         */
        WithFile showSaveDialog();

        /**
         * Shows an "open file" dialog, and blocks until the user has selected a file or cancelled.
         * @return this builder, with the selected file, or {@code null} if no file was selected or the user cancelled
         */
        WithFile showOpenFileDialog();
        /**
         * Shows an "open multiple files" dialog, and blocks until the user has selected one or more files or has cancelled.
         * @return this builder, with the selected files, or {@code null} if no file was selected or the user cancelled
         */
        WithFileList showOpenMultipleFilesDialog();

        /**
         * Shows an "open directory" dialog, and blocks until the user has selected a directory or cancelled.
         * @return this builder, with the selected directory, or {@code null} if no directory was selected or the user cancelled
         */
        WithDirectory showOpenDirectoryDialog();
    }

    interface WithFile {
        /**
         * @return the lower case file extension of the selected file (according to the selected extension filter)
         */
        String getLowerCaseFileExtension();

        /**
         * @return the selected file
         */
        File getFile();
    }

    interface WithFileList {
        /**
         * @return the lower case file extensions of the selected files
         */
        List<String> getLowerCaseFileExtensions();

        /**
         * @return the selected files
         */
        List<File> getFileList();
    }

    interface WithDirectory {
        /**
         * @return the selected directory
         */
        Path getDirectory();
    }

}
