package pers.moe;

import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import pers.moe.command.CleanCommand;
import pers.moe.command.ThrowItCommand;

public final class ThrowItMirai extends JavaPlugin {
    public static final ThrowItMirai INSTANCE = new ThrowItMirai();

    private ThrowItMirai() {
        super(new JvmPluginDescriptionBuilder("pers.moe.ThrowIt-mirai", "1.0-SNAPSHOT")
                .name("ThrowItMirai")
                .author("Moe")
                .build());
    }

    @Override
    public void onEnable() {
        CommandManager.INSTANCE.registerCommand(ThrowItCommand.INSTANCE, false);
        CommandManager.INSTANCE.registerCommand(CleanCommand.INSTANCE, false);
        getLogger().info("ThrowItMirai已加载!");
    }
}