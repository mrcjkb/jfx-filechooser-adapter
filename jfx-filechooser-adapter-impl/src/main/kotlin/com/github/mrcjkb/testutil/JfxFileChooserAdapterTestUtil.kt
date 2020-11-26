package com.github.mrcjkb.testutil

import java.io.File
import java.io.IOException

/**
 * Creates an empty directory in the specified [directory], using the given [prefix] and [suffix] to generate its name.
 *
 * If [prefix] is not specified then some unspecified name will be used.
 * If [suffix] is not specified then ".tmp" will be used.
 * If [directory] is not specified then the default temporary-file directory will be used.
 *
 * @return a file object corresponding to a newly-created directory.
 *
 * @throws IOException in case of input/output error.
 * @throws IllegalArgumentException if [prefix] is shorter than three symbols.
 */
fun createTempDirectory(prefix: String = "tmp", suffix: String? = null, directory: File? = null): File {
    val dir = File.createTempFile(prefix, suffix, directory)
    dir.delete()
    if (dir.mkdir()) {
        return dir
    } else {
        throw IOException("Unable to create temporary directory $dir.")
    }
}