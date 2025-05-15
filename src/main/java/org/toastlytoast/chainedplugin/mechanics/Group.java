package org.toastlytoast.chainedplugin.mechanics;


import org.bukkit.entity.Player;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Group {
    private final String name;
    private final Player owner;
    private final Set<Player> members = new HashSet<>();

    public Group(String name, Player owner) {
        this.name = name;
        this.owner = owner;
        members.add(owner);
    }

    public boolean addMember(Player player) {
        return members.add(player);
    }

    public boolean removeMember(Player player) {
        return members.remove(player);
    }

    public boolean isOwner(Player player) {
        return owner.equals(player);
    }

    public boolean isEmpty() {
        return members.isEmpty();
    }

    public Set<Player> getMembers() {
        return Collections.unmodifiableSet(members);
    }
}
