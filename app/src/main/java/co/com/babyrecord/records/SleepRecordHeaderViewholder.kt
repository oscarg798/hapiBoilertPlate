package co.com.babyrecord.records

import android.view.View
import android.widget.TextView
import co.com.babyrecord.R
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

/**
 * Created by oscarg798 on 12/22/17.
 */
class SleepRecordHeaderViewholder(mItemView: View) : GroupViewHolder(mItemView) {

    val mTVTitle = mItemView.findViewById<TextView>(R.id.mTVTitle)
}