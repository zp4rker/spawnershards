package com.zp4rker.spawnershards

import com.zp4rker.bukkitutils.KotlinRuntime
import com.zp4rker.spawnershards.listeners.BlockListener
import com.zp4rker.spawnershards.listeners.CraftingListener
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.plugin.java.JavaPlugin

class SpawnerShards : JavaPlugin() {

    private val kotlinVersion = "1.3.61"

    override fun onEnable() {
        KotlinRuntime.setup(this, kotlinVersion)

        arrayOf(BlockListener(this), CraftingListener(this)).forEach { server.pluginManager.registerEvents(it, this) }

        saveDefaultConfig()

        addRecipe()

        server.onlinePlayers.forEach {
            it.inventory.addItem(ItemStack(Material.PRISMARINE_SHARD, 9).apply { itemMeta = itemMeta.apply { lore = listOf("Mob type: Skeleton") } })
        }
    }

    fun isCustomShard(shard: ItemStack) = with (shard.itemMeta) { hasLore() && lore[0].contains(config.getString("lore-prefix", "Mob type:")) }

    fun getShard(type: EntityType) = ItemStack(Material.matchMaterial(config.getString("shard-item.type", "PRISMARINE_SHARD"))).apply {
        itemMeta = itemMeta.apply {
            displayName = ChatColor.translateAlternateColorCodes('&', config.getString("shard-item.name", "Spawner Shard"))
            lore = listOf(ChatColor.translateAlternateColorCodes('&', "${config.getString("lore-prefix", "Mob type:")} ${type.name.toLowerCase().capitalize()}"))
        }
    }

    fun getSpawner(type: EntityType) = ItemStack(Material.MOB_SPAWNER).apply {
        itemMeta = itemMeta.apply {
            lore = listOf(ChatColor.translateAlternateColorCodes('&', "${config.getString("lore-prefix", "Mob type:")} ${type.name.toLowerCase().capitalize()}"))
        }
    }

    private fun addRecipe() {
        server.addRecipe(ShapelessRecipe(NamespacedKey(this, "shardedspawner"), ItemStack(Material.MOB_SPAWNER)).apply {
            addIngredient(9, Material.PRISMARINE_SHARD)
        })
    }

}