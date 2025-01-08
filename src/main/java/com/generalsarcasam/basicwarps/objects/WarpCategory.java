package com.generalsarcasam.basicwarps.objects;

import com.generalsarcasam.basicwarps.BasicWarps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@DefaultQualifier(NonNull.class)
public final class WarpCategory {

    private static final Gson GSON = new Gson();
    private static final TypeToken<WarpCategory> SERIALIZED_TYPE = new TypeToken<>() {
    };

    String key;
    Map<String, Warp> warps;
    ItemStack icon;

    public WarpCategory(final String key,
                        final Map<String, Warp> warps,
                        final ItemStack icon) {
        this.key = key;
        this.warps = warps;
        this.icon = icon;

        BasicWarps.categories.put(this.key, this);
    }

    public void key(final String key) {
        this.key = key;
        this.save();
    }

    public void warps(final Map<String, Warp> warpList) {
        this.warps = warpList;
        this.save();
    }

    public void icon(final ItemStack icon) {
        this.icon = icon;
        this.save();
    }

    public String key() {
        return this.key;
    }

    public Map<String, Warp> warps() {
        return this.warps;
    }

    public ItemStack icon() {
        return this.icon;
    }

    public void save() {
        //ToDo: Break a WarpCategory out into more primitive types to serialize to .json properly,
        // as throwing GSON a WarpCategory very much doesn't ~just work~

        //Save the data to a file in the Categories Folder of the BasicWarps plugin folder
        Path target = BasicWarps.plugin.getDataFolder().toPath()
                .resolve("categories/" + this.key + ".json");

        //Create warps.json if it doesn't exist
        if (Files.notExists(target)) {
            try {
                Files.createFile(target);
            } catch (IOException e) {
                // shouldn't happen
                throw new RuntimeException(e);
            }
        }

        //Convert Warps Data to Json
        String dataString = GSON.toJson(this);

        //Save the Data to a File
        try {
            Files.writeString(target, dataString);
        } catch (IOException e) {
            BasicWarps.logger.severe("Failed to save Warp Category " + this.key);
        }

    }

    public static void load(final String key) {
        //Data should be saved in a file in the Categories Folder of the BasicWarps plugin folder
        Path target = BasicWarps.plugin.getDataFolder().toPath()
                .resolve("categories/" + key + ".json");

        if (Files.notExists(target)) {
            BasicWarps.logger.warning("Failed to load Warp Category " + key);
            return;
        }

        String dataString;

        try {
            dataString = Files.readString(target);
        } catch (IOException e) {
            BasicWarps.logger.severe("Failed to load Warp Category! Invalid json in file " + target);
            throw new RuntimeException(e);
        }

        WarpCategory category = GSON.fromJson(dataString, SERIALIZED_TYPE);
        BasicWarps.categories.put(category.key, category);

        int warpsLoaded = category.warps.size();
        BasicWarps.logger.info("Loaded Warp Category " + category.key + " with " + warpsLoaded + "warps.");

    }
}
