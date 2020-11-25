package com.github.mrcjkb.jfxfilechooseradapter.impl

import com.github.mrcjkb.jfxfilechooseradapter.adapter.api.IDirectoryChooserAdapter
import com.github.mrcjkb.jfxfilechooseradapter.adapter.api.IFileChooserAdapter
import com.github.mrcjkb.jfxfilechooseradapter.adapter.api.IJavaFxChooserAdapter
import com.github.mrcjkb.jfxfilechooseradapter.api.IFileChooserBuilder
import com.github.mrcjkb.jfxfilechooseradapter.api.IFileChooserBuilder.*
import java.io.File
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.util.function.Consumer
import java.util.prefs.Preferences
import javax.swing.JComponent

class FileChooserBuilder: IFileChooserBuilder, Initialised, WithInitialFileName, WithInitialDirectory, WithInitialDirectoryAndInitialFileName, FileChooser, FileAndDirectoryChooser, WithFile, WithFileList, WithDirectory {

    private val javaFxChooserAdapter: IJavaFxChooserAdapter
    private val fileChooser: IFileChooserAdapter
    private val directoryChooser: IDirectoryChooserAdapter
    private lateinit var preferences: Preferences
    private val lastChosenDirectoryKey = "lastChosenDirectory"
    private var title: String? = null
    private var selectedFile: File? = null
    private var selectedFiles: List<File>? = null

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
        javaFxChooserAdapter.title = null
        javaFxChooserAdapter.initialDirectory = null
        fileChooser.initialFileName = null
        return this
    }

    override fun withInitialDirectory(initialDirectory: File?): FileChooserBuilder {
        javaFxChooserAdapter.initialDirectory = initialDirectory
        return this
    }

    override fun withInitialFileName(initialFileName: String?): FileChooserBuilder {
        fileChooser.initialFileName = initialFileName
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
        persistLastChosenDirectory(selectedFile?.parentFile)
        return this
    }

    override fun showOpenFileDialog(): FileChooserBuilder {
        selectedFile = showDialog { fileChooser.showOpenDialog() }
        persistLastChosenDirectory(selectedFile?.parentFile)
        return this
    }

    override fun showOpenMultipleFilesDialog(): FileChooserBuilder {
        selectedFiles = showDialog { fileChooser.showOpenMultipleDialog() }
        persistLastChosenDirectory(selectedFiles?.takeIf { it.isNotEmpty() }?.get(0)?.parentFile)
        return this
    }

    override fun showOpenDirectoryDialog(): FileChooserBuilder {
        selectedFile = showDialog { directoryChooser.showDialog() }
        persistLastChosenDirectory(selectedFile)
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
        javaFxChooserAdapter.initialDirectory = javaFxChooserAdapter.initialDirectory ?: preferences[lastChosenDirectoryKey, null] ?. let { File(it) }
        javaFxChooserAdapter.title = title
        return callback.invoke()
    }

    private fun persistLastChosenDirectory(lastChosenDirectory: File?) {
        lastChosenDirectory
                ?.takeIf { it.isDirectory }
                ?.let { preferences.put(lastChosenDirectoryKey, it.absolutePath) }
    }
}