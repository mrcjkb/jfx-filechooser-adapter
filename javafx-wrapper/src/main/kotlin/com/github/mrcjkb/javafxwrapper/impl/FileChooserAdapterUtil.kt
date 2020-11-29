package com.github.mrcjkb.javafxwrapper.impl

import javafx.application.Platform
import java.awt.Dialog
import java.awt.GraphicsDevice
import java.awt.Window
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutionException
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit
import javax.swing.JDialog
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

fun <T> runPlatformTaskAndBlockEdt(callback: () -> T?, parentWindow: Window?): T? {
    val invisibleModalSwingDialog = buildInvisibleModalSwingDialog(parentWindow)
    val dialogLatch = CountDownLatch(1)
    val futureTaskLatch = CountDownLatch(1)
    val futureTask = FutureTask {
        synchronized(futureTaskLatch) {
            futureTaskLatch.countDown()
        }
        try {
            return@FutureTask callback.invoke()
        } finally {
            try {
                dialogLatch.await()
            } catch (ignored: InterruptedException) {
            }
            SwingUtilities.invokeLater { invisibleModalSwingDialog.isVisible = false }
        }
    }
    Platform.runLater(futureTask)
    try {
        if (!futureTaskLatch.await(5, TimeUnit.SECONDS)) {
            synchronized(futureTaskLatch) {
                if (!futureTaskLatch.await(0, TimeUnit.SECONDS)) {
                    throw IllegalStateException("JavaFX is not responding.")
                }
            }
        }
    } catch (ignored: InterruptedException) {
    }
    SwingUtilities.invokeLater { dialogLatch.countDown() }
    invisibleModalSwingDialog.isVisible = true
    invisibleModalSwingDialog.dispose()
    try {
        return futureTask.get()
    } catch (ignored: InterruptedException) {
    } catch (ignored: ExecutionException) {
    }
    return null
}

fun buildInvisibleModalSwingDialog(parentWindow: Window?): Dialog {
    val swingModalDialog = JDialog(parentWindow)
    swingModalDialog.isModal = true
    swingModalDialog.isUndecorated = true
    if (swingModalDialog.graphicsConfiguration.device.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT)) {
        swingModalDialog.opacity = 0.0f
    }
    swingModalDialog.defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE
    return swingModalDialog
}
