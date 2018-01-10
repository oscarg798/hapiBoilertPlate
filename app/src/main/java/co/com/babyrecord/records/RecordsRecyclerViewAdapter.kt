package co.com.babyrecord.records

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import co.com.babyrecord.*
import co.com.core.models.Record
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by oscarg798 on 12/20/17.
 */
class RecordsRecyclerViewAdapter(val mRecords: ArrayList<Record>,
                                 private val mCallbacks: IRecordCallbacks) : RecyclerView.Adapter<RecordViewHolder>() {


    private val mCurrentDayInLong = Calendar.getInstance().timeInMillis

    private val mSimpleTimeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    private val mSimpleDateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())

    override fun onBindViewHolder(holder: RecordViewHolder?, position: Int) {
        holder?.let {
            holder.mTVStartTime?.
                    setText(Utils.instance.
                            boldTextPrefix("Start Time: ${getTimeFromDate(mRecords[position]
                                    .startTime)}"), TextView.BufferType.SPANNABLE)

            holder.mTVEndTime?.setText(Utils.instance.
                    boldTextPrefix(if (mRecords[position].endTime !== null)
                        "End Time: ${getTimeFromDate(mRecords[position].endTime!!)}"
                    else "End Time:"), TextView.BufferType.SPANNABLE)

            holder.mTVEditEndTimeToRecord?.setOnClickListener {
                mCallbacks.editRecordEndTime(mRecords[position])
            }

            holder.mTVEditEndTimeToRecord?.visibility = if (mRecords[position].endTime !== null) View.VISIBLE
            else View.GONE

            holder.mTVDate?.
                    setText(Utils.instance.
                            boldTextPrefix("Date: ${getDate(mRecords[position].startTime)}"),
                            TextView.BufferType.SPANNABLE)

            holder.mTVType?.
                    setText(Utils.instance.
                            boldTextPrefix("Type: ${mRecords[position].type}"),
                            TextView.BufferType.SPANNABLE)

            val duration = Utils.instance.calculateRecordDuration(mRecords[position].startTime,
                    mRecords[position].endTime)

            if (duration !== null) {
                holder.mTVRecordDuration?.
                        setText(Utils.instance.
                                boldTextPrefix("Duration: $duration"),
                                TextView.BufferType.SPANNABLE)
                holder.mTVRecordDuration?.visibility = View.VISIBLE
                holder.mTVRecordDuration.visibility = if (mRecords[position].type == MEDICINE_TYPE) View.GONE else View.VISIBLE

            } else {
                holder.mTVRecordDuration?.visibility = View.GONE
            }


            if (position - 1 >= 0) {
                val sleepDuration = Utils.instance.
                        calculateRecordDuration(mRecords[position - 1].endTime, mRecords[position].startTime)
                if (sleepDuration !== null) {
                    holder.mTVTimeFromLastRecord?.
                            setText(Utils.instance.
                                    boldTextPrefix("Elapsed Time: $sleepDuration"),
                                    TextView.BufferType.SPANNABLE)
                    holder.mTVTimeFromLastRecord?.visibility = View.VISIBLE
                } else {
                    holder.mTVTimeFromLastRecord?.visibility = View.GONE
                }

            } else {
                holder.mTVTimeFromLastRecord?.visibility = View.GONE
            }

            holder.mTVSetEndTimeToRecord?.setOnClickListener {
                mCallbacks.finishRecord(mRecords[position])
            }

            holder.mTVDeleteRecord?.setOnClickListener {
                mCallbacks.deleteRecord(mRecords[position])
            }

            holder.mTVSetEndTimeToRecord?.visibility = if (Utils.instance.canStopTheRecord(mRecords[position].startTime,
                    mCurrentDayInLong)) {
                if (mRecords[position].type == MEDICINE_TYPE) View.GONE else View.VISIBLE
            } else View.GONE

            holder.mTVEndTime.visibility = if (mRecords[position].type == MEDICINE_TYPE) View.GONE else View.VISIBLE





            holder.mIVTypeIcon.setImageDrawable(ContextCompat.getDrawable(holder.mIVTypeIcon.context, when (mRecords[position].type) {
                SLEEP_TYPE -> R.drawable.ic_sleep_white
                FEED_TYPE -> R.drawable.ic_feed
                else -> R.drawable.ic_pills
            }))

            holder.mIVTypeIcon.setColorFilter(holder.mIVTypeIcon.context.resources.
                    getColor(R.color.colorPrimary))


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder =
            RecordViewHolder(LayoutInflater.from(parent.context).
                    inflate(R.layout.record_view_holder, parent, false))


    override fun getItemCount(): Int = mRecords.size

    private fun getTimeFromDate(time: Long): String {
        return mSimpleTimeFormat.format(Date(time))
    }

    private fun getDate(date: Long): String {
        return mSimpleDateFormat.format(Date(date))
    }


    fun addRecord(record: List<Record>) {
        mRecords.addAll(record)
        sort()

    }

    fun clear() {
        mRecords.clear()
        notifyDataSetChanged()
    }


    fun updateRecord(record: Record) {
        mRecords.filter { it.uuid == record.uuid }.take(1).forEach {
            mRecords.remove(it)
            mRecords.add(record)
        }

        sort()
    }

    private fun sort() {
        mRecords.sortedBy {
            it.startTime
        }
        notifyDataSetChanged()

    }

    fun removeRecord(recordUuid: String) {
        for (i in 0 until mRecords.size) {
            if (mRecords[i].uuid == recordUuid) {
                mRecords.removeAt(i)
                break
            }
        }
        sort()
    }
}