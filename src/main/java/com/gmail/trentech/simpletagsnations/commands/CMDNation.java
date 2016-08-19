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

import com.arckenver.nations.DataHandler;
import com.arckenver.nations.object.Nation;
import com.gmail.trentech.simpletagsnations.tags.NationTag;
import com.gmail.trentech.simpletagsnations.utils.Help;

public class CMDNation implements CommandExecutor {

	public static CommandSpec cmd = CommandSpec.builder()
		    .permission("simpletags.cmd.tag.nation")
		    .child(CMDNationDefault.cmd, "default", "d")
		    .arguments(GenericArguments.optional(GenericArguments.string(Text.of("name"))), GenericArguments.optional(GenericArguments.string(Text.of("tag"))))
		    .executor(new CMDNation())
		    .build();
	
	public CMDNation() {
		Help help = new Help("nation", "nation", " View and edit nation tags");
		help.setSyntax(" /tag nation <nation> <tag>\n /t g <nation> <tag>");
		help.setExample(" /tag nation admin\n /tag nation admin &e[BOSS]\n /tag nation admin reset");
		help.save();
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!args.hasAny("name")) {
			src.sendMessage(Text.of(TextColors.YELLOW, "/tag nation <nation> <tag>"));
			return CommandResult.empty();
		}
		String name = args.<String> getOne("name").get();

		Nation nation = DataHandler.getNation(name);
		
		if(nation == null) {
			src.sendMessage(Text.of(TextColors.DARK_RED, "Nation does not exist!"));
			return CommandResult.empty();
		}

		if(src instanceof Player) {
			if(!nation.isMinister(((Player) src).getUniqueId()) && !nation.isMinister(((Player) src).getUniqueId())) {
				src.sendMessage(Text.of(TextColors.DARK_RED, "Only the president, minister and console can change nations tag"));
				return CommandResult.empty();
			}
		}
		
		Optional<NationTag> optionalNationTag = NationTag.get(name);

		if (!args.hasAny("tag")) {
			List<Text> list = new ArrayList<>();

			if (optionalNationTag.isPresent()) {
				list.add(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RESET, optionalNationTag.get().getTag()));
			} else {
				list.add(Text.of(TextColors.GREEN, "Current Tag: ", TextColors.RED, NationTag.getDefault(name)));
			}

			list.add(Text.of(TextColors.GREEN, "Update Tag: ", TextColors.YELLOW, "/tag nation <nation> <tag>"));

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
			NationTag.create(name, tag);
		}

		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Tag changed to ", TextSerializers.FORMATTING_CODE.deserialize(tag)));

		return CommandResult.success();
	}

}
