package com.github.mrcjkb.javafxwrapper.impl

import com.github.mrcjkb.javafxwrapper.api.IJavaFxChooserWrapper
import com.github.mrcjkb.javafxwrapper.api.IJavaFxChooserWrapperInternal
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.embed.swing.SwingFXUtils
import javafx.scene.Group
import javafx.scene.Scene
import javafx.stage.Stage
import java.awt.Window
import java.awt.image.BufferedImage
import java.io.File
import java.util.function.Consumer
import javax.swing.JComponent
import javax.swing.SwingUtilities

class JavaFxChooserWrapper: IJavaFxChooserWrapperInternal, IJavaFxChooserWrapper {

    private val jfxPanel: JFXPanel = JFXPanel()
    override var swingParentWindow: Window? = null
    override var javaFxParentWindow: javafx.stage.Window?
        get() {
            swingParentWindow?.iconImages
                    ?.filterIsInstance<BufferedImage>()
                    ?.map { return@map SwingFXUtils.toFXImage(it, null) }
                    ?.forEach { (jfxPanel.scene?.window as Stage).icons?.add(it) }
            return jfxPanel.scene?.window
            }
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
                        Platform.runLater {
                            jfxPanel.scene = Scene(Group())
                        }
                    }
                }
                addToSwingParentCallback?.accept(jfxPanel)
            }
}