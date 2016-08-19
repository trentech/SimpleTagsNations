package com.gmail.trentech.simpletagsnations.tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simpletags.tags.Tag;

public class RankTag extends Tag {

	public static ConcurrentHashMap<String, RankTag> cache = new ConcurrentHashMap<>();

	RankTag(String rank, String tag) {
		super(rank, RankTag.class, tag);
	}

	RankTag(Tag tag) {
		super(tag);
	}

	public static Optional<RankTag> get(String rank) {
		if (cache.containsKey(rank)) {
			return Optional.of(cache.get(rank));
		}

		return Optional.empty();
	}

	public static Optional<RankTag> create(String rank, String tag) {
		if (cache.containsKey(rank)) {
			return Optional.empty();
		}

		return Optional.of(new RankTag(rank, tag));
	}

	public static List<RankTag> getAll() {
		List<RankTag> list = new ArrayList<>();

		for (Entry<String, RankTag> entry : cache.entrySet()) {
			list.add(entry.getValue());
		}

		return list;
	}

	public static Text getDefault(String rank) {
		Tag tag = getDefault().get();
		String text = TextSerializers.FORMATTING_CODE.serialize(tag.getTag()).replace("%RANK%", rank);
		return TextSerializers.FORMATTING_CODE.deserialize(text);
	}

	public static Optional<RankTag> getDefault() {
		if (cache.containsKey("simpletags.DEFAULT")) {
			return Optional.of(cache.get("simpletags.DEFAULT"));
		}

		return Optional.empty();
	}
	
	private static void createDefault() {
		String name = "simpletags.DEFAULT";
		String tag = "&9[%RANK%]";

		if (cache.containsKey(name)) {
			return;
		}

		create(name, tag);
	}
	
	public static void init() {
		ConcurrentHashMap<String, RankTag> hash = new ConcurrentHashMap<>();

		for (Tag tag : getAll(RankTag.class)) {
			hash.put(tag.getName(), new RankTag(tag));
		}

		cache = hash;
		
		createDefault();
	}
}
