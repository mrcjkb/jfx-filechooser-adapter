package com.github.mrcjkb.javafxwrapper.api

import com.github.mrcjkb.javafxwrapper.impl.KExtensionFilter
import java.io.File

interface IFileChooserWrapper: IJavaFxChooserWrapper {

    val extensionFilters: MutableList<KExtensionFilter>
    var selectedExtensionFilter: KExtensionFilter?
    var initialFileName: String?

    fun showSaveDialog(): File?
    fun showOpenDialog(): File?
    fun showOpenMultipleDialog(): MutableList<File>?

}