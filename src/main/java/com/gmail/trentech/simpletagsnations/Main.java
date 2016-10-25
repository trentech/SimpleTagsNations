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

import com.gmail.trentech.helpme.help.Argument;
import com.gmail.trentech.helpme.help.Help;
import com.gmail.trentech.helpme.help.Usage;
import com.gmail.trentech.simpletagsnations.commands.CMDNation;
import com.gmail.trentech.simpletagsnations.commands.CMDRank;
import com.gmail.trentech.simpletagsnations.tags.NationTag;
import com.gmail.trentech.simpletagsnations.tags.RankTag;
import com.gmail.trentech.simpletagsnations.utils.ConfigManager;
import com.gmail.trentech.simpletagsnations.utils.Resource;
import com.google.inject.Inject;

import me.flibio.updatifier.Updatifier;

@Updatifier(repoName = Resource.NAME, repoOwner = Resource.AUTHOR, version = Resource.VERSION)
@Plugin(id = Resource.ID, name = Resource.NAME, version = Resource.VERSION, description = Resource.DESCRIPTION, authors = Resource.AUTHOR, url = Resource.URL, 
	dependencies = {
		@Dependency(id = "Updatifier", optional = true), 
		@Dependency(id = "nations", optional = false), 
		@Dependency(id = "simpletags", optional = false), 
		@Dependency(id = "helpme", version = "0.2.1", optional = true) 
	})
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
		
		Sponge.getEventManager().registerListeners(this, new EventListener());
		
		if (Sponge.getPluginManager().isLoaded("helpme")) {
			Usage usageNation = new Usage(Argument.of("<nation>", "Specifies the name of a nation"))
					.addArgument(Argument.of("<tag>", "Set custom tag. Accepts color codes"));
			
			Help tagNation = new Help("tag nation", "nation", "View and edit nation tags")
					.setPermission("simpletags.cmd.tag.nation")
					.setUsage(usageNation)
					.addExample("/tag nation admin")
					.addExample("/tag nation admin &e[BOSS]")
					.addExample("/tag nation admin reset");
			
			Usage usageRank = new Usage(Argument.of("<rank>", "Specifies the name of a rank"))
					.addArgument(Argument.of("<tag>", "Set custom tag. Accepts color codes"));
			
			Help tagRank = new Help("tag rank", "rank", "View and edit rank tags")
					.setPermission("simpletags.cmd.tag.rank")
					.setUsage(usageRank)
					.addExample("/tag rank admin")
					.addExample("/tag rank admin &e[BOSS]")
					.addExample("/tag rank admin reset");
			
			Help.register(Help.get("tag").get().addChild(tagRank).addChild(tagNation));
		}
	}

	@Listener
	public void onPostInitializationEvent(GamePostInitializationEvent event) {
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
