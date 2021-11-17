package pers.moe.command;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JSimpleCommand;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;
import pers.moe.ThrowItMirai;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ThrowItCommand extends JSimpleCommand {
    public static final ThrowItCommand INSTANCE = new ThrowItCommand();

    private ThrowItCommand() {
        super(ThrowItMirai.INSTANCE, "丢", "throw");
        this.setDescription("把别人丢出去");
    }

    @Handler
    public void handle(CommandSender sender, User target) {
        try {
            // 从AvatarURL读入头像图片
            URL avaUrl = new URL(target.getAvatarUrl());
            BufferedImage image = ImageIO.read(avaUrl);
            BufferedImage avaImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            // 开启抗锯齿
            RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // 绘制蒙版
            Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, image.getWidth(), image.getHeight());
            Graphics2D avaImgGraphics = avaImg.createGraphics();
            //开始绘制
            avaImgGraphics.setRenderingHints(renderingHints);
            avaImgGraphics.setClip(shape);
            avaImgGraphics.drawImage(image, 0, 0, null);
            //结束绘制
            avaImgGraphics.dispose();
            // 旋转图片
            BufferedImage proceedImg = Thumbnails.of(avaImg)
                    .size(136, 136)
                    .rotate(-160)
                    .asBufferedImage();
            // 二次裁切旋转后图片
            proceedImg = Thumbnails.of(proceedImg)
                    .sourceRegion(Positions.CENTER, 136, 136)
                    .size(136, 136)
                    .keepAspectRatio(false)
                    .asBufferedImage();
            // 读取背景图并绘制图片
            BufferedImage bgImg = ImageIO.read(ThrowItCommand.class.getClassLoader().getResource("bg.png"));
            Graphics2D bgImgGraphics = bgImg.createGraphics();
            bgImgGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP));
            bgImgGraphics.drawImage(proceedImg, 19, 181, 137, 137, null);
            // 结束绘制图片
            bgImgGraphics.dispose();
            // 将内存中图片写入data中
            File dataPath = new File("data/ThrowIt/");
            if (!dataPath.exists()) dataPath.mkdirs();
            Thumbnails.of(bgImg)
                    .size(512, 512)
                    .toFile("data/ThrowIt/" + target.getId() + ".png");
        } catch (IOException e) {
            e.printStackTrace();
            sender.sendMessage("ThrowIt绘制失败");
        }
        File throwPic = new File("data/ThrowIt/" + target.getId() + ".png");
        Image image = ExternalResource.uploadAsImage(throwPic, Objects.requireNonNull(sender.getSubject()));
        sender.sendMessage(image);
    }


}