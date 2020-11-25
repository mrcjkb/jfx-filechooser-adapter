package com.github.mrcjkb.jfxfilechooseradapter.adapter.api

import java.io.File
import java.util.function.Consumer
import javax.swing.JComponent

interface IJavaFxChooserAdapter {

    var initialDirectory: File?
    var title: String?

    fun addToSwingParent(addToSwingParentCallback: Consumer<JComponent>?)
}