package com.zp4rker.spawnershards

import com.joeyoey.spacestacker.SpaceStacker
import com.zp4rker.bukkitutils.KotlinRuntime
import com.zp4rker.spawnershards.listeners.BlockListener
import com.zp4rker.spawnershards.listeners.CraftingListener
import com.zp4rker.spawnershards.listeners.MobListener
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class SpawnerShards : JavaPlugin() {

    companion object {
        lateinit var config: Config
    }

    private val kotlinVersion = "1.3.61"

    override fun onEnable() {
        KotlinRuntime.setup(this, kotlinVersion)

        SpawnerShards.config = Config(this)

        arrayOf(BlockListener(this), CraftingListener(), MobListener()).forEach { server.pluginManager.registerEvents(it, this) }

        saveDefaultConfig()
    }

    class Config(private val plugin: JavaPlugin) {
        fun isCustomShard(shard: ItemStack): Boolean {
            if (shard.type != Material.matchMaterial(plugin.config.getString("shard-item.type", "PRISMARINE_SHARD"))) return false
            if (!shard.hasItemMeta() || !shard.itemMeta.hasLore()) return false
            return shard.itemMeta.lore[0].contains(ChatColor.translateAlternateColorCodes('&', plugin.config.getString("lore-prefix", "Mob type:")))
        }

        fun getShard(type: EntityType): ItemStack {
            return ItemStack(Material.matchMaterial(plugin.config.getString("shard-item.type", "PRISMARINE_SHARD"))).apply {
                itemMeta = itemMeta.apply {
                    displayName = ChatColor.translateAlternateColorCodes('&', plugin.config.getString("shard-item.name", "Spawner Shard"))
                    lore = listOf(ChatColor.translateAlternateColorCodes('&', "${plugin.config.getString("lore-prefix", "Mob type:")} ${type.name.toLowerCase().capitalize()}"))
                }
            }
        }

        /*fun getSpawner(type: EntityType): ItemStack {
            return ItemStack(Material.MOB_SPAWNER).apply {
                itemMeta = itemMeta.apply {
                    lore = listOf(ChatColor.translateAlternateColorCodes('&', "${plugin.config.getString("lore-prefix", "Mob type:")} ${type.name.toLowerCase().capitalize()}"))
                }
            }
        }*/

        fun getSpawner(type: EntityType): ItemStack {
            val spaceStacker = plugin.server.pluginManager.getPlugin("SpaceStacker") as SpaceStacker

            return ItemStack(Material.MOB_SPAWNER).apply {
                itemMeta = itemMeta.apply {
                    val name = spaceStacker.config.getString("formats.tier-spawner.name")
                    val lore = spaceStacker.config.getStringList("formats.tier-spawner.lore")
                    val entityName = type.name.split("_").joinToString("") { it.toLowerCase().capitalize() }

                    displayName = ChatColor.translateAlternateColorCodes('&', name.replace("%entity%", entityName))
                    this.lore = lore.map {
                        ChatColor.translateAlternateColorCodes('&', with(it) {
                            replace("%upgrade%", "DEFAULT")
                            replace("%amount%", "1")
                            replace("%entity%", entityName)
                        })
                    }
                }
            }
        }
    }

}