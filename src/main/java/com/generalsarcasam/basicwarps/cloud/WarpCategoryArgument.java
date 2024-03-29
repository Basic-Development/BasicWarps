package com.generalsarcasam.basicwarps.cloud;

import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.parser.ArgumentParseResult;
import cloud.commandframework.arguments.parser.ArgumentParser;
import cloud.commandframework.context.CommandContext;
import com.generalsarcasam.basicwarps.BasicWarps;
import com.generalsarcasam.basicwarps.objects.WarpCategory;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@DefaultQualifier(NonNull.class)
public class WarpCategoryArgument extends CommandArgument<CommandSender, WarpCategory> {

    public WarpCategoryArgument() {
        super(true, "category", new WarpCategoryParser(), WarpCategory.class);
    }

    private static final class WarpCategoryParser implements ArgumentParser<CommandSender, WarpCategory> {

        @Override
        public ArgumentParseResult<@NonNull WarpCategory> parse(
                final CommandContext<CommandSender> commandContext,
                final Queue<String> inputQueue
        ) {

            String key = inputQueue.remove();
            @Nullable WarpCategory category = BasicWarps.categories.get(key);

            if (category == null) {
                return ArgumentParseResult.failure(new UnknownCategoryException());
            }

            return ArgumentParseResult.success(category);

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

    private static final class UnknownCategoryException extends Exception {
    }

}
