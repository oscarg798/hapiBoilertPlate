package co.com.babyrecord.size_record

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import co.com.babyrecord.R
import co.com.babyrecord.Utils
import co.com.core.models.SizeRecord
import io.reactivex.subjects.PublishSubject
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by oscarg798 on 12/22/17.
 */
class SizeRecordAdapter(private val mSizeRecords: ArrayList<SizeRecord>,
                        private val mISizeRecordCallbacks: ISizeRecordCallbacks) : RecyclerView.Adapter<SizeRecordViewHolder>() {


    private val mSimpleDateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())

    val mSizeObservable = PublishSubject.create<Int>()


    override fun onBindViewHolder(holder: SizeRecordViewHolder?, position: Int) {
        holder?.let {
            holder.mTVDate?.text = Utils.instance.
                    boldTextPrefix("Date: ${mSimpleDateFormat.format(Date(mSizeRecords[position].date))}")
            holder.mTVHeight?.text = Utils.instance.
                    boldTextPrefix("Height: ${mSizeRecords[position].height} cm")
            holder.mTVWeight?.text = Utils.instance.
                    boldTextPrefix("Weight: ${mSizeRecords[position].weight} pounds")
            holder.mTVDeleteRecord?.setOnClickListener {
                mISizeRecordCallbacks.deleteSizeRecord(mSizeRecords[position])
            }
        }
    }

    fun addSizeRecords(sizeRecords: List<SizeRecord>) {
        mSizeRecords.addAll(sizeRecords)
        mSizeRecords.sortedByDescending { it.date }
        notifyDataSetChanged()
    }

    fun deleteRecord(sizeRecordUuid: String) {
        mSizeRecords.filter {
            it.uuid == sizeRecordUuid
        }.forEach {
            mSizeRecords.remove(it)
        }

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        mSizeObservable.onNext(mSizeRecords.size)
        return mSizeRecords.size

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeRecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.size_record_view_holder,
                parent, false)
        return SizeRecordViewHolder(view)
    }
}