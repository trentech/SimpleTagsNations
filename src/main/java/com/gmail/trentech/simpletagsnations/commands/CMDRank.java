package com.gmail.trentech.simpletagsnations.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import com.gmail.trentech.simpletagsnations.tags.RankTag;
import com.gmail.trentech.simpletagsnations.utils.Help;

public class CMDRank implements CommandExecutor {

	public static CommandSpec cmd = CommandSpec.builder()
		    .permission("simpletags.cmd.tag.rank")
		    .child(CMDRankDefault.cmd, "default", "d")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))), GenericArguments.optional(GenericArguments.string(Text.of("tag"))))
		    .executor(new CMDRank())
		    .build();
	
	public CMDRank() {
		Help help = new Help("rank", "rank", " View and edit rank tags");
		help.setSyntax(" /tag rank <rank> <tag>\n /t g <rank> <tag>");
		help.setExample(" /tag rank admin\n /tag rank admin &e[BOSS]\n /tag rank admin reset");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!args.hasAny("name")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/tag rank <rank> <tag>"));
			return CommandResult.empty();
		}
		String name = args.<String> getOne("name").get();

		if(!name.equalsIgnoreCase("citizen") && !name.equalsIgnoreCase("minister") && !name.equalsIgnoreCase("president")) {
			src.sendMessage(Text.of(TextColors.DARK_RED, "Rank does not exist!"));
			return CommandResult.empty();
		}

		Optional<RankTag> optionalRankTag = RankTag.get(name);

		if (!args.hasAny("tag")) {
			List<Text> list = new ArrayList<>();

			if (optionalRankTag.isPresent()) {
				list.add(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RESET, optionalRankTag.get().getTag()));
			} else {
				list.add(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RED, RankTag.getDefault(name)));
			}

			list.add(Text.of(TextColors.GREEN, "Update Tag: ", TextColors.YELLOW, "/tag rank <rank> <tag>"));

			if (src instanceof Player) {
				Builder pages = Sponge.getServiceManager().provide(PaginationService.class).get().builder();

				pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "Rank")).build());

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
