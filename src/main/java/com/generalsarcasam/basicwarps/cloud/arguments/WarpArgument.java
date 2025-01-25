package com.generalsarcasam.basicwarps.cloud.arguments;

import com.generalsarcasam.basicwarps.BasicWarps;
import com.generalsarcasam.basicwarps.cloud.DescribedArgumentParser;
import com.generalsarcasam.basicwarps.objects.Warp;
import com.generalsarcasam.basicwarps.objects.WarpCategory;
import io.leangen.geantyref.TypeToken;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.paper.util.sender.Source;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

@DefaultQualifier(NonNull.class)
public final class WarpArgument implements DescribedArgumentParser<Warp> {

    @Override
    public TypeToken<Warp> valueType() {
        return TypeToken.get(Warp.class);
    }

    @Override
    public ArgumentParseResult<Warp> parse(final CommandContext<Source> commandContext,
                                           final CommandInput commandInput) {
        String input = commandInput.readString();

        for (WarpCategory category : BasicWarps.categories.values()) {

            if (category.warps().containsKey(input)) {
                return ArgumentParseResult.success(category.warps().get(input));
            }

        }

        return ArgumentParseResult.failure(new UnknownWarpException(input));

    }

    @Override
    public Iterable<String> stringSuggestions(final CommandContext<Source> commandContext,
                                              final CommandInput input) {
        List<String> warpNames = new ArrayList<>();

        for (WarpCategory category : BasicWarps.categories.values()) {
            warpNames.addAll(category.warps().keySet());
        }

        return warpNames;
    }

    private static final class UnknownWarpException extends Exception {
        UnknownWarpException(final String badWarpKey) {
            super(badWarpKey + " is not a valid Warp name!");
        }

    }
}


