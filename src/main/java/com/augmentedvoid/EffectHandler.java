package com.augmentedvoid;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EffectHandler implements Listener {
    
    private final DragonHeadManager dragonHeadManager;
    
    public EffectHandler(DragonHeadManager dragonHeadManager) {
        this.dragonHeadManager = dragonHeadManager;
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        
        if (dragonHeadManager.hasAugmentedVoidEffects(player)) {
            // Add subtle visual effects
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 60, 0, false, false), true);
            
            // Add particle effects when near void
            if (player.getLocation().getY() < 15) {
                player.getWorld().spawnParticle(
                    org.bukkit.Particle.PORTAL, 
                    player.getLocation().add(0, 0.5, 0), 
                    5, 
                    0.5, 0.5, 0.5, 
                    0.1
                );
            }
        }
    }
}