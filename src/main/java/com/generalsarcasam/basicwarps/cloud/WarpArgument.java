package com.generalsarcasam.basicwarps.cloud;

import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import com.generalsarcasam.basicwarps.BasicWarps;
import com.generalsarcasam.basicwarps.objects.Warp;
import com.generalsarcasam.basicwarps.objects.WarpCategory;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@DefaultQualifier(NonNull.class)
public class WarpArgument extends CommandArgument<CommandSender, Warp> {

    public WarpArgument() {
        super(true, "warp", new WarpParser(), Warp.class);
    }

    private static final class WarpParser implements ArgumentParser<CommandSender, Warp> {

        @Override
        public ArgumentParseResult<@NonNull Warp> parse(
                final CommandContext<CommandSender> commandContext,
                final Queue<String> inputQueue
        ) {

            String key = inputQueue.remove();
            @Nullable Warp warp = null;

            for (WarpCategory category : BasicWarps.categories.values()) {
                if (category.warps().containsKey(key)) {
                    warp = category.warps().get(key);
                }
            }

            if (warp == null) {
                return ArgumentParseResult.failure(new UnknownWarpException());
            }

            return ArgumentParseResult.success(warp);

        }

        @Override
        public @NonNull List<@NonNull String> suggestions(
                final CommandContext<CommandSender> commandContext,
                final String input
        ) {

            CommandSender sender = commandContext.getSender();
            if (sender.hasPermission("")) {
                return new ArrayList<>(BasicWarps.categories.keySet());

            }

            return new ArrayList<>();

        }
    }

    private static final class UnknownWarpException extends Exception {
    }

}
