package com.github.mrcjkb.javafxwrapper.api

import java.io.File

interface IDirectoryChooserWrapper: IJavaFxChooserWrapper {

    fun showDialog(): File?

}