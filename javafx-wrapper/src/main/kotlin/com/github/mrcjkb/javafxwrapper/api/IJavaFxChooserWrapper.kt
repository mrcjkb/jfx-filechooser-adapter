package com.github.mrcjkb.javafxwrapper.api

import java.io.File
import java.util.function.Consumer
import javax.swing.JComponent

interface IJavaFxChooserWrapper {

    var initialDirectory: File?
    var title: String?

    fun addToSwingParent(addToSwingParentCallback: Consumer<JComponent>?)
}