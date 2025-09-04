package com.augmentedvoid;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PotionManager {
    
    private final Map<UUID, Long> activePotions = new HashMap<>();
    private static final long POTION_DURATION = 20 * 60 * 5; // 5 minutes in ticks
    
    public PotionManager() {
    }
    
    public ItemStack createAugmentedVoidPotion() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        
        if (meta != null) {
            meta.displayName(Component.text("Augmented Void").color(NamedTextColor.DARK_PURPLE));
            meta.lore(Arrays.asList(
                Component.text("A mysterious potion that grants").color(NamedTextColor.GRAY),
                Component.text("supernatural protection when consumed.").color(NamedTextColor.GRAY),
                Component.empty(),
                Component.text("Effects when active:").color(NamedTextColor.DARK_GRAY),
                Component.text("• Walk on void with speed boost").color(NamedTextColor.YELLOW),
                Component.text("• No fall damage").color(NamedTextColor.YELLOW),
                Component.text("• Fire/Lava immunity").color(NamedTextColor.YELLOW),
                Component.text("• Special abilities with dragon head").color(NamedTextColor.YELLOW),
                Component.empty(),
                Component.text("Duration: 5 minutes").color(NamedTextColor.GOLD)
            ));
            
            // Set base potion data to make it drinkable
            meta.setBasePotionData(new PotionData(PotionType.NIGHT_VISION, false, false));
            meta.setColor(org.bukkit.Color.fromRGB(75, 0, 130)); // Indigo color
            
            // Add custom model data for resource pack support
            meta.setCustomModelData(1001); // This will be used for the eye of ender texture
            
            potion.setItemMeta(meta);
        }
        
        return potion;
    }
    
    public boolean isAugmentedVoidPotion(ItemStack item) {
        if (item == null || item.getType() != Material.POTION) {
            return false;
        }
        
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return false;
        }
        
        return meta.displayName().equals(Component.text("Augmented Void").color(NamedTextColor.DARK_PURPLE));
    }
    
    public void activatePotion(Player player) {
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        activePotions.put(playerId, currentTime + (POTION_DURATION * 50)); // Convert ticks to ms
        
        // Give visual feedback
        player.sendMessage(Component.text("The void's power flows through you...").color(NamedTextColor.DARK_PURPLE));
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, (int)POTION_DURATION, 0, false, false));
    }
    
    public boolean hasActivePotionEffect(Player player) {
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        
        if (activePotions.containsKey(playerId)) {
            if (currentTime < activePotions.get(playerId)) {
                return true;
            } else {
                activePotions.remove(playerId);
                player.sendMessage(Component.text("The void's power fades from your body...").color(NamedTextColor.GRAY));
            }
        }
        return false;
    }
    
    public void drainPotion(Player player) {
        UUID playerId = player.getUniqueId();
        if (activePotions.containsKey(playerId)) {
            activePotions.remove(playerId);
            
            // Remove night vision effect
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            
            // Give back the potion
            ItemStack potion = createAugmentedVoidPotion();
            player.getInventory().addItem(potion);
            
            player.sendMessage(Component.text("The void's power has been drained back into a potion.")
                .color(NamedTextColor.GREEN));
        } else {
            player.sendMessage(Component.text("You don't have an active Augmented Void effect to drain.")
                .color(NamedTextColor.RED));
        }
    }
}