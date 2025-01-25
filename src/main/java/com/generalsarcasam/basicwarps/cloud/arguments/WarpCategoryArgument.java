package com.generalsarcasam.basicwarps.cloud.arguments;

import com.generalsarcasam.basicwarps.BasicWarps;
import com.generalsarcasam.basicwarps.cloud.DescribedArgumentParser;
import com.generalsarcasam.basicwarps.objects.WarpCategory;
import io.leangen.geantyref.TypeToken;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.context.CommandInput;
import org.incendo.cloud.paper.util.sender.Source;
import org.incendo.cloud.parser.ArgumentParseResult;
import org.jspecify.annotations.NonNull;

@DefaultQualifier(NonNull.class)
public final class WarpCategoryArgument implements DescribedArgumentParser<WarpCategory> {

    @Override
    public TypeToken<WarpCategory> valueType() {
        return TypeToken.get(WarpCategory.class);
    }

    @Override
    public ArgumentParseResult<WarpCategory> parse(final CommandContext<Source> commandContext,
                                                   final CommandInput commandInput) {
        String input = commandInput.readString();

        if (BasicWarps.categories.containsKey(input)) {
            return ArgumentParseResult.success(BasicWarps.categories.get(input));
        }

        return ArgumentParseResult.failure(new UnknownWarpCategoryException(input));

    }

    @Override
    public Iterable<String> stringSuggestions(final CommandContext<Source> commandContext,
                                              final CommandInput input) {
        return BasicWarps.categories.keySet();
    }

    private static final class UnknownWarpCategoryException extends Exception {
        UnknownWarpCategoryException(final String badWarpCategoryKey) {
            super(badWarpCategoryKey + " is not a valid Warp Category name!");
        }

    }
}


