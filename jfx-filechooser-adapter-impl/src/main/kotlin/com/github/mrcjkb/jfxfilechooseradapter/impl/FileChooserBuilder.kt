package com.github.mrcjkb.jfxfilechooseradapter.impl

import com.github.mrcjkb.jfxfilechooseradapter.adapter.api.IDirectoryChooserAdapter
import com.github.mrcjkb.jfxfilechooseradapter.adapter.api.IFileChooserAdapter
import com.github.mrcjkb.jfxfilechooseradapter.api.IFileChooserBuilder
import com.github.mrcjkb.jfxfilechooseradapter.adapter.api.IJavaFxChooserAdapter
import javafx.application.Platform
import java.io.File
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.util.function.Consumer
import java.util.prefs.Preferences
import javax.swing.JComponent

class FileChooserBuilder: IFileChooserBuilder.Compound {

    private val javaFxChooserAdapter: IJavaFxChooserAdapter
    private val fileChooser: IFileChooserAdapter
    private val directoryChooser: IDirectoryChooserAdapter
    private lateinit var preferences: Preferences
    private var title: String? = null
    private var selectedFile: File? = null
    private var selectedFiles: List<File>? = null
    private var initialDirectory: File? = null

    init {
        javaFxChooserAdapter = JavaFxChooserAdapter()
        fileChooser = FileChooserAdapter(javaFxChooserAdapter)
        directoryChooser = DirectoryChooserAdapter(javaFxChooserAdapter)
    }

    override fun addToSwingParent(addToSwingParentCallback: Consumer<JComponent>?): IFileChooserBuilder {
        javaFxChooserAdapter.addToSwingParent(addToSwingParentCallback)
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
        fileChooser.extensionFilters.add(KExtensionFilter(extensionFilterDescription, extensions))
        return this
    }

    override fun showSaveDialog(): FileChooserBuilder {
        selectedFile = showDialog { fileChooser.showSaveDialog() }
        return this
    }

    override fun showOpenFileDialog(): FileChooserBuilder {
        selectedFile = showDialog { fileChooser.showOpenDialog() }
        return this
    }

    override fun showOpenMultipleFilesDialog(): FileChooserBuilder {
        selectedFiles = showDialog { fileChooser.showOpenMultipleDialog() }
        return this
    }

    override fun showOpenDirectoryDialog(): IFileChooserBuilder.WithDirectory {
        selectedFile = showDialog { directoryChooser.showDialog() }
        return this
    }

    override fun getLowerCaseFileExtension(): String {
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
        javaFxChooserAdapter.initialDirectory = initialDirectory ?: preferences["lastChosenDirectory", null] ?. let { File(it) }
        javaFxChooserAdapter.title = title
        return callback.invoke()
    }


}