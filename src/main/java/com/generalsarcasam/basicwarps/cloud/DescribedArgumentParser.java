package com.generalsarcasam.basicwarps.cloud;

import org.incendo.cloud.paper.util.sender.Source;
import org.incendo.cloud.parser.ArgumentParser;
import org.incendo.cloud.parser.ParserDescriptor;
import org.incendo.cloud.suggestion.BlockingSuggestionProvider;

public interface DescribedArgumentParser<T> extends
        ParserDescriptor<Source, T>,
        ArgumentParser<Source, T>,
        BlockingSuggestionProvider.Strings<Source> {

    @Override
    default ArgumentParser<Source, T> parser() {
        return this;
    }
}
