package com.gmail.trentech.simpletagsnations.commands;

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

import com.gmail.trentech.simpletagsnations.tags.NationTag;

public class CMDNationDefault implements CommandExecutor {

	public static CommandSpec cmd = CommandSpec.builder()
			.permission("simpletags.cmd.tag.nation.default")
			.arguments(GenericArguments.optional(GenericArguments.string(Text.of("tag"))))
			.executor(new CMDNationDefault())
			.build();

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		NationTag defaultTag = NationTag.getDefault().get();

		if (!args.hasAny("tag")) {
			src.sendMessage(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RESET, defaultTag.getTag()));
			src.sendMessage(Text.of(TextColors.GREEN, "Update Tag: ", TextColors.YELLOW, "/tag nation default <tag>"));

			return CommandResult.success();
		}
		String tag = args.<String>getOne("tag").get();

		defaultTag.setTag(tag);

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag changed to ", TextSerializers.FORMATTING_CODE.deserialize(tag)));

		return CommandResult.success();
	}

}
