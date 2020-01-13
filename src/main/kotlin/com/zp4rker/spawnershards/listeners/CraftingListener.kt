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
        if (event.inventory.matrix.any { it == null }) return
        if (!event.inventory.matrix.all(SpawnerShards.config::isCustomShard) || !event.inventory.matrix.all { it == event.inventory.matrix[0] }) {
            event.inventory.result = ItemStack(Material.AIR)
            return
        }

        val type = EntityType.valueOf(ChatColor.stripColor(event.inventory.matrix[0].itemMeta.lore[0]).substring(10).toUpperCase())
        event.inventory.result = SpawnerShards.config.getSpawner(type)
    }

}
