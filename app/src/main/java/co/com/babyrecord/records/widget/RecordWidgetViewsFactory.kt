package co.com.babyrecord.records.widget

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import co.com.babyrecord.*
import co.com.babyrecord.dashboard.DashboardActivity
import co.com.core.models.Record
import co.com.core.use_cases.record.GetRecordsUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by oscarg798 on 1/6/18.
 */
class RecordWidgetViewsFactory(val mContext: Context,
                               val mIntent: Intent?) : RemoteViewsService.RemoteViewsFactory {

    private val mRecords = ArrayList<Record>()

    private val mSimpleTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    private val mSimpleDateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())

    override fun onCreate() {
        getRecords()

    }

    private fun getRecords() {
        GetRecordsUseCase(Schedulers.trampoline(),
                Schedulers.trampoline())
                .execute(Calendar.getInstance().timeInMillis,
                        object : DisposableSingleObserver<List<Record>>() {
                            override fun onSuccess(t: List<Record>) {
                                mRecords.addAll(t)
                                mRecords.sortedByDescending { it.startTime }
                                mRecords.reverse()
                                this.dispose()
                            }

                            override fun onError(e: Throwable) {
                                e.printStackTrace()
                                this.dispose()
                            }
                        })
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onDataSetChanged() {
        mRecords.clear()
        getRecords()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getViewAt(position: Int): RemoteViews {
        val remoteView = RemoteViews(mContext.packageName, R.layout.records_widget_view_holder)

        remoteView.setTextViewText(R.id.mTVDate, "Date: ${getDate(mRecords[position].startTime)}")

        remoteView.setTextViewText(R.id.mTVStartTime, Utils.instance.
                boldTextPrefix("Start Time: ${getTimeFromDate(mRecords[position]
                        .startTime)}"))

        remoteView.setTextViewText(R.id.mTVEndTime, Utils.instance.
                boldTextPrefix(if (mRecords[position].endTime !== null)
                    "End Time: ${getTimeFromDate(mRecords[position].endTime!!)}"
                else "End Time:"))

        remoteView.setTextViewText(R.id.mTVType, Utils.instance.
                boldTextPrefix("Type: ${mRecords[position].type}"))

        val duration = Utils.instance.calculateRecordDuration(mRecords[position].startTime,
                mRecords[position].endTime)

        if (duration !== null) {
            remoteView.setTextViewText(R.id.mTVType, Utils.instance.
                    boldTextPrefix("Duration: $duration"))
            remoteView.setViewVisibility(R.id.mTVType, View.VISIBLE)
        } else {
            remoteView.setViewVisibility(R.id.mTVType, View.GONE)
        }

        remoteView.setImageViewBitmap(R.id.mIVTypeIcon, convertToBitmap(ContextCompat.getDrawable(mContext, when (mRecords[position].type) {
            SLEEP_TYPE -> R.drawable.ic_sleep_white
            FEED_TYPE -> R.drawable.ic_feed
            else -> R.drawable.ic_pills
        })!!, 30, 30))



        remoteView.setOnClickFillInIntent(R.id.mTVDeleteRecord,
                getFillIntentForView(mRecords[position], R.id.mTVDeleteRecord))
        remoteView.setOnClickFillInIntent(R.id.mTVSetEndTimeToRecord,
                getFillIntentForView(mRecords[position], R.id.mTVSetEndTimeToRecord))



        return remoteView
    }

    private fun getFillIntentForView(record: Record, viewId: Int): Intent {
        val intent = Intent(mContext, DashboardActivity::class.java)
        intent.putExtra(RECORD, record)
        intent.putExtra(VIEW_ID, viewId)
        return intent
    }

    fun convertToBitmap(drawable: Drawable, widthPixels: Int, heightPixels: Int): Bitmap {
        drawable.setColorFilter(mContext.resources.
                getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY)
        val mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mutableBitmap)
        drawable.setBounds(0, 0, widthPixels, heightPixels)
        drawable.draw(canvas)

        return mutableBitmap
    }

    override fun getCount(): Int {
        return mRecords.size
    }

    private fun getDate(date: Long): String {
        return mSimpleDateFormat.format(Date(date))
    }

    private fun getTimeFromDate(time: Long): String {
        return mSimpleTimeFormat.format(Date(time))
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {
    }
}