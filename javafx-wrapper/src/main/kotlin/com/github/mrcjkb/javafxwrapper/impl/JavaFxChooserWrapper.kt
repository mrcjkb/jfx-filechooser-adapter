package com.github.mrcjkb.javafxwrapper.impl

import com.github.mrcjkb.javafxwrapper.api.IJavaFxChooserWrapper
import com.github.mrcjkb.javafxwrapper.api.IJavaFxChooserWrapperInternal
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.Group
import javafx.scene.Scene
import java.awt.Window
import java.io.File
import java.util.function.Consumer
import javax.swing.JComponent
import javax.swing.SwingUtilities

class JavaFxChooserWrapper: IJavaFxChooserWrapperInternal, IJavaFxChooserWrapper {

    private val jfxPanel: JFXPanel = JFXPanel()
    override var swingParentWindow: Window? = null
    override var javaFxParentWindow: javafx.stage.Window?
        get() = jfxPanel.scene?.window
        set(value) {}
    override var initialDirectory: File? = null
    override var title: String? = null

    init {
        Platform.setImplicitExit(false)
    }

    override fun addToSwingParent(addToSwingParentCallback: Consumer<JComponent>?) {
        jfxPanel.addHierarchyListener {
            swingParentWindow = SwingUtilities.getWindowAncestor(jfxPanel)
            swingParentWindow?.let {
                Platform.runLater { jfxPanel.scene = Scene(Group()) }
            }
        }
        addToSwingParentCallback?.accept(jfxPanel)
    }
}