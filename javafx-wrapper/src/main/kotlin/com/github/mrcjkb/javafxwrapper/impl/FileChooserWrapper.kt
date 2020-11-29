package com.github.mrcjkb.javafxwrapper.impl

import com.github.mrcjkb.javafxwrapper.api.IFileChooserWrapper
import com.github.mrcjkb.javafxwrapper.api.IJavaFxChooserWrapper
import com.github.mrcjkb.javafxwrapper.api.IJavaFxChooserWrapperInternal
import javafx.stage.FileChooser
import java.io.File

class FileChooserWrapper(private val javaFxSwingAdapter: IJavaFxChooserWrapperInternal, override val extensionFilters: MutableList<KExtensionFilter> = mutableListOf()) : IFileChooserWrapper, IJavaFxChooserWrapper by javaFxSwingAdapter {

    private val fileChooser: FileChooser = FileChooser()
    override var selectedExtensionFilter: KExtensionFilter? = null
    override var initialFileName: String? = null

    override fun showSaveDialog(): File? {
        return showDialog { fileChooser.showSaveDialog(javaFxSwingAdapter.javaFxParentWindow) }
    }

    override fun showOpenDialog(): File? {
        return showDialog { fileChooser.showOpenDialog(javaFxSwingAdapter.javaFxParentWindow) }
    }

    override fun showOpenMultipleDialog(): MutableList<File>? {
        return showDialog { fileChooser.showOpenMultipleDialog(javaFxSwingAdapter.javaFxParentWindow) }
    }

    private fun <T> showDialog(callback: () -> T?): T? {
        initialDirectory?.takeIf { it.isDirectory }.let { fileChooser.initialDirectory = it }
        title.let { fileChooser.title = it }
        initialFileName.let { fileChooser.initialFileName = initialFileName }
        translateExtensionFilters()
        val retVal = runPlatformTaskAndBlockEdt(callback, javaFxSwingAdapter.swingParentWindow)
        translateSelectedExtensionFilter()
        return retVal
    }

    private fun translateExtensionFilters() {
        fileChooser.extensionFilters.clear()
        extensionFilters
                .map { FileChooser.ExtensionFilter(it.description, it.extensions) }
                .forEach { fileChooser.extensionFilters.add(it) }
    }

    private fun translateSelectedExtensionFilter() {
        fileChooser.selectedExtensionFilter?.let {
            selectedExtensionFilter = KExtensionFilter(
                    fileChooser.selectedExtensionFilter.description,
                    fileChooser.selectedExtensionFilter.extensions
            )
        }
    }
}