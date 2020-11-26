package com.github.mrcjkb.jfxfilechooseradapter.impl

fun getLowerCaseExtension(filePart: String?) : String {
    return filePart
            .takeIf { it?.contains(".")?:false }
            ?.let { it.substring(it.lastIndexOf(".") + 1).toLowerCase() }
            ?:run { "" }
}