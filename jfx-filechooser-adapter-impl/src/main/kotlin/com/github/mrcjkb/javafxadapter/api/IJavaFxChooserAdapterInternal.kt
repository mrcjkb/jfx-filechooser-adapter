package com.github.mrcjkb.javafxadapter.api

import javafx.stage.Window

interface IJavaFxChooserAdapterInternal: IJavaFxChooserAdapter {

    var swingParentWindow: java.awt.Window?
    var javaFxParentWindow: Window?

}