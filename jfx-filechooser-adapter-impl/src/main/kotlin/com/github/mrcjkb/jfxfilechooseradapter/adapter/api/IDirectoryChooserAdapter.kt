package com.github.mrcjkb.jfxfilechooseradapter.adapter.api

import java.io.File

interface IDirectoryChooserAdapter: IJavaFxChooserAdapter {

    fun showDialog(): File?

}