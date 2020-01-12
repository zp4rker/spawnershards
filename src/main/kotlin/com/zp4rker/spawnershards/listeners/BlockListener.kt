package com.zp4rker.spawnershards.listeners

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.block.CreatureSpawner
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

class BlockListener : Listener {

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (event.block.state !is CreatureSpawner || event.itemInHand.type != Material.MOB_SPAWNER) return

        val meta = if (event.itemInHand.hasItemMeta()) event.itemInHand.itemMeta else return
        if (!meta.hasLore() || !meta.lore[0].contains("Mob type:")) return

        val type = ChatColor.stripColor(meta.lore[0]).substring(10).let { EntityType.valueOf(it.toUpperCase()) }
        val spawner = event.block.state as CreatureSpawner

        spawner.spawnedType = type
        spawner.update(true)
    }

}