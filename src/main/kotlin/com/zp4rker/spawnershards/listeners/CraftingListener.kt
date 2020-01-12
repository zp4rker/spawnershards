package com.zp4rker.spawnershards.listeners

import com.zp4rker.spawnershards.SpawnerShards
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.ItemStack

class CraftingListener : Listener {

    @EventHandler
    fun onPrepareCraft(event: PrepareItemCraftEvent) {
        if (event.recipe == null || event.recipe.result.type != Material.MOB_SPAWNER) return

        if (!event.inventory.matrix.all(SpawnerShards.config::isCustomShard)) {
            event.inventory.result = ItemStack(Material.AIR)
            return
        }

        val type = EntityType.valueOf(ChatColor.stripColor(event.inventory.matrix.first().itemMeta.lore[0]).substring(10).toUpperCase())
        event.inventory.result = SpawnerShards.config.getSpawner(type)
    }

}
