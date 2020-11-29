package com.github.mrcjkb.jfxfilechooseradapter.impl

import com.github.mrcjkb.javafxwrapper.api.IDirectoryChooserWrapper
import com.github.mrcjkb.javafxwrapper.api.IFileChooserWrapper
import com.github.mrcjkb.javafxwrapper.api.IJavaFxChooserWrapper
import com.github.mrcjkb.javafxwrapper.impl.KExtensionFilter
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.matchers.collections.shouldContainInOrder
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.io.File
import java.util.stream.Collectors

class FileChooserBuilderTest: StringSpec( {

    "FileChooserBuilder should return file that was chosen by file chooser on open file dialog" {
        val mockJavaFxChooserWrapper: IJavaFxChooserWrapper = mock()
        val mockFileChooserWrapper: IFileChooserWrapper = mock()
        val mockDirectoryChooserWrapper: IDirectoryChooserWrapper = mock()
        val chosenFile = File("TestFile.ext")
        whenever(mockFileChooserWrapper.showOpenDialog())
                .thenReturn(chosenFile)
        val fileChooserBuilder = FileChooserBuilder.Companion.createFileChooserBuilder(
                mockJavaFxChooserWrapper,
                mockFileChooserWrapper,
                mockDirectoryChooserWrapper
        )
        fileChooserBuilder.init()
                .withTitle("")
                .showOpenFileDialog()
                .file shouldBe chosenFile
    }

    "FileChooserBuilder should return file that was chosen by file chooser on save file dialog" {
        val mockJavaFxChooserWrapper: IJavaFxChooserWrapper = mock()
        val mockFileChooserWrapper: IFileChooserWrapper = mock()
        val mockDirectoryChooserWrapper: IDirectoryChooserWrapper = mock()
        val chosenFile = File("TestFile.ext")
        whenever(mockFileChooserWrapper.showSaveDialog())
                .thenReturn(chosenFile)
        val fileChooserBuilder = FileChooserBuilder.Companion.createFileChooserBuilder(
                mockJavaFxChooserWrapper,
                mockFileChooserWrapper,
                mockDirectoryChooserWrapper
        )
        fileChooserBuilder.init()
                .withTitle("")
                .showSaveDialog()
                .file shouldBe chosenFile
    }

    "FileChooserBuilder should return files that were chosen by file chooser on open multiple files dialog" {
        val mockJavaFxChooserWrapper: IJavaFxChooserWrapper = mock()
        val mockFileChooserWrapper: IFileChooserWrapper = mock()
        val mockDirectoryChooserWrapper: IDirectoryChooserWrapper = mock()
        val chosenFile = File("TestFile.ext")
        whenever(mockFileChooserWrapper.showOpenMultipleDialog())
                .thenReturn(mutableListOf(chosenFile))
        val fileChooserBuilder = FileChooserBuilder.Companion.createFileChooserBuilder(
                mockJavaFxChooserWrapper,
                mockFileChooserWrapper,
                mockDirectoryChooserWrapper
        )
        fileChooserBuilder.init()
                .withTitle("")
                .showOpenMultipleFilesDialog()
                .fileList shouldContain chosenFile
    }

    "FileChooserBuilder should return path of file that was chosen by directory chooser on open directory dialog" {
        val mockJavaFxChooserWrapper: IJavaFxChooserWrapper = mock()
        val mockFileChooserWrapper: IFileChooserWrapper = mock()
        val mockDirectoryChooserWrapper: IDirectoryChooserWrapper = mock()
        val chosenFile = File("TestDirectory")
        whenever(mockDirectoryChooserWrapper.showDialog())
                .thenReturn(chosenFile)
        val fileChooserBuilder = FileChooserBuilder.Companion.createFileChooserBuilder(
                mockJavaFxChooserWrapper,
                mockFileChooserWrapper,
                mockDirectoryChooserWrapper
        )
        fileChooserBuilder.init()
                .withTitle("")
                .showOpenDirectoryDialog()
                .directory shouldBe chosenFile.toPath()
    }

    "FileChooserBuilder should get extension from file name if no extension filter has been selected" {
        val mockJavaFxChooserWrapper: IJavaFxChooserWrapper = mock()
        val mockFileChooserWrapper: IFileChooserWrapper = mock()
        val mockDirectoryChooserWrapper: IDirectoryChooserWrapper = mock()
        whenever(mockFileChooserWrapper.showOpenDialog())
                .thenReturn(File("TestFile.ext"))
        whenever(mockFileChooserWrapper.selectedExtensionFilter)
                .thenReturn(null)
        val fileChooserBuilder = FileChooserBuilder.Companion.createFileChooserBuilder(
                mockJavaFxChooserWrapper,
                mockFileChooserWrapper,
                mockDirectoryChooserWrapper
        )
        fileChooserBuilder.init()
                .withTitle("")
                .showOpenFileDialog()
                .lowerCaseFileExtension shouldBe "ext"
    }

    "FileChooserBuilder should get extension from selected extension filter if one has been selected" {
        val mockJavaFxChooserWrapper: IJavaFxChooserWrapper = mock()
        val mockFileChooserWrapper: IFileChooserWrapper = mock()
        val mockDirectoryChooserWrapper: IDirectoryChooserWrapper = mock()
        whenever(mockFileChooserWrapper.showOpenDialog())
                .thenReturn(File("TestFile.ext"))
        whenever(mockFileChooserWrapper.selectedExtensionFilter)
                .thenReturn(KExtensionFilter("", listOf("ext1")))
        val fileChooserBuilder = FileChooserBuilder.Companion.createFileChooserBuilder(
                mockJavaFxChooserWrapper,
                mockFileChooserWrapper,
                mockDirectoryChooserWrapper
        )
        fileChooserBuilder.init()
                .withTitle("")
                .showOpenFileDialog()
                .lowerCaseFileExtension shouldBe "ext1"
    }

    "FileChooserBuilder should get extensions from file names if multiple files have been selected" {
        val mockJavaFxChooserWrapper: IJavaFxChooserWrapper = mock()
        val mockFileChooserWrapper: IFileChooserWrapper = mock()
        val mockDirectoryChooserWrapper: IDirectoryChooserWrapper = mock()
        val expectedExtensions = listOf("ext0", "ext1", "ext2")
        whenever(mockFileChooserWrapper.showOpenMultipleDialog())
                .thenReturn(expectedExtensions.stream()
                        .map { File("Test.$it") }
                        .collect(Collectors.toList()))
        val fileChooserBuilder = FileChooserBuilder.Companion.createFileChooserBuilder(
                mockJavaFxChooserWrapper,
                mockFileChooserWrapper,
                mockDirectoryChooserWrapper
        )
        fileChooserBuilder.init()
                .withTitle("")
                .showOpenMultipleFilesDialog()
                .lowerCaseFileExtensions shouldContainInOrder expectedExtensions
    }

})