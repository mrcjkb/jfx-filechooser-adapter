package com.github.mrcjkb.javafxwrapper.impl

import com.github.mrcjkb.javafxwrapper.api.IDirectoryChooserWrapper
import com.github.mrcjkb.javafxwrapper.api.IJavaFxChooserWrapper
import com.github.mrcjkb.javafxwrapper.api.IJavaFxChooserWrapperInternal
import javafx.stage.DirectoryChooser
import java.io.File

class DirectoryChooserWrapper(private val javaFxSwingAdapter: IJavaFxChooserWrapperInternal): IDirectoryChooserWrapper, IJavaFxChooserWrapper by javaFxSwingAdapter {

    private val directoryChooser: DirectoryChooser = DirectoryChooser()

    override fun showDialog(): File? {
        initialDirectory?.takeIf { it.isDirectory }?.let { directoryChooser.initialDirectory = it }
        title.let { directoryChooser.title = it }
        return runPlatformTaskAndBlockEdt({ directoryChooser.showDialog(javaFxSwingAdapter.javaFxParentWindow) }, javaFxSwingAdapter.swingParentWindow)
    }
}