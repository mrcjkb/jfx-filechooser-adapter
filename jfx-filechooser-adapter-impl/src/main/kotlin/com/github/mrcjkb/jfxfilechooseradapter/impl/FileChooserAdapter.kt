package com.github.mrcjkb.jfxfilechooseradapter.impl

import com.github.mrcjkb.jfxfilechooseradapter.adapter.api.IFileChooserAdapter
import com.github.mrcjkb.jfxfilechooseradapter.adapter.api.IJavaFxChooserAdapter
import com.github.mrcjkb.jfxfilechooseradapter.adapter.api.IJavaFxChooserAdapterInternal
import javafx.stage.FileChooser
import java.io.File

class FileChooserAdapter(private val javaFxSwingAdapter: IJavaFxChooserAdapterInternal, override val extensionFilters: MutableList<KExtensionFilter> = mutableListOf()) : IFileChooserAdapter, IJavaFxChooserAdapter by javaFxSwingAdapter {

    private val fileChooser: FileChooser = FileChooser()
    override var selectedExtensionFilter: KExtensionFilter? = null

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