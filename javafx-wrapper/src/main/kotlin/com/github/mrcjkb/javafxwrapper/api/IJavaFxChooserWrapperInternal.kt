package com.github.mrcjkb.javafxwrapper.api

import javafx.stage.Window

interface IJavaFxChooserWrapperInternal: IJavaFxChooserWrapper {

    var swingParentWindow: java.awt.Window?
    var javaFxParentWindow: Window?

}