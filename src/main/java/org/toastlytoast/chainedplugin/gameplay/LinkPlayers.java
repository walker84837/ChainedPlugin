import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.toastlytoast.chainedplugin.ChainedPlugin;
import org.toastlytoast.chainedplugin.mechanics.GroupManager;

public class LinkPlayers implements Listener {
    private final ChainedPlugin plugin;
    private final GroupManager groupManager;

    public LinkPlayers(ChainedPlugin plugin) {
        this.plugin = plugin;
        this.groupManager = plugin.getGroupManager();
    }

    @EventHandler
    public void onInventoryChange(InventoryClickEvent event) {
        syncInventory((Player) event.getWhoClicked());
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        syncInventory((Player) event.getWhoClicked());
    }

    private void syncInventory(Player player) {
        groupManager.getPlayerGroup(player).ifPresent(group -> {
            ItemStack[] contents = player.getInventory().getContents();
            group.getMembers().forEach(member -> {
                if (!member.equals(player)) {
                    member.getInventory().setContents(contents);
                    member.updateInventory();
                }
            });
        });
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        
        groupManager.getPlayerGroup(player).ifPresent(group -> {
            double newHealth = player.getHealth() - event.getFinalDamage();
            group.getMembers().forEach(member -> {
                if (!member.equals(player)) {
                    member.setHealth(Math.max(0, Math.min(newHealth, 20)));
                }
            });
        });
    }

    @EventHandler
    public void onHungerChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        
        groupManager.getPlayerGroup(player).ifPresent(group -> {
            int newFood = event.getFoodLevel();
            group.getMembers().forEach(member -> {
                if (!member.equals(player)) {
                    member.setFoodLevel(newFood);
                }
            });
        });
    }
}
