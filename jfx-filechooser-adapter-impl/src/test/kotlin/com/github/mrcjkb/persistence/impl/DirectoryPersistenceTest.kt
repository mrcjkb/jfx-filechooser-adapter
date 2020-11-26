package com.github.mrcjkb.persistence.impl

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.io.File
import java.util.*

class DirectoryPersistenceTest: StringSpec( {

    "DirectoryPersistence should remember valid directory" {
        createTempDir().also {
            val directoryPersistence = DirectoryPersistence(UUID.randomUUID().toString())
            directoryPersistence.lastChosenDirectory = it
            directoryPersistence.lastChosenDirectory shouldBe it
        }
    }

    "DirectoryPersistence should not remember invalid directory" {
        val file = File("/random/path/that/does/not/exist")
        val directoryPersistence = DirectoryPersistence(UUID.randomUUID().toString())
        directoryPersistence.lastChosenDirectory = file
        directoryPersistence.lastChosenDirectory shouldBe null
    }

})