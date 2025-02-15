package com.example.aos_ar_evacuation_beacon.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import android.view.View
import com.example.aos_ar_evacuation_beacon.constant.CellType
import com.example.aos_ar_evacuation_beacon.repository.LocationRepository

class Paint2FView : View {
   private lateinit var locationRepository: LocationRepository

   constructor(context: Context) : super(context) {
      init()
   }

   private fun init() {
      locationRepository = LocationRepository.instance
   }

   override fun onDraw(canvas: Canvas?) {
      super.onDraw(canvas)

      if (canvas != null) {
         locationRepository.secondPath.value?.let { drawCell(it, canvas, CellType.PathList) }
         locationRepository.secondFireCell.value?.let { drawCell(it, canvas, CellType.FireCell) }
         locationRepository.secondPredictedCell.value?.let { drawCell(it, canvas, CellType.PredictedCell) }
         locationRepository.secondCongestionCell.value?.let { drawCell(it, canvas, CellType.CongestionCell) }
      }
   }

   private fun drawCell(pathList: List<String>, canvas: Canvas, cellType: CellType) {
      val paint = Paint().apply {
         isAntiAlias = true
         style = Paint.Style.FILL
         strokeWidth = 3f
      }

      when (cellType) {
         CellType.PathList -> paint.color = Color.GREEN
         CellType.FireCell -> paint.color = Color.RED
         CellType.PredictedCell -> paint.color = Color.rgb(249, 162, 0)
         else -> paint.color = Color.YELLOW
      }
      paint.alpha = 50

      pathList?.forEach {
         val path = Path()

         val coordinateList = locationRepository.newMapDict2f[it]
         if (coordinateList != null) {
            path.moveTo(coordinateList[0].first * 30, coordinateList[0].second * 30)
         }

         coordinateList?.forEach { value ->
            path.lineTo(value.first * 30, value.second * 30)
            Log.i("aaaLocation: ", "${(value.first * 30)}, ${(value.second * 30)}")
         }

         if (coordinateList != null) {
            path.lineTo(coordinateList[0].first * 30, coordinateList[0].second * 30)
         }

         canvas.drawPath(path, paint)
      }
   }

   private fun drawGrid(canvas: Canvas, paint: Paint) {
      var x = 0
      var y = 0
      val offset = 30

      // 격자 그리기
      if (canvas != null) {
         while (y < canvas.height) {
            canvas.drawLine(0F, y.toFloat(), canvas.width.toFloat(), y.toFloat(), paint)
            y += offset
         }

         while (x < canvas.width) {
            canvas.drawLine(x.toFloat(), 0F, x.toFloat(), canvas.height.toFloat(), paint)
            x += offset
         }
      }
   }
}