package com.generalsarcasam.basicwarps.objects;

import com.generalsarcasam.basicwarps.BasicWarps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@DefaultQualifier(NonNull.class)
public final class WarpCategory {

    private static final GsonComponentSerializer GSON_COMPONENT_SERIALIZER = GsonComponentSerializer.gson();
    private static final Gson GSON = GSON_COMPONENT_SERIALIZER.serializer();

    private static final TypeToken<SavedWarpCategory> SERIALIZED_SAVED_WARP_CATEGORY_TYPE_TOKEN = new TypeToken<>() {
    };
    private static final TypeToken<SavedWarp> SAVED_WARP_TYPE_TOKEN = new TypeToken<>() {
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

        SavedWarpCategory savedCategory = GSON.fromJson(dataString, SERIALIZED_SAVED_WARP_CATEGORY_TYPE_TOKEN);

        ItemStack icon = ItemStack.deserializeBytes(savedCategory.icon());
        Map<String, String> savedWarps = savedCategory.savedWarps();
        Map<String, Warp> warps = new HashMap<>();

        //Make the Warp Category with No Warps in the Map to Update After Loading
        WarpCategory category = new WarpCategory(key, warps, icon);

        //Deserialize Warp Data Strings into Warps
        for (String warpDataString : savedWarps.values()) {

            SavedWarp savedWarp = GSON.fromJson(warpDataString, SAVED_WARP_TYPE_TOKEN);

            String warpKey = savedWarp.key();

            Map<String, Object> serializedLocation = savedWarp.serializedLocation();
            Location warpLocation = Location.deserialize(serializedLocation);

            ItemStack warpIcon = ItemStack.deserializeBytes(savedWarp.icon());

            Warp warp = new Warp(warpKey, category, warpIcon, warpLocation);
            warps.put(warpKey, warp);
        }

        //Add the Warps to the Category that we already created
        category.warps(warps);

        //Print to Console that Warps were Loaded
        int warpsLoaded = category.warps.size();
        BasicWarps.logger.info("Loaded Warp Category " + category.key + " with " + warpsLoaded + " warps.");

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

        Map<String, String> savedWarps = new HashMap<>();

        for (Warp warp : this.warps.values()) {
            String key = warp.key();
            byte[] icon = warp.warpIcon().serializeAsBytes();
            Location location = warp.location();
            Map<String, Object> serializedLocation = location.serialize();

            SavedWarp savedWarp = new SavedWarp(key, icon, serializedLocation);

            String warpDataString = GSON.toJson(savedWarp);

            savedWarps.put(key, warpDataString);
        }

        SavedWarpCategory savedWarpCategory = new SavedWarpCategory(
                this.key, this.icon.serializeAsBytes(), savedWarps
        );

        //Convert Warps Data to Json
        String dataString = GSON.toJson(savedWarpCategory);

        //Save the Data to a File
        try {
            Files.writeString(target, dataString);
        } catch (IOException e) {
            BasicWarps.logger.severe("Failed to save Warp Category " + this.key);
        }

    }

    private record SavedWarpCategory(String key,
                                     byte[] icon,
                                     Map<String, String> savedWarps) {

    }

    private record SavedWarp(String key,
                            byte[] icon,
                            Map<String, Object> serializedLocation) {
    }

}
