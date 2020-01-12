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

    companion object {
        lateinit var config: Config
    }

    private val kotlinVersion = "1.3.61"

    override fun onEnable() {
        KotlinRuntime.setup(this, kotlinVersion)

        SpawnerShards.config = Config(this)

        arrayOf(BlockListener(this), CraftingListener()).forEach { server.pluginManager.registerEvents(it, this) }

        saveDefaultConfig()

        server.addRecipe(ShapelessRecipe(NamespacedKey(this, "shardedspawner"), ItemStack(Material.MOB_SPAWNER)).apply {
            addIngredient(9, Material.PRISMARINE_SHARD)
        })

        server.onlinePlayers.forEach {
            it.inventory.addItem(SpawnerShards.config.getShard(EntityType.SHEEP).apply { amount = 9 })
        }
    }

    class Config(private val plugin: JavaPlugin) {
        fun isCustomShard(shard: ItemStack): Boolean {
            return with(shard.itemMeta) { hasLore() && lore[0].contains(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("lore-prefix", "Mob type:"))) }
        }

        fun getShard(type: EntityType): ItemStack {
            return ItemStack(Material.matchMaterial(plugin.config.getString("shard-item.type", "PRISMARINE_SHARD"))).apply {
                itemMeta = itemMeta.apply {
                    displayName = ChatColor.translateAlternateColorCodes('&', plugin.config.getString("shard-item.name", "Spawner Shard"))
                    lore = listOf(ChatColor.translateAlternateColorCodes('&', "${plugin.config.getString("lore-prefix", "Mob type:")} ${type.name.toLowerCase().capitalize()}"))
                }
            }
        }

        fun getSpawner(type: EntityType): ItemStack {
            return ItemStack(Material.MOB_SPAWNER).apply {
                itemMeta = itemMeta.apply {
                    lore = listOf(ChatColor.translateAlternateColorCodes('&', "${plugin.config.getString("lore-prefix", "Mob type:")} ${type.name.toLowerCase().capitalize()}"))
                }
            }
        }
    }

}