package pers.moe.command

import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.geometry.Positions
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import pers.moe.ThrowItMirai
import java.awt.AlphaComposite
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage
import java.io.*
import java.net.URL
import javax.imageio.ImageIO

object ThrowItCommand : SimpleCommand(
    ThrowItMirai, "丢", "throw",
    description = "把群友丢出去"
) {
    @Handler
    @Suppress("unused")
    suspend fun CommandSender.handle(target: User) {
        if(user==null){
            throw RuntimeException("请在聊天环境使用此命令")
        }
        lateinit var result : ExternalResource
        lateinit var os : ByteArrayOutputStream
        try {
            // 从AvatarURL读入头像图片
            val image: BufferedImage = ImageIO.read(URL(target.avatarUrl))
            // 设置图层参数
            val avaImg = BufferedImage(image.width, image.height, BufferedImage.TYPE_4BYTE_ABGR)
            // 开启抗锯齿
            val renderingHints = RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            // 绘制蒙版
            val shape: Ellipse2D = Ellipse2D.Double(.0, .0, image.width.toDouble(), image.height.toDouble())
            val avaImgGraphics: Graphics2D = avaImg.createGraphics()
            // 绘制开始
            avaImgGraphics.setRenderingHints(renderingHints)
            avaImgGraphics.clip = shape
            avaImgGraphics.drawImage(image, 0, 0, null)
            avaImgGraphics.dispose()
            // 绘制结束
            // 旋转图片
            var proceedAva: BufferedImage = Thumbnails.of(avaImg)
                .size(136, 136)
                .rotate(-160.0)
                .asBufferedImage()
            // 二次裁切旋转后图片
            proceedAva = Thumbnails.of(proceedAva)
                .sourceRegion(Positions.CENTER, 136, 136)
                .size(136, 136)
                .keepAspectRatio(false)
                .asBufferedImage()
            // 读取背景图并绘制图片
            val bgImg: BufferedImage = ImageIO.read(ThrowItCommand::class.java.classLoader.getResource("template.png"))
            val bgImgGraphics: Graphics2D = bgImg.createGraphics()
            bgImgGraphics.composite = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP)
            bgImgGraphics.drawImage(proceedAva, 19, 181, 137, 137, null)
            // 结束绘制图片
            bgImgGraphics.dispose()
            // 将内存中图片写入data中
            val bufferedImage = Thumbnails.of(bgImg)
                .size(512, 512)
                .asBufferedImage()
            os = ByteArrayOutputStream()
            os.use { it.close() }
            ImageIO.write(bufferedImage, "png", os)
            result = os.toByteArray().toExternalResource()
            result.use { it.close() }
        } catch (e: IOException) {
            ThrowItMirai.logger.error(e)
            sendMessage("Throw-It图片绘制失败")
        }
        val uploadAsImage = result.uploadAsImage(subject!!)
        subject!!.sendMessage(uploadAsImage)
    }
}