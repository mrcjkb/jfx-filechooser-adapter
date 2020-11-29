package com.github.mrcjkb.jfxfilechooseradapter;

import com.github.mrcjkb.jfxfilechooseradapter.api.IFileChooserBuilder;
import com.github.mrcjkb.jfxfilechooseradapter.api.IJfxFileChooserAdapterModuleContext;
import com.github.mrcjkb.jfxfilechooseradapter.impl.FileChooserBuilder;

public class JfxFileChooserAdapterModuleContext implements IJfxFileChooserAdapterModuleContext {
    @Override
    public IFileChooserBuilder getFileChooserBuilder() {
        return FileChooserBuilder.Companion.createFileChooserBuilder();
    }
}
