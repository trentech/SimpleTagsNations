package com.gmail.trentech.simpletagsnations.commands.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.arckenver.nations.DataHandler;
import com.arckenver.nations.object.Nation;

public class NationElement extends CommandElement {

    public NationElement(Text key) {
        super(key);
    }

    @Override
    protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        final String next = args.next();

		Nation nation = DataHandler.getNation(next);

		if (nation != null) {
			return nation;
		}

		return args.createError(Text.of(TextColors.RED, "Nation not found"));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
    	List<String> list = new ArrayList<>();
    	
    	for(Entry<UUID, Nation> entry : DataHandler.getNations().entrySet()) {
    		list.add(entry.getValue().getName());
    	}

        return list;
    }

    @Override
    public Text getUsage(CommandSource src) {
        return Text.of(getKey().getColor(), "<" + getKey(), ">");
    }
}
