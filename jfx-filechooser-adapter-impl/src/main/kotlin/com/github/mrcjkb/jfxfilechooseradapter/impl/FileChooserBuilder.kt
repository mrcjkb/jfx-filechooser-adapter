package com.github.mrcjkb.jfxfilechooseradapter.impl

import com.github.mrcjkb.jfxfilechooseradapter.api.IFileChooserBuilder
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.Group
import javafx.scene.Scene
import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import java.awt.Window
import java.io.File
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.util.function.Consumer
import java.util.prefs.Preferences
import javax.swing.JComponent
import javax.swing.SwingUtilities

class FileChooserBuilder: IFileChooserBuilder.Compound {

    private val fileChooser: FileChooser = FileChooser()
    private val directoryChooser: DirectoryChooser = DirectoryChooser()
    private val jfxPanel: JFXPanel = JFXPanel()
    private lateinit var preferences: Preferences
    private var title: String? = null
    private var selectedFile: File? = null
    private var selectedFiles: List<File>? = null
    private var initialDirectory: File? = null
    private var swingParentWindow: Window? = null

    init {
        Platform.setImplicitExit(false)
    }

    override fun addToSwingParent(addToSwingParentCallback: Consumer<JComponent>?): IFileChooserBuilder {
        jfxPanel.addHierarchyListener {
            swingParentWindow = SwingUtilities.getWindowAncestor(jfxPanel)
            swingParentWindow?.let {
                Platform.runLater { jfxPanel.scene = Scene(Group()) }
            }
        }
        addToSwingParentCallback?.accept(jfxPanel)
        return this
    }

    override fun init(): FileChooserBuilder {
        return init(javaClass.name)
    }

    override fun init(identifier: String?): FileChooserBuilder {
        preferences = Preferences.userRoot().node(identifier)
        fileChooser.extensionFilters.clear()
        return this
    }

    override fun withInitialDirectory(initialDirectory: File?): FileChooserBuilder {
        this.initialDirectory = initialDirectory
        return this
    }

    override fun withTitle(title: String?): FileChooserBuilder {
        this.title = title
        return this
    }

    override fun addExtensionFilter(extensionFilterDescription: String?, extensions: List<String>?): FileChooserBuilder {
        fileChooser.extensionFilters.add(FileChooser.ExtensionFilter(extensionFilterDescription, extensions))
        return this
    }

    override fun showSaveDialog(): FileChooserBuilder {
        selectedFile = showDialog { fileChooser.showSaveDialog(getJfxParentWindow()) }
        return this
    }

    override fun showOpenFileDialog(): FileChooserBuilder {
        selectedFile = showDialog { fileChooser.showOpenDialog(getJfxParentWindow()) }
        return this
    }

    override fun showOpenMultipleFilesDialog(): FileChooserBuilder {
        selectedFiles = showDialog { fileChooser.showOpenMultipleDialog(getJfxParentWindow()) }
        return this
    }

    override fun showOpenDirectoryDialog(): IFileChooserBuilder.WithDirectory {
        selectedFile = showDialog { directoryChooser.showDialog(getJfxParentWindow()) }
        return this
    }

    override fun getLowerCaseFileExtension(): String? {
        fileChooser.selectedExtensionFilter?.extensions
                ?.takeIf { it.isNotEmpty() }
                ?.let {
                    val selectedExtensionFilterExtension = it[0]
                    return (if (selectedExtensionFilterExtension.contains(".")) getLowerCaseExtension(selectedExtensionFilterExtension) else selectedExtensionFilterExtension)
                            .replace("*", "")
                }?:run {
                    return getLowerCaseExtension(selectedFile?.name)
                }
    }

    override fun getFile(): File? {
        return selectedFile
    }

    override fun getLowerCaseFileExtensions(): List<String>? {
        return fileList
                ?.map { it.name }
                ?.map { getLowerCaseExtension(it) }
    }

    override fun getFileList(): List<File>? {
        return selectedFiles
    }

    override fun getDirectory(): Path? {
        return try {
            selectedFile?.toPath()
        } catch (ignored: InvalidPathException) {
            null
        }
    }

    private fun <T> showDialog(callback: () -> T?): T? {
        getInitialDirectory()?.let {
            fileChooser.initialDirectory = it
            directoryChooser.initialDirectory = it
        }
        fileChooser.title = title
        directoryChooser.title = title
        return runPlatformTaskAndBlockEdt(callback)
    }

    private fun getJfxParentWindow(): javafx.stage.Window? {
        return jfxPanel.scene?.window
    }

    private fun getInitialDirectory(): File? {
        return initialDirectory?:preferences["lastChosenDirectory", null]?.let { File(it) }
    }

}