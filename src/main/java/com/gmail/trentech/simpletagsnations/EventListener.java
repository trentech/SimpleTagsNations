package com.gmail.trentech.simpletagsnations;

import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.message.MessageEvent.MessageFormatter;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.TextTemplate;

import com.arckenver.nations.DataHandler;
import com.arckenver.nations.object.Nation;
import com.arckenver.nations.service.NationsService;
import com.gmail.trentech.simpletags.events.ChangeTagEvent;
import com.gmail.trentech.simpletags.tags.Tag;
import com.gmail.trentech.simpletagsnations.tags.NationTag;
import com.gmail.trentech.simpletagsnations.tags.RankTag;
import com.gmail.trentech.simpletagsnations.utils.ConfigManager;

public class EventListener {

	@Listener(order = Order.LAST)
	public void onMessageChannelEventChat(MessageChannelEvent.Chat event, @First Player player) {
		String mode = ConfigManager.get().getConfig().getNode("mode").getString();

		Text tag;

		if (mode.equalsIgnoreCase("default")) {
			tag = defaultTag(player);
		} else if (mode.equalsIgnoreCase("advanced")) {
			tag = advancedTag(player);
		} else {
			return;
		}

		MessageFormatter formatter = event.getFormatter();

		Text prefix = Text.join(tag, formatter.getHeader().toText());
		formatter.setHeader(TextTemplate.of(prefix));
	}

	@Listener
	public void onChangeTagEventUpdate(ChangeTagEvent.Update event) {
		Tag tag = event.getTag();

		if (tag instanceof RankTag) {
			RankTag.cache.put(tag.getName(), (RankTag) tag);
		} else if (tag instanceof NationTag) {
			NationTag.cache.put(tag.getName(), (NationTag) tag);
		}
	}

	@Listener
	public void onChangeTagEventDelete(ChangeTagEvent.Delete event) {
		Tag tag = event.getTag();

		if (tag instanceof RankTag) {
			RankTag.cache.remove(tag.getName());
		} else if (tag instanceof NationTag) {
			NationTag.cache.remove(tag.getName());
		}
	}

	private Text defaultTag(Player player) {
		Builder nationTagBuilder = Text.builder();

		Optional<NationsService> optionalNationsService = Sponge.getServiceManager().provide(NationsService.class);

		if (!optionalNationsService.isPresent()) {
			return Text.EMPTY;
		}
		NationsService nations = optionalNationsService.get();

		UUID uuid = player.getUniqueId();

		Optional<String> optionalNation = nations.getNationNameOfPlayer(uuid);

		if (optionalNation.isPresent()) {
			Optional<NationTag> optionalTag = NationTag.get(optionalNation.get());

			if (optionalTag.isPresent()) {
				nationTagBuilder.append(optionalTag.get().getTag());
			} else {
				nationTagBuilder.append(NationTag.getDefault(optionalNation.get()));
			}
		}

		String rank;

		if (nations.isMinister(uuid)) {
			rank = "minister";
		} else if (nations.isPresident(uuid)) {
			rank = "president";
		} else {
			rank = "citizen";
		}

		Optional<RankTag> optionalRank = RankTag.get(rank);

		if (optionalRank.isPresent()) {
			nationTagBuilder.append(optionalRank.get().getTag());
		} else {
			nationTagBuilder.append(RankTag.getDefault(rank));
		}

		return nationTagBuilder.build();
	}

	private Text advancedTag(Player player) {
		Builder nationTagBuilder = Text.builder();

		Optional<NationsService> optionalNationsService = Sponge.getServiceManager().provide(NationsService.class);

		if (!optionalNationsService.isPresent()) {
			return Text.EMPTY;
		}
		NationsService nations = optionalNationsService.get();

		UUID uuid = player.getUniqueId();

		Optional<String> optionalNation = nations.getNationNameAtLocation(player.getLocation());

		if (!optionalNation.isPresent()) {
			return Text.EMPTY;
		}

		Optional<NationTag> optionalTag = NationTag.get(optionalNation.get());

		if (optionalTag.isPresent()) {
			nationTagBuilder.append(optionalTag.get().getTag());
		} else {
			nationTagBuilder.append(NationTag.getDefault(optionalNation.get()));
		}

		Nation nation = DataHandler.getNation(optionalNation.get());

		String rank;

		if (nation.isMinister(uuid)) {
			rank = "minister";
		} else if (nation.isPresident(uuid)) {
			rank = "president";
		} else if (nation.isCitizen(uuid)) {
			rank = "citizen";
		} else {
			return nationTagBuilder.build();
		}

		Optional<RankTag> optionalRank = RankTag.get(rank);

		if (optionalRank.isPresent()) {
			nationTagBuilder.append(optionalRank.get().getTag());
		} else {
			nationTagBuilder.append(RankTag.getDefault(rank));
		}

		return nationTagBuilder.build();
	}
}
