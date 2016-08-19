package com.gmail.trentech.simpletagsnations.commands;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList.Builder;
import org.spongepowered.api.service.pagination.PaginationService;
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
			List<Text> list = new ArrayList<>();

			list.add(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RESET, defaultTag.getTag()));
			list.add(Text.of(Text.of(TextColors.GREEN, "Update Tag: ", TextColors.YELLOW, "/tag nation default <tag>")));

			if (src instanceof Player) {
				Builder pages = Sponge.getServiceManager().provide(PaginationService.class).get().builder();

				pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "Nation")).build());

				pages.contents(list);

				pages.sendTo(src);
			} else {
				for (Text text : list) {
					src.sendMessage(text);
				}
			}

			return CommandResult.success();
		}
		String tag = args.<String> getOne("tag").get();

		defaultTag.setTag(tag);

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag changed to ", TextSerializers.FORMATTING_CODE.deserialize(tag)));

		return CommandResult.success();
	}

}
