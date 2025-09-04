package com.augmentedvoid;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerAbilityManager {
    private final Map<UUID, Long> lastAbilityUse = new HashMap<>();
    private final PotionManager potionManager;
    
    private static final long ABILITY_COOLDOWN = 30 * 1000; // 30 seconds in milliseconds
    private static final int ABILITY_RANGE = 10; // Blocks
    private static final int WITHER_EFFECT_DURATION = 400; // 20 seconds
    private static final int WITHER_EFFECT_AMPLIFIER = 1; // Wither II
    private static final int SLOWNESS_EFFECT_DURATION = 400; // 20 seconds
    private static final int SLOWNESS_EFFECT_AMPLIFIER = 1; // Slowness II
    private static final int BLINDNESS_EFFECT_DURATION = 400; // 20 seconds
    private static final int BLINDNESS_EFFECT_AMPLIFIER = 0; // Blindness I
    
    public PlayerAbilityManager(PotionManager potionManager) {
        this.potionManager = potionManager;
    }
    
    public void useAbility(Player player, int abilityLevel) {
        if (player == null) {
            return;
        }
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        
        // Check cooldown
        if (lastAbilityUse.containsKey(playerId)) {
            long lastUse = lastAbilityUse.get(playerId);
            if (currentTime - lastUse < ABILITY_COOLDOWN) {
                long remaining = (ABILITY_COOLDOWN - (currentTime - lastUse)) / 1000;
                player.sendMessage(Component.text("Ability is on cooldown for " + remaining + " more seconds", 
                    NamedTextColor.RED));
                return;
            }
        }
        
        // Check if player has active potion effect
        if (potionManager != null && !potionManager.hasActivePotionEffect(player)) {
            player.sendMessage(Component.text("You need an active Augmented Void effect to use abilities!", NamedTextColor.RED));
            return;
        }
        
        if (abilityLevel <= 0) {
            player.sendMessage(Component.text("Invalid ability level!", NamedTextColor.RED));
            return;
        }
        
        if (abilityLevel != 1) {
            player.sendMessage(Component.text("Only ability level 1 is available!", NamedTextColor.RED));
            return;
        }
        
        // Apply effects to nearby entities
        applyAbilityEffects(player, abilityLevel);
        
        // Set cooldown
        lastAbilityUse.put(playerId, currentTime);
        
        player.sendMessage(Component.text("Void ability activated! Nearby enemies are afflicted.", NamedTextColor.DARK_PURPLE));
    }
    
    private void applyAbilityEffects(Player player, int abilityLevel) {
        // Find nearby living entities (within range)
        for (org.bukkit.entity.Entity entity : player.getNearbyEntities(ABILITY_RANGE, ABILITY_RANGE, ABILITY_RANGE)) {
            if (entity instanceof LivingEntity && !entity.equals(player)) {
                LivingEntity target = (LivingEntity) entity;
                
                // Apply Wither effect
                target.addPotionEffect(new PotionEffect(
                    PotionEffectType.WITHER, 
                    WITHER_EFFECT_DURATION,
                    WITHER_EFFECT_AMPLIFIER,
                    false, 
                    true
                ));
                
                // Apply Slowness effect
                target.addPotionEffect(new PotionEffect(
                    PotionEffectType.SLOWNESS, 
                    SLOWNESS_EFFECT_DURATION,
                    SLOWNESS_EFFECT_AMPLIFIER,
                    false, 
                    true
                ));
                
                // Apply Blindness effect
                target.addPotionEffect(new PotionEffect(
                    PotionEffectType.BLINDNESS, 
                    BLINDNESS_EFFECT_DURATION,
                    BLINDNESS_EFFECT_AMPLIFIER,
                    false, 
                    true
                ));
            }
        }
    }
}