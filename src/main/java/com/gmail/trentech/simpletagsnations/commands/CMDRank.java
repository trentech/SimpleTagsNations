package com.gmail.trentech.simpletagsnations.commands;

import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simpletagsnations.commands.elements.RankElement;
import com.gmail.trentech.simpletagsnations.tags.RankTag;

public class CMDRank implements CommandExecutor {

	public static CommandSpec cmd = CommandSpec.builder()
			.permission("simpletags.cmd.tag.rank")
			.child(CMDRankDefault.cmd, "default", "d")
			.arguments(new RankElement(Text.of("rank")), GenericArguments.optional(GenericArguments.string(Text.of("tag"))))
			.executor(new CMDRank())
			.build();

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		String name = args.<String>getOne("rank").get();

		Optional<RankTag> optionalRankTag = RankTag.get(name);

		if (!args.hasAny("tag")) {
			if (optionalRankTag.isPresent()) {
				src.sendMessage(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RESET, optionalRankTag.get().getTag()));
			} else {
				src.sendMessage(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RED, RankTag.getDefault(name)));
			}

			src.sendMessage(Text.of(TextColors.GREEN, "Update Tag: ", TextColors.YELLOW, "/tag rank <rank> <tag>"));

			return CommandResult.success();
		}
		String tag = args.<String>getOne("tag").get();

		if (tag.equalsIgnoreCase("reset")) {
			if (optionalRankTag.isPresent()) {
				optionalRankTag.get().setTag(null);
			}
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag reset"));

			return CommandResult.success();
		}

		if (optionalRankTag.isPresent()) {
			RankTag rankTag = optionalRankTag.get();
			rankTag.setTag(tag);
		} else {
			RankTag.create(name, tag);
		}

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag changed to ", TextSerializers.FORMATTING_CODE.deserialize(tag)));

		return CommandResult.success();
	}

}
