package org.toastlytoast.chainedplugin;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.arguments.EntityArgument;
import io.papermc.paper.lifecycle.LifecycleEventManager;
import io.papermc.paper.lifecycle.LifecycleEvents;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;
import org.toastlytoast.chainedplugin.commands.ChainCommand;
import org.toastlytoast.chainedplugin.gameplay.LinkPlayers;
import org.toastlytoast.chainedplugin.gameplay.GroupManager;

public final class ChainedPlugin extends JavaPlugin
{
    private GroupManager groupManager;
    private LinkPlayers linkPlayers;

    @Override
    public void onEnable() {
        this.groupManager = new GroupManager();
        this.linkPlayers = new LinkPlayers(this);
        private GroupManager groupManager;
        private LinkPlayers linkPlayers;

        // Register events
        Bukkit.getPluginManager().registerEvents(linkPlayers, this);

        // Paper command registration
        LifecycleEventManager lifecycleManager = getLifecycleManager();
        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register("chain", ChainCommand.createCommand(this));
        });

        getLogger().info("ChainedPlugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ChainedPlugin disabled!");
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }
}
