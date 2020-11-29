package com.github.mrcjkb.persistence.impl

import com.github.mrcjkb.persistence.api.IDirectoryPersistence
import java.io.File
import java.util.prefs.Preferences

class DirectoryPersistence(identifier: String): IDirectoryPersistence {

    private val lastChosenDirectoryKey = "lastChosenDirectory"
    private var preferences: Preferences = Preferences.userRoot().node(identifier)
    override var lastChosenDirectory: File?
        get() { return preferences[lastChosenDirectoryKey, null]?.let { return File(it) } }
        set(value) { value?.takeIf { it.isDirectory }?.let {preferences.put(lastChosenDirectoryKey, value.absolutePath) } }

}