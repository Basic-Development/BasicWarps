package com.generalsarcasam.basicwarps.utils;

import com.generalsarcasam.basicwarps.BasicWarps;
import com.generalsarcasam.basicwarps.objects.WarpCategory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class DataTools {
    private DataTools() {
    }

    public static void loadWarps() {

        //We're looking for .json files in the "categories" directory of the plugin data folder
        File categoryFile = BasicWarps.plugin.getDataFolder().toPath().resolve("categories/").toFile();

        //If the categories folder exists
        if (categoryFile.exists()) {
            //Get the files from the categories folder
            File[] files = categoryFile.listFiles();

            //If there weren't any files
            if (files == null) {
                //There aren't any Categories or Warps saved. Nothing for us to load.
                return;
            }

            //Iterate over the files to load them one at a time
            for (File file : files) {
                //Grab the file name (including the extension)
                String fileName = file.getName();

                //Find json files, which are probably category data files
                if (fileName.endsWith(".json")) {
                    //Strip the last 5 characters off to remove the ".json" from the Warp Category Name
                    String warpKey = fileName.substring(0, fileName.length() - 6);

                    //Try to load it as a Warp Category data file
                    WarpCategory.load(warpKey);
                }
            }
        } else {
            //The categories folder didn't exist. Let's create it so data can be saved there.
            try {
                Files.createDirectory(categoryFile.toPath());
            } catch (IOException e) {
                //Shouldn't happen
                BasicWarps.logger.severe("Failed to create folder plugins/BasicWarps/categories!");
                throw new RuntimeException(e);
            }
        }
    }
}
