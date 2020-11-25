package com.github.mrcjkb.jfxfilechooseradapter.adapter.api

import com.github.mrcjkb.jfxfilechooseradapter.impl.KExtensionFilter
import java.io.File

interface IFileChooserAdapter: IJavaFxChooserAdapter {

    val extensionFilters: MutableList<KExtensionFilter>
    var selectedExtensionFilter: KExtensionFilter?

    fun showSaveDialog(): File?
    fun showOpenDialog(): File?
    fun showOpenMultipleDialog(): MutableList<File>?

}