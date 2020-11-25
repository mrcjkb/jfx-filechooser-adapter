package com.github.mrcjkb.jfxfilechooseradapter.adapter.api

import javafx.stage.Window

interface IJavaFxChooserAdapterInternal: IJavaFxChooserAdapter {

    var swingParentWindow: java.awt.Window?
    var javaFxParentWindow: Window?

}