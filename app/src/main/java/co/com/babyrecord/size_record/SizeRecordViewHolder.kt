package co.com.babyrecord.size_record

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import co.com.babyrecord.R

/**
 * Created by oscarg798 on 12/22/17.
 */
class SizeRecordViewHolder(mItemView: View) : RecyclerView.ViewHolder(mItemView) {

    val mTVHeight = mItemView.findViewById<TextView>(R.id.mTVHeight)
    val mTVWeight = mItemView.findViewById<TextView>(R.id.mTVWeight)
    val mTVDate = mItemView.findViewById<TextView>(R.id.mTVDate)
    val mTVDeleteRecord = mItemView.findViewById<TextView>(R.id.mTVDeleteRecord)
}