package org.toastlytoast.chainedplugin.mechanics;


import org.bukkit.entity.Player;
import java.util.*;

public class GroupManager {
    private final Map<String, Group> groups = new HashMap<>();
    private final Map<Player, String> playerGroups = new HashMap<>();

    public void createGroup(String name, Player owner) {
        groups.put(name, new Group(name, owner));
        playerGroups.put(owner, name);
    }

    public void disbandGroup(String name) {
        Group group = groups.remove(name);
        if (group != null) {
            group.getMembers().forEach(playerGroups::remove);
        }
    }

    public boolean addToGroup(String groupName, Player player) {
        Group group = groups.get(groupName);
        if (group != null && !playerGroups.containsKey(player)) {
            group.addMember(player);
            playerGroups.put(player, groupName);
            return true;
        }
        return false;
    }

    public boolean removeFromGroup(Player player) {
        String groupName = playerGroups.remove(player);
        if (groupName != null) {
            Group group = groups.get(groupName);
            if (group != null) {
                group.removeMember(player);
                if (group.isEmpty()) {
                    disbandGroup(groupName);
                }
                return true;
            }
        }
        return false;
    }

    public Optional<Group> getPlayerGroup(Player player) {
        return Optional.ofNullable(playerGroups.get(player))
            .map(groups::get);
    }

    public Optional<Group> getGroup(String name) {
        return Optional.ofNullable(groups.get(name));
    }
}
