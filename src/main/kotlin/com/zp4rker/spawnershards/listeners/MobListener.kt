package com.zp4rker.spawnershards.listeners

import com.zp4rker.spawnershards.SpawnerShards
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

class MobListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onMobDeath(event: EntityDeathEvent) {
        event.entity.killer ?: return
        event.drops.add(SpawnerShards.config.getShard(event.entityType))
    }

}