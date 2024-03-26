package io.github.xiaomengjie.bitmaptool

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.FileDescriptor
import java.io.InputStream
import kotlin.math.round

/**
 * 获取缩放后的本地图片
 * @param filePath 图片路径
 * @param width 宽度
 * @param height 高度
 */
fun readBitmapFromFile(filePath: String, width: Int, height: Int): Bitmap?{
    return readBitmap(filePath, FilePathBitmapDecode(), width, height)
}

/**
 * 获取缩放后的本地图片
 * @param fileDescriptor 文件描述符
 * @param width 宽度
 * @param height 高度
 */
fun readBitmapFromFileDescriptor(fileDescriptor: FileDescriptor, width: Int, height: Int): Bitmap?{
    return readBitmap(fileDescriptor, FileDescriptorBitmapDecode(), width, height)
}

/**
 * 获取缩放后的本地图片
 * @param inputStream 输入流
 * @param width 宽度
 * @param height 高度
 */
fun readBitmapFromInputStream(inputStream: InputStream, width: Int, height: Int): Bitmap? {
    return readBitmap(inputStream, InputStreamBitmapDecode(), width, height)
}

fun readBitmapFromResources(resources: Resources, resourcesId: Int, width: Int, height: Int): Bitmap? {
    return readBitmap(resources, ResourcesBitmapDecode(resourcesId), width, height)
}

fun readBitmapFromResourcesOptimized(resources: Resources, resourcesId: Int, width: Int, height: Int): Bitmap? {
    val inputStream = resources.openRawResource(resourcesId)
    return readBitmap(inputStream, InputStreamBitmapDecode(), width, height)
}

fun readBitmapFromAssets(context: Context, filePath: String, width: Int, height: Int): Bitmap? {
    val inputStream = context.assets.open(filePath)
    return readBitmap(inputStream, InputStreamBitmapDecode(), width, height)
}

fun readBitmapFromByteArray(data: ByteArray, width: Int, height: Int): Bitmap? {
    return readBitmap(data, ByteArrayBitmapDecode(), width, height)
}

private fun <T> readBitmap(source: T, decode: BitmapDecode<T>, width: Int, height: Int): Bitmap?{
    return try {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        decode.decode(source, options)
        val srcWidth = options.outWidth.toFloat()
        val srcHeight = options.outHeight.toFloat()
        var inSampleSize = 1
        if (srcHeight > height || srcWidth > width){
            inSampleSize = if (srcWidth > srcHeight){
                round(srcHeight / height).toInt()
            }else{
                round(srcWidth / width).toInt()
            }
        }
        options.inJustDecodeBounds = false
        options.inSampleSize = inSampleSize
        decode.decode(source, options)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}