package com.github.mrcjkb.jfxfilechooseradapter.impl

import com.github.mrcjkb.javafxwrapper.api.IDirectoryChooserWrapper
import com.github.mrcjkb.javafxwrapper.api.IFileChooserWrapper
import com.github.mrcjkb.javafxwrapper.api.IJavaFxChooserWrapper
import com.github.mrcjkb.javafxwrapper.impl.DirectoryChooserWrapper
import com.github.mrcjkb.javafxwrapper.impl.FileChooserWrapper
import com.github.mrcjkb.javafxwrapper.impl.JavaFxChooserWrapper
import com.github.mrcjkb.javafxwrapper.impl.KExtensionFilter
import com.github.mrcjkb.jfxfilechooseradapter.api.IFileChooserBuilder
import com.github.mrcjkb.jfxfilechooseradapter.api.IFileChooserBuilder.*
import com.github.mrcjkb.persistence.api.IDirectoryPersistence
import com.github.mrcjkb.persistence.impl.DirectoryPersistence
import java.io.File
import java.nio.file.InvalidPathException
import java.nio.file.Path
import java.util.function.Consumer
import javax.swing.JComponent

class FileChooserBuilder: IFileChooserBuilder, Initialised, WithInitialFileName, WithInitialDirectory, WithInitialDirectoryAndInitialFileName, FileChooser, FileAndDirectoryChooser, WithFile, WithFileList, WithDirectory {

    private val javaFxChooserWrapper: IJavaFxChooserWrapper
    private val fileChooser: IFileChooserWrapper
    private val directoryChooser: IDirectoryChooserWrapper
    private lateinit var directoryPersistence: IDirectoryPersistence
    private var title: String? = null
    private var selectedFile: File? = null
    private var selectedFiles: List<File>? = null

    init {
        javaFxChooserWrapper = JavaFxChooserWrapper()
        fileChooser = FileChooserWrapper(javaFxChooserWrapper)
        directoryChooser = DirectoryChooserWrapper(javaFxChooserWrapper)
    }

    override fun addToSwingParent(addToSwingParentCallback: Consumer<JComponent>?): IFileChooserBuilder {
        javaFxChooserWrapper.addToSwingParent(addToSwingParentCallback)
        return this
    }

    override fun init(): FileChooserBuilder {
        return init(javaClass.name)
    }

    override fun init(identifier: String?): FileChooserBuilder {
        directoryPersistence = DirectoryPersistence(identifier?:javaClass.name)
        fileChooser.extensionFilters.clear()
        javaFxChooserWrapper.title = null
        javaFxChooserWrapper.initialDirectory = null
        fileChooser.initialFileName = null
        return this
    }

    override fun withInitialDirectory(initialDirectory: File?): FileChooserBuilder {
        javaFxChooserWrapper.initialDirectory = initialDirectory
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
        directoryPersistence.lastChosenDirectory = selectedFile?.parentFile
        return this
    }

    override fun showOpenFileDialog(): FileChooserBuilder {
        selectedFile = showDialog { fileChooser.showOpenDialog() }
        directoryPersistence.lastChosenDirectory = selectedFile?.parentFile
        return this
    }

    override fun showOpenMultipleFilesDialog(): FileChooserBuilder {
        selectedFiles = showDialog { fileChooser.showOpenMultipleDialog() }
        directoryPersistence.lastChosenDirectory = selectedFiles?.takeIf { it.isNotEmpty() }?.get(0)?.parentFile
        return this
    }

    override fun showOpenDirectoryDialog(): FileChooserBuilder {
        selectedFile = showDialog { directoryChooser.showDialog() }
        directoryPersistence.lastChosenDirectory = selectedFile
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
        javaFxChooserWrapper.initialDirectory = javaFxChooserWrapper.initialDirectory ?: directoryPersistence.lastChosenDirectory
        javaFxChooserWrapper.title = title
        return callback.invoke()
    }

}