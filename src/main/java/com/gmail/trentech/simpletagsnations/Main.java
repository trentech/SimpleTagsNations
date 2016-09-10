package com.gmail.trentech.simpletagsnations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import com.gmail.trentech.simpletagsnations.commands.CMDNation;
import com.gmail.trentech.simpletagsnations.commands.CMDRank;
import com.gmail.trentech.simpletagsnations.tags.NationTag;
import com.gmail.trentech.simpletagsnations.tags.RankTag;
import com.gmail.trentech.simpletagsnations.utils.ConfigManager;
import com.gmail.trentech.simpletagsnations.utils.Resource;
import com.google.inject.Inject;

import me.flibio.updatifier.Updatifier;

@Updatifier(repoName = Resource.NAME, repoOwner = Resource.AUTHOR, version = Resource.VERSION)
@Plugin(id = Resource.ID, name = Resource.NAME, version = Resource.VERSION, description = Resource.DESCRIPTION, authors = Resource.AUTHOR, url = Resource.URL, dependencies = {@Dependency(id = "Updatifier", optional = true), @Dependency(id = "com.arckenver.nations", optional = false), @Dependency(id = "simpletags", optional = false) })
public class Main {

	@Inject @ConfigDir(sharedRoot = false)
    private Path path;

	@Inject
	private Logger log;

	private static PluginContainer plugin;
	private static Main instance;
	
	@Listener
	public void onPreInitializationEvent(GamePreInitializationEvent event) {
		plugin = Sponge.getPluginManager().getPlugin(Resource.ID).get();
		instance = this;
		
		try {			
			Files.createDirectories(path);		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Listener
	public void onInitializationEvent(GameInitializationEvent event) {
		com.gmail.trentech.simpletags.Main.registerTag(RankTag.class);
		com.gmail.trentech.simpletags.Main.registerCommand(CMDNation.cmd, "nation", "n");
		com.gmail.trentech.simpletags.Main.registerTag(NationTag.class);
		com.gmail.trentech.simpletags.Main.registerCommand(CMDRank.cmd, "rank", "r");

		ConfigManager.init();
	}

	@Listener
	public void onPostInitializationEvent(GamePostInitializationEvent event) {
		Sponge.getEventManager().registerListeners(this, new EventListener());

		RankTag.init();
		NationTag.init();
	}

	public Logger getLog() {
		return log;
	}

	public Path getPath() {
		return path;
	}

	public static PluginContainer getPlugin() {
		return plugin;
	}
	
	public static Main instance() {
		return instance;
	}
}
