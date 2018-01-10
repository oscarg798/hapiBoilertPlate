package co.com.babyrecord.records.widget

import android.content.Intent
import android.widget.RemoteViewsService

/**
 * Created by oscarg798 on 1/6/18.
 */
class WidgetSchedulerService:RemoteViewsService(){

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return RecordWidgetViewsFactory(applicationContext, intent)
    }
}