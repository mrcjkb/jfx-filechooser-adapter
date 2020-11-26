package com.github.mrcjkb.javafxadapter.api

import com.github.mrcjkb.javafxadapter.impl.KExtensionFilter
import java.io.File

interface IFileChooserAdapter: IJavaFxChooserAdapter {

    val extensionFilters: MutableList<KExtensionFilter>
    var selectedExtensionFilter: KExtensionFilter?
    var initialFileName: String?

    fun showSaveDialog(): File?
    fun showOpenDialog(): File?
    fun showOpenMultipleDialog(): MutableList<File>?

}