package com.gmail.trentech.simpletagsnations.tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.gmail.trentech.simpletags.tags.Tag;

public class NationTag extends Tag {

	public static ConcurrentHashMap<String, NationTag> cache = new ConcurrentHashMap<>();

	NationTag(String nationName, String tag) {
		super(nationName, NationTag.class, tag);
	}

	NationTag(Tag tag) {
		super(tag);
	}

	public static Optional<NationTag> get(String nationName) {
		if (cache.containsKey(nationName)) {
			return Optional.of(cache.get(nationName));
		}

		return Optional.empty();
	}

	public static Optional<NationTag> create(String nationName, String tag) {
		if (cache.containsKey(nationName)) {
			return Optional.empty();
		}

		return Optional.of(new NationTag(nationName, tag));
	}

	public static List<NationTag> getAll() {
		List<NationTag> list = new ArrayList<>();

		for (Entry<String, NationTag> entry : cache.entrySet()) {
			list.add(entry.getValue());
		}

		return list;
	}

	public static Text getDefault(String nationName) {
		Tag tag = getDefault().get();
		String text = TextSerializers.FORMATTING_CODE.serialize(tag.getTag()).replace("%NATION%", nationName);
		return TextSerializers.FORMATTING_CODE.deserialize(text);
	}

	public static Optional<NationTag> getDefault() {
		if (cache.containsKey("simpletags.DEFAULT")) {
			return Optional.of(cache.get("simpletags.DEFAULT"));
		}

		return Optional.empty();
	}

	private static void createDefault() {
		String name = "simpletags.DEFAULT";
		String tag = "&b[%NATION%]";

		if (cache.containsKey(name)) {
			return;
		}

		create(name, tag);
	}

	public static void init() {
		ConcurrentHashMap<String, NationTag> hash = new ConcurrentHashMap<>();

		for (Tag tag : getAll(NationTag.class)) {
			hash.put(tag.getName(), new NationTag(tag));
		}

		cache = hash;

		createDefault();
	}
}
