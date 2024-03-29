package com.generalsarcasam.basicwarps.utils;

import com.generalsarcasam.basicwarps.BasicWarps;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;
import static net.kyori.adventure.text.format.TextDecoration.State.FALSE;

@DefaultQualifier(NonNull.class)
public final class Messages {

    public static final Component PREFIX = BasicWarps.MINI.deserialize(
            "<gradient:#8737FF:#C800FF><bold>Warps » </bold></gradient>");
    public static final Component ADMIN_PREFIX = BasicWarps.MINI.deserialize(
            "<gradient:#8737FF:#C800FF:#FF3C04><bold>WarpsAdmin » </bold></gradient>");
    public static final TextColor MESSAGE_COLOR = TextColor.color(0x50ffc0);
    public static final TextColor DARKER_COLOR = TextColor.color(0x349170);
    public static final TextColor ERROR_COLOR = TextColor.color(0xff3d3d);

    private Messages() {
    }

    public static Component format(final String text,
                                   final TextColor color) {
        return PREFIX.append(Component.text(text, color)
                .decorationIfAbsent(BOLD, FALSE)
                .decorationIfAbsent(ITALIC, FALSE));
    }

    public static Component confirmGenericAction() {
        return format("This action requires confirmation! To confirm, type /warps confirm", ERROR_COLOR);
    }
}
