package co.com.babyrecord.records

import co.com.core.models.Record
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

/**
 * Created by oscarg798 on 12/22/17.
 */
class SleepRecordsByDate(private val mTitle: String, private val mRecords: ArrayList<Record>,
                         val mYear: Int, val mDayOfTheYear: Int) :
        ExpandableGroup<Record>(mTitle, mRecords)
