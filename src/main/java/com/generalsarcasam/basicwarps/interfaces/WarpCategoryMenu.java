package com.generalsarcasam.basicwarps.interfaces;

import com.generalsarcasam.basicwarps.objects.WarpCategory;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class WarpCategoryMenu {

    WarpCategory category;

    public WarpCategoryMenu(final WarpCategory category) {
        this.category = category;
    }

    public WarpCategory category() {
        return this.category;
    }

}
