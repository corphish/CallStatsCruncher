package com.corphish.callstatscruncher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StatsAdapter(var ctx: Context, var callStatList: List<CallStat>): RecyclerView.Adapter<StatsAdapter.StatViewHolder>() {
    inner class StatViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val name: TextView = v.findViewById(R.id.name)
        val duration: TextView = v.findViewById(R.id.duration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        StatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.call_stat_iten, parent, false))

    override fun getItemCount() = callStatList.size

    override fun onBindViewHolder(holder: StatViewHolder, position: Int) {
        holder.name.text = callStatList[position].name
        holder.duration.text = TimeUtils.toDescription(callStatList[position].duration)
    }
}