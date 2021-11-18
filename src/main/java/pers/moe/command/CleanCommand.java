package pers.moe.command;

import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JSimpleCommand;
import pers.moe.ThrowItMirai;

import java.io.File;

public class CleanCommand extends JSimpleCommand {
    public static final CleanCommand INSTANCE = new CleanCommand();

    private CleanCommand() {
        super(ThrowItMirai.INSTANCE, "clean-throw");
        this.setDescription("清理数据目录");
    }

    @Handler
    public void handle(CommandSender sender) {
        File dataPath = new File("data/ThrowIt/");
        if (dataPath.exists() || dataPath.listFiles().length > 0) {
            for (File files : dataPath.listFiles()) {
                files.delete();
            }
            sender.sendMessage("已清理数据目录内文件");
        } else {
            sender.sendMessage("未读取到文件");
        }

    }
}
