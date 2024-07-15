package com.example.ml_kit

import android.content.Context
import android.graphics.Rect
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class TensorFlowLiteModel(context: Context, modelPath: String) {
    private var interpreter: Interpreter? = null

    init {
        try {
            val buffer = loadModelFile(context, modelPath)
            interpreter = Interpreter(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun loadModelFile(context: Context, modelPath: String): ByteBuffer {
        val fileDescriptor = context.assets.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
            .order(ByteOrder.nativeOrder())
    }

    fun detectObjects(inputImage: TensorImage): List<DetectedObject>? {
        // Create an image processor with all required operations.
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(300, 300, ResizeOp.ResizeMethod.BILINEAR))
            .build()

        val processedImage = imageProcessor.process(inputImage)

        val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, 10, 4), DataType.FLOAT32)
        interpreter!!.run(processedImage.buffer, outputBuffer.buffer)
        return processOutput(outputBuffer)
    }

    private fun processOutput(outputBuffer: TensorBuffer): List<DetectedObject>? {
        val results = mutableListOf<DetectedObject>()
        val detections = outputBuffer.floatArray

        for (i in detections.indices step 4) {
            val x = detections[i]
            val y = detections[i + 1]
            val width = detections[i + 2]
            val height = detections[i + 3]

            if (width > 0 && height > 0) {
                val boundingBox = Rect(x.toInt(), y.toInt(), (x + width).toInt(), (y + height).toInt())
                results.add(DetectedObject(boundingBox))
            }
        }

        return results
    }

    data class DetectedObject(val boundingBox: Rect)
}
