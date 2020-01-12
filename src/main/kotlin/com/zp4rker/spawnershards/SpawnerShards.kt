package com.zp4rker.spawnershards

import com.zp4rker.bukkitutils.KotlinRuntime
import org.bukkit.plugin.java.JavaPlugin

class SpawnerShards : JavaPlugin() {

    private val kotlinVersion = "1.3.61"

    override fun onEnable() {
        KotlinRuntime.setup(this, kotlinVersion)
    }

}