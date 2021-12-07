package pers.moe

import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.console.util.SemVersion.Companion.satisfies
import net.mamoe.mirai.utils.info
import pers.moe.command.ThrowItCommand

object ThrowItMirai : KotlinPlugin(
    JvmPluginDescription(
        id = "pers.moe.ThrowIt-Mirai",
        name = "ThrowIt-Mirai",
        version = "1.2",
    ) {
        author("Moe")
        info("Throw your friends out!")
    }
) {
    @ExperimentalCommandDescriptors
    @ConsoleExperimentalApi
    override fun onEnable() {
        CommandManager.registerCommand(ThrowItCommand)
        if (!MiraiConsole.version.satisfies(">=2.9.0-M1")) logger.warning("\n*****ThrowIt-Mirai*****" +
                "\n您的Mirai-Console版本低于 2.9.0-M1 图片上传检查功能将不会工作" +
                "\n***********************")
        logger.info { "Plugin loaded" }
    }
}