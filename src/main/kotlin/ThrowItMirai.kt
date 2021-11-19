package pers.moe

import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info
import pers.moe.command.ThrowItCommand

object ThrowItMirai : KotlinPlugin(
    JvmPluginDescription(
        id = "pers.moe.ThrowIt-Mirai",
        name = "ThrowIt-Mirai",
        version = "1.1",
    ) {
        author("Moe")
        info("Throw your friends out!")
    }
) {
    override fun onEnable() {
        CommandManager.registerCommand(ThrowItCommand)
        logger.info { "Plugin loaded" }
    }
}