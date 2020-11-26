package com.github.mrcjkb.persistence.impl

import com.github.mrcjkb.testutil.createTempDirectory
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.io.File
import java.util.*

class DirectoryPersistenceTest: StringSpec( {

    "DirectoryPersistence should remember valid directory" {
        createTempDirectory().also {
            val directoryPersistence = DirectoryPersistence(UUID.randomUUID().toString())
            directoryPersistence.lastChosenDirectory = it
            directoryPersistence.lastChosenDirectory shouldBe it
        }
    }

    "DirectoryPersistence should not remember invalid directory" {
        val invalidDirectory = File("/random/path/that/does/not/exist")
        val directoryPersistence = DirectoryPersistence(UUID.randomUUID().toString())
        directoryPersistence.lastChosenDirectory = invalidDirectory
        directoryPersistence.lastChosenDirectory shouldBe null
    }

    "DirectoryPersistence should not reset valid directory on invalid directory" {
        createTempDirectory().also {
            val directoryPersistence = DirectoryPersistence(UUID.randomUUID().toString())
            directoryPersistence.lastChosenDirectory = it
            val invalidDirectory = File("/random/path/that/does/not/exist")
            directoryPersistence.lastChosenDirectory = invalidDirectory
            directoryPersistence.lastChosenDirectory shouldBe it
        }
    }

})