package com.corphish.callstatscruncher

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var recentTopContactName = ""
    private var recentTopContactDuration = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = StatsAdapter(this, crunchStat())
        recyclerView.adapter?.notifyDataSetChanged()
        //crunchStat()
        topContactDuration.text = TimeUtils.toDescription(recentTopContactDuration)
        topContactName.text = recentTopContactName
    }

    private fun crunchStat(): List<CallStat> {
        val callStatMap = HashMap<String, Long>()
        val recentMap = HashMap<String, Long>()
        val allCalls = Uri.parse("content://call_log/calls")
        val c = contentResolver.query(allCalls, null, null, null, null)
        val curTime = System.currentTimeMillis()

        if (c != null) {
            c.moveToFirst();
            while (c.moveToNext()) {
                val num = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER))// for  number
                var name = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME))// for name
                val duration = c.getString(c.getColumnIndex(CallLog.Calls.DURATION)).toLong()// for duration
                val date = c.getString(c.getColumnIndex(CallLog.Calls.DATE)).toLong()
                //val type = Integer.parseInt(c.getString(c.getColumnIndex(CallLog.Calls.TYPE)))

                if (name == null) {
                    name = if (num == null) "Unknown" else num
                }
                val prevDuration = callStatMap[name]
                var newDuration: Long

                if (prevDuration == null) newDuration = duration
                else newDuration = prevDuration + duration

                callStatMap[name] = newDuration

                if (curTime - date <= 24 * 60 * 60 * 1000) {
                    val prevRDuration = recentMap[name]
                    var newRDuration: Long

                    if (prevRDuration == null) newRDuration = duration
                    else newRDuration = prevRDuration + duration

                    recentMap[name] = newRDuration
                }
            }
            c.close()
        }
        Log.d("CSC_Main", "$callStatMap")

        val list = ArrayList<CallStat>()

        for ((k, v) in callStatMap.entries) list += CallStat(k, v)

        list.sortBy { -it.duration }

        for ((k, v) in recentMap.entries) {
            if (v > recentTopContactDuration) {
                recentTopContactDuration = v
                recentTopContactName = k
            }
        }

        return list
    }
}
