package com.gmail.trentech.simpletagsnations.commands.elements;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class RankElement extends CommandElement {

    public RankElement(Text key) {
        super(key);
    }

    @Override
    protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        final String next = args.next();

		if (next.equalsIgnoreCase("citizen") || next.equalsIgnoreCase("minister") || next.equalsIgnoreCase("president")) {
			return next;
		}

		return args.createError(Text.of(TextColors.RED, "Rank not found"));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
    	List<String> list = new ArrayList<>();
    	
    	list.add("citizen");
    	list.add("minister");
    	list.add("president");
		
        return list;
    }

    @Override
    public Text getUsage(CommandSource src) {
        return Text.of(getKey().getColor(), "<" + getKey(), ">");
    }
}
