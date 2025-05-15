package org.toastlytoast.chainedplugin;

import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;

import org.bukkit.entity.Player;
import org.toastlytoast.chainedplugin.ChainedPlugin;
import org.toastlytoast.chainedplugin.mechanics.GroupManager;

public class ChainCommand {
    public static LiteralCommandNode<CommandSourceStack> createCommand(ChainedPlugin plugin) {
        GroupManager groupManager = plugin.getGroupManager();
        
        return Commands.literal("chain")
            .then(Commands.literal("invite")
                .then(Commands.argument("player", ArgumentTypes.player())
                    .executes(context -> {
                        CommandSourceStack source = context.getSource();
                        Player sender = source.getSender() instanceof Player ? (Player) source.getSender() : null;
                        Player target = context.getArgument(context, PlayerSelectorArgumentResolver.class);
                        
                        if (sender == null) {
                            source.sendMessage(Component.text("This command can only be used by players!", NamedTextColor.RED));
                            return 0;
                        }
                        
                        groupManager.getPlayerGroup(sender).ifPresentOrElse(group -> {
                            if (group.isOwner(sender)) {
                                if (group.addMember(target)) {
                                    sender.sendMessage(Component.text("Invited " + target.getName() + " to your group!", NamedTextColor.GREEN));
                                    target.sendMessage(Component.text("You've been invited to join " + sender.getName() + "'s group!", NamedTextColor.GREEN));
                                } else {
                                    sender.sendMessage(Component.text("Couldn't invite " + target.getName(), NamedTextColor.RED));
                                }
                            } else {
                                sender.sendMessage(Component.text("Only group owners can invite members!", NamedTextColor.RED));
                            }
                        }, () -> {
                            groupManager.createGroup(sender.getName(), sender);
                            sender.sendMessage(Component.text("Created new group!", NamedTextColor.GREEN));
                        });
                        
                        return 1;
                    })))
            .then(Commands.literal("leave")
                .executes(context -> {
                    Player player = (Player) context.getSource().getSender();
                    if (groupManager.removeFromGroup(player)) {
                        player.sendMessage(Component.text("Left the group!", NamedTextColor.GREEN));
                    } else {
                        player.sendMessage(Component.text("You're not in a group!", NamedTextColor.RED));
                    }
                    return 1;
                }))
            .build();
    }
}
