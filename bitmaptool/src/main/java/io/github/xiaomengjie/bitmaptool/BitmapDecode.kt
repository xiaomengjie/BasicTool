package io.github.xiaomengjie.bitmaptool

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import androidx.annotation.DrawableRes
import java.io.FileDescriptor
import java.io.InputStream

interface BitmapDecode<T> {
    fun decode(source: T, options: Options): Bitmap?
}

class FilePathBitmapDecode: BitmapDecode<String>{
    override fun decode(source: String, options: Options): Bitmap? {
        return BitmapFactory.decodeFile(source, options)
    }
}

class FileDescriptorBitmapDecode: BitmapDecode<FileDescriptor>{
    override fun decode(source: FileDescriptor, options: Options): Bitmap? {
        return BitmapFactory.decodeFileDescriptor(source, null, options)
    }
}

class InputStreamBitmapDecode: BitmapDecode<InputStream>{
    override fun decode(source: InputStream, options: Options): Bitmap? {
        return BitmapFactory.decodeStream(source, null, options)
    }
}

class ResourcesBitmapDecode(@DrawableRes private val resourcesId: Int): BitmapDecode<Resources>{
    override fun decode(source: Resources, options: Options): Bitmap? {
        /**
         * BitmapFactory.decodeResource 加载的图片可能会经过缩放，该缩放目前是放在 java 层做的，效率
         * 比较低，而且需要消耗 java 层的内存。因此，如果大量使用该接口加载图片，容易导致OOM错误
         * BitmapFactory.decodeStream 不会对所加载的图片进行缩放，相比之下占用内存少，效率更高
         */
        return BitmapFactory.decodeResource(source, resourcesId, options)
    }
}

class ByteArrayBitmapDecode: BitmapDecode<ByteArray>{
    override fun decode(source: ByteArray, options: Options): Bitmap? {
        return BitmapFactory.decodeByteArray(source, 0, source.size, options)
    }
}

