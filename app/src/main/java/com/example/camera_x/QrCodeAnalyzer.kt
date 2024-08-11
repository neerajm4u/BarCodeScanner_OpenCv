package com.example.camera_x

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.lifecycle.MutableLiveData
import com.example.camera_x.util.toBitmap
import com.example.camera_x.viewmodel.QrViewModel
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.objdetect.QRCodeDetector


class QrCodeAnalyzer(private val qr: QRCodeDetector, private val h: Int, private val w: Int , private val qrViewModel: QrViewModel) : ImageAnalysis.Analyzer {

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {

        //mat to store camera frame, points to store detected coordinates
        val mat = Mat()
        val points = Mat()

        var bmp = image.toBitmap()// toBitmap(image.image!!)
        var matrix = Matrix();
        matrix.postRotate(90F);
        bmp =
            bmp?.let {
                Bitmap.createBitmap(it, 0, 0, bmp!!.getWidth(), bmp!!.getHeight(), matrix, true)
            }
        //imgBm.postValue(bmp)
        if (bmp != null) {
            // Log.d("Neeraj" , "Scale w   ${bmp.width } $w")
            // Log.d("Neeraj" , "Scale h  ${bmp.height } $h")
        }
        Utils.bitmapToMat(bmp, mat)
        try {

            var decodedString = qr.detectAndDecode(mat, points)
            if (!points.empty() && !decodedString.isNullOrEmpty()) {
                Log.d("Neeraj", decodedString)
                qrViewModel.decodedData.postValue(decodedString)
                points.convertTo(points, CvType.CV_64FC3);

                //init scale for detected coordinates
                val scaleX = bmp?.width?.let { w.div(it.toFloat()) }
                val scaleY = bmp?.height?.let { h.div(it.toFloat()) }
                qrViewModel.scale.clear()
                if (scaleX != null && scaleY != null) {
                    qrViewModel.scale.add(scaleX.toFloat())
                    qrViewModel.scale.add(scaleY.toFloat())
                }
                qrViewModel.polygonCoordinates.postValue(points)

            } else {
                qrViewModel.scale.clear()
                qrViewModel.polygonCoordinates.postValue(points)
                qrViewModel.decodedData.postValue("")
            }
        } catch (e: Exception) {

        }
        bmp?.recycle()
        image.close()
    }
}