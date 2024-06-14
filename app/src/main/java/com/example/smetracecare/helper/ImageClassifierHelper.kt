package com.example.smetracecare.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import com.example.smetracecare.R
import java.lang.IllegalStateException

import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import org.tensorflow.lite.task.vision.classifier.Classifications

@Suppress("DEPRECATION")
class ImageClassifierHelper(
    var maxResultsValue: Int = 3,
    val thresholdValue: Float = 0.1f,
    val modelNameValue: String = "model3.tflite",
    val contextValue: Context,
    val classifierListenerValue: ClassifierListener?
) {
    private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        // TODO: Menyiapkan Image Classifier untuk memproses gambar.
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(thresholdValue)
            .setMaxResults(maxResultsValue)
        val baseOptionBuilder = BaseOptions.builder()
            .setNumThreads(4)
        optionsBuilder.setBaseOptions(baseOptionBuilder.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                contextValue,
                modelNameValue,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            classifierListenerValue?.onError(contextValue.getString(R.string.image_classifier_failed))
            Log.e(IMAGETAG, e.message.toString())
        }
    }

    fun classifyImage(imageUri: Uri) {
        // TODO: mengklasifikasikan imageUri dari gambar statis.
        if (imageClassifier == null) {
            setupImageClassifier()
        }

        val bitmap = getImageBitmap(imageUri)

        val tensorImage = preprocessImage(bitmap)

        val results = performInference(tensorImage)

        notifyResults(results)
    }

    private fun getImageBitmap(imageUri: Uri): Bitmap {
        return if (checkVersion()) {
            val source = ImageDecoder.createSource(contextValue.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(contextValue.contentResolver, imageUri)
        }.copy(Bitmap.Config.ARGB_8888, true)
    }

    private fun checkVersion(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }

    private fun preprocessImage(bitmap: Bitmap): TensorImage {
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(CastOp(DataType.UINT8))
            .build()

        val tensorImage = TensorImage(DataType.UINT8)
        tensorImage.load(bitmap)

        return imageProcessor.process(tensorImage)
    }

    private fun performInference(tensorImage: TensorImage): List<Classifications>? {
        var inferenceTime = SystemClock.uptimeMillis()
        val results = imageClassifier?.classify(tensorImage)
        inferenceTime = SystemClock.uptimeMillis() - inferenceTime
        classifierListenerValue?.onResults(results, inferenceTime)
        return results
    }

    private fun notifyResults(results: List<Classifications>?) {
        classifierListenerValue?.onResults(results, 0)
    }

    interface ClassifierListener {
        fun onError(errorMsg: String)
        fun onResults(
            results: List<Classifications>?,
            inferenceTime: Long
        )
    }

    companion object {
        private const val IMAGETAG = "Image_Classifier_Helper_Modified" //
    }
}
