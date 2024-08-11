package com.example.camera_x.viewmodel

import android.graphics.Bitmap
import android.graphics.RectF
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.opencv.core.Mat

class QrViewModel : ViewModel() {

    var rectf = MutableLiveData<List<RectF>>()
    var imgBm = MutableLiveData<Bitmap>()
    var scale = mutableListOf<Float>()
    var polygonCoordinates = MutableLiveData<Mat>()
    var decodedData = MutableLiveData<String>()

}