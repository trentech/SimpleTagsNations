package com.gmail.trentech.simpletagsnations.commands;

import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.arckenver.nations.object.Nation;
import com.gmail.trentech.simpletagsnations.commands.elements.NationElement;
import com.gmail.trentech.simpletagsnations.tags.NationTag;

public class CMDNation implements CommandExecutor {

	public static CommandSpec cmd = CommandSpec.builder()
			.permission("simpletags.cmd.tag.nation")
			.child(CMDNationDefault.cmd, "default", "d")
			.arguments(new NationElement(Text.of("nation")), GenericArguments.optional(GenericArguments.string(Text.of("tag"))))
			.executor(new CMDNation())
			.build();

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Nation nation = args.<Nation>getOne("nation").get();

		if (src instanceof Player) {
			if (!nation.isMinister(((Player) src).getUniqueId()) && !nation.isMinister(((Player) src).getUniqueId())) {
				throw new CommandException(Text.of(TextColors.RED, "Only the president, minister and console can change nations tag"), false);
			}
		}

		Optional<NationTag> optionalNationTag = NationTag.get(nation.getName());

		if (!args.hasAny("tag")) {
			if (optionalNationTag.isPresent()) {
				src.sendMessage(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RESET, optionalNationTag.get().getTag()));
			} else {
				src.sendMessage(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RED, NationTag.getDefault(nation.getName())));
			}

			src.sendMessage(Text.of(TextColors.GREEN, "Update Tag: ", TextColors.YELLOW, "/tag nation <nation> <tag>"));

			return CommandResult.success();
		}
		String tag = args.<String>getOne("tag").get();

		if (tag.equalsIgnoreCase("reset")) {
			if (optionalNationTag.isPresent()) {
				optionalNationTag.get().setTag(null);
			}
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag reset"));

			return CommandResult.success();
		}

		if (optionalNationTag.isPresent()) {
			NationTag nationTag = optionalNationTag.get();
			nationTag.setTag(tag);
		} else {
			NationTag.create(nation.getName(), tag);
		}

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag changed to ", TextSerializers.FORMATTING_CODE.deserialize(tag)));

		return CommandResult.success();
	}

}
