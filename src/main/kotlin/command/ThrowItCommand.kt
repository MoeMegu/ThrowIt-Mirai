package pers.moe.command

import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.geometry.Positions
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.console.util.SemVersion.Companion.satisfies
import net.mamoe.mirai.contact.BotIsBeingMutedException
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.isUploaded
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.sendTo
import net.mamoe.mirai.utils.ExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import pers.moe.ThrowItMirai
import java.awt.AlphaComposite
import java.awt.RenderingHints
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL
import javax.imageio.ImageIO

@ConsoleExperimentalApi
@ExperimentalCommandDescriptors
object ThrowItCommand : SimpleCommand(
    ThrowItMirai, "丢", "throw",
    description = "把群友丢出去"
) {

    override val prefixOptional: Boolean = true // 命令可不通过添加前缀'/'使用

    @JvmStatic
    private val bgImg = ImageIO.read(ThrowItCommand::class.java.classLoader.getResource("template.png"))

    private val avaQuality = "5"

    @Handler
    @Suppress("unused")
    suspend fun CommandSender.handle(target: User) {
        subject ?: throw RuntimeException("请在聊天环境使用此命令")
        lateinit var result: ExternalResource
        lateinit var imageMessage: Image
        try {
            result = throwIt(ImageIO.read(URL(target.avatarUrl)))
            result.use { it.close() }
            imageMessage = sendResult(result, subject!!)
        } catch (e: IOException) {
            ThrowItMirai.logger.error(e)
            ThrowItMirai.logger.error("图片绘制失败")
        } catch (e: RuntimeException) {
            ThrowItMirai.logger.error(e)
            ThrowItMirai.logger.error("图片上传失败")
        }
        TODO("用户调用")
    }

    @Handler
    @Suppress("unused")
    suspend fun CommandSender.handle(image: Image) {
        lateinit var bufferedImage: BufferedImage
        lateinit var result: ExternalResource
        try {
            bufferedImage = ImageIO.read(URL(image.queryUrl()))
        } catch (e: IOException) {
            ThrowItMirai.logger.error(e)
            ThrowItMirai.logger.error("图片下载失败,请检查网络")
        }
        try {
            result = throwIt(bufferedImage)
            result.use { it.close() }
        } catch (e: IOException) {
            ThrowItMirai.logger.error(e)
            ThrowItMirai.logger.error("图片绘制失败")
        }
        sendResult(result)

    }

    @Handler
    @Suppress("unused")
    suspend fun CommandSender.handle(target: PlainText) {
        TODO("纯文本解析")

    }
    /**
     * 通过传入图片绘制ThrowIt
     *
     * @param image 传入一个图片 [BufferedImage] 作为绘制源文件
     *
     * @return [ExternalResource] 返回一个外部资源对象作为绘制结果
     *
     * @exception IOException 绘制过程中出现异常
     */
    private fun throwIt(image: BufferedImage): ExternalResource {
        // 设置图层参数
        val avaImg = BufferedImage(image.width, image.height, BufferedImage.TYPE_4BYTE_ABGR)
        // 开启抗锯齿
        val renderingHints = RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        // 绘制蒙版
        val shape = Ellipse2D.Double(.0, .0, image.width.toDouble(), image.height.toDouble())
        val avaImgGraphics = avaImg.createGraphics()
        // 绘制开始
        avaImgGraphics.setRenderingHints(renderingHints)
        avaImgGraphics.clip = shape
        avaImgGraphics.drawImage(image, 0, 0, null)
        avaImgGraphics.dispose()
        // 绘制结束
        // 旋转图片
        var proceedAva = Thumbnails.of(avaImg)
            .size(136, 136)
            .rotate(-160.0)
            .asBufferedImage()
        // 二次裁切旋转后图片
        proceedAva = Thumbnails.of(proceedAva)
            .sourceRegion(Positions.CENTER, 136, 136)
            .size(136, 136)
            .keepAspectRatio(false)
            .asBufferedImage()
        val bgImgGraphics = bgImg.createGraphics()
        bgImgGraphics.composite = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP)
        bgImgGraphics.drawImage(proceedAva, 19, 181, 137, 137, null)
        // 结束绘制图片
        bgImgGraphics.dispose()
        // 将内存中图片写入data中
        val bufferedImage = Thumbnails.of(bgImg)
            .size(512, 512)
            .asBufferedImage()
        val os: ByteArrayOutputStream = ByteArrayOutputStream()
        os.use { it.close() }
        ImageIO.write(bufferedImage, "png", os)
        val result: ExternalResource = os.toByteArray().toExternalResource()
        result.use { it.close() }
        return result
    }

    private suspend fun CommandSender.sendResult(result: ExternalResource){
        lateinit var imageMessage: Image
        var failedTry = 0
        do imageMessage = subject!!.uploadImage(result) while (
            MiraiConsole.version.satisfies("2.9.0") && !imageMessage.isUploaded(bot!!) && failedTry++ <= 3
        )
        if (failedTry == 3) {
            ThrowItMirai.logger.error(RuntimeException("Failed uploading message to $subject"))
            ThrowItMirai.logger.error("ThrowIt:图片上传失败,请检查网络连接")
        }
        try {subject!!.sendMessage(imageMessage)} catch (e: BotIsBeingMutedException){
            ThrowItMirai.logger.error(e)
            ThrowItMirai.logger.error("ThrowIt:图片发送失败,机器人被禁言")
        }
    }
}