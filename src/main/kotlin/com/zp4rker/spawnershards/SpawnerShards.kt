package com.zp4rker.spawnershards

import com.zp4rker.bukkitutils.KotlinRuntime
import com.zp4rker.spawnershards.listeners.BlockListener
import com.zp4rker.spawnershards.listeners.CraftingListener
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.plugin.java.JavaPlugin

class SpawnerShards : JavaPlugin() {

    private val kotlinVersion = "1.3.61"

    override fun onEnable() {
        KotlinRuntime.setup(this, kotlinVersion)

        arrayOf(BlockListener(), CraftingListener()).forEach { server.pluginManager.registerEvents(it, this) }

        addRecipe()

        server.onlinePlayers.forEach {
            it.inventory.addItem(ItemStack(Material.PRISMARINE_SHARD, 9).apply { itemMeta = itemMeta.apply { lore = listOf("Mob type: Skeleton") } })
        }
    }

    private fun addRecipe() {
        server.addRecipe(ShapelessRecipe(NamespacedKey(this, "shardedspawner"), ItemStack(Material.MOB_SPAWNER)).apply {
            addIngredient(9, Material.PRISMARINE_SHARD)
        })
    }

}