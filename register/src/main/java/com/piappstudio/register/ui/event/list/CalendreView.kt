/*
 * **
 * Pi App Studio. All rights reserved.Copyright (c) 2022.
 *
 */

package com.piappstudio.register.ui.event.list


import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.piappstudio.pimodel.Constant
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomCalenderView(selectedDate: Date, onClick:(date:String)->Unit) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        factory = { context ->
            DatePicker(context)
        },
        update = { view ->

            val calender = Calendar.getInstance()
            calender.time = selectedDate

            val year = calender.get(Calendar.YEAR)
            val month = calender.get(Calendar.MONTH)
            val day = calender.get(Calendar.DAY_OF_MONTH)
            view.updateDate(year,month,day)

            //view.date = selectedDate
            view.maxDate = Date().time

            view.setOnDateChangedListener { datePicker, year, month, day ->
                val calender = Calendar.getInstance()
                calender.set(Calendar.YEAR, year)
                calender.set(Calendar.MONTH, month)
                calender.set(Calendar.DAY_OF_MONTH, day)
                val month_name: String = Constant.PiFormat.month.format(calender.time)
                onClick.invoke("$day-$month_name-$year")
            }

            /*view.setOnDateChangeListener { _, year, month, dayOfMonth ->
                val calender = Calendar.getInstance()
                calender.set(Calendar.YEAR, year)
                calender.set(Calendar.MONTH, month)
                calender.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            }*/
        }
    )
}