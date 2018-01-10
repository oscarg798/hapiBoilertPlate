package co.com.babyrecord

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import co.com.babyrecord.records.widget.RecordsWidget
import co.com.core.models.Record
import java.time.Month
import java.util.*


/**
 * Created by oscarg798 on 12/21/17.
 */
class Utils private constructor() {

    private val secondsInMilli: Long = 1000
    private val minutesInMilli = secondsInMilli * 60
    private val hoursInMilli = minutesInMilli * 60
    private val dayInMilli = hoursInMilli * 24
    private val monthInMilli = 2629746000
    private val yearInMilli = monthInMilli * 12

    fun canStopTheRecord(startTime: Long, currentTime: Long): Boolean {

        return ((currentTime - startTime) / hoursInMilli) <= 24
    }

    fun calculateRecordDuration(startTime: Long?, endTime: Long?): String? {

        endTime?.let {
            startTime?.let {
                var different = endTime - startTime


                val elapsedHours = different / hoursInMilli
                different %= hoursInMilli

                val elapsedMinutes = different / minutesInMilli
                different %= minutesInMilli



                return if (elapsedHours > 0)
                    "$elapsedHours Hours and $elapsedMinutes minutes"
                else "$elapsedMinutes minutes"
            }


        }

        return null
    }

    fun getBabyAge(birthDay: Long): String {
        val currentDate = Calendar.getInstance().timeInMillis
        var different = currentDate - birthDay
        val elapsedYears = different / yearInMilli
        val elapsedMonths = (different % yearInMilli) / monthInMilli
        val elapseDays = (different % monthInMilli) / dayInMilli


        return if (elapsedYears > 0) {
            "Your baby is $elapsedYears years and $elapsedMonths monthand $elapseDays days"
        } else {
            "Your baby is $elapsedMonths month and $elapseDays days"
        }


    }

    fun boldTextPrefix(text: String): SpannableString {

        val spannable = SpannableString(text)
        spannable.setSpan(ForegroundColorSpan(Color.BLACK), 0, text.indexOf(":") + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
    }

     fun requestWidgetUpdate(context:Context) {
        val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        intent.component = ComponentName(context, RecordsWidget::class.java)
         context.sendBroadcast(intent)
    }

    private object Holder {

        val INSTANCE = Utils()
    }

    companion object {
        val instance by lazy {
            Holder.INSTANCE
        }
    }

}