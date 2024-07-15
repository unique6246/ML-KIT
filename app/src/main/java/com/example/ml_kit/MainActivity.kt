package com.example.ml_kit

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import org.tensorflow.lite.support.image.TensorImage
import java.util.concurrent.ExecutionException

class MainActivity : AppCompatActivity() {

    private lateinit var objectOverlayView: ObjectOverlayView
    private lateinit var tensorFlowLiteModel: TensorFlowLiteModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        objectOverlayView = findViewById(R.id.objectOverlayView)
        tensorFlowLiteModel = TensorFlowLiteModel(this, "file:///android_asset/sample_mouse2.tflite")

        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build()
                preview.setSurfaceProvider(findViewById<PreviewView>(R.id.previewView).surfaceProvider)

                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()

                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(android.util.Size(1280, 720))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
                    @SuppressLint("UnsafeOptInUsageError")
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                        val tensorImage = TensorImage.fromBitmap(image.bitmapInternal)
                        val detectedObjects = tensorFlowLiteModel.detectObjects(tensorImage)
                        val boundingBoxes = detectedObjects?.map { it.boundingBox } ?: emptyList()
                        objectOverlayView.setBoundingBoxes(boundingBoxes)
                        imageProxy.close()
                    }
                }

                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }
}
