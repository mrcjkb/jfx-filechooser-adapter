package com.github.mrcjkb.javafxadapter.api

import java.io.File

interface IDirectoryChooserAdapter: IJavaFxChooserAdapter {

    fun showDialog(): File?

}