package com.example.dailykitessentials.adapters

import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.dailykitessentials.R
import com.example.dailykitessentials.helpers.DatabaseHelper
import com.example.dailykitessentials.helpers.EMPTY
import com.example.dailykitessentials.models.Alarm
import kotlin.collections.ArrayList
import android.content.Intent
import android.graphics.Color
import android.widget.*
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import com.example.dailykitessentials.controllers.AlarmDetailsActivity
import com.example.dailykitessentials.controllers.MainActivity
import com.example.dailykitessentials.helpers.Utilities
import java.util.*


class AlarmAdapter : RecyclerView.Adapter<AlarmAdapter.AlarmRow>() {
    private var alarms : ArrayList<Alarm> = ArrayList()
    private var listType : String = String.EMPTY
    private var itemPosition : Int = 0
    private var _dbHelper : DatabaseHelper? = null

    class AlarmRow(view : View) : RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener {
        var setTimeText : TextView? = null
        var quickAlarmIcon : ImageView? = null
        var quickAlarmText : TextView? = null
        var repeatDaysText : TextView? = null
        var challengeIcon : ImageView? = null
        var remainTimeText : TextView? = null
        var alarmSwitch : Switch? = null
        var invisible : TextView? = null

        init {
            setTimeText = view.findViewById(R.id.alarm_set_time)
            quickAlarmIcon = view.findViewById(R.id.quick_alarm_icon)
            quickAlarmText = view.findViewById(R.id.quick_alarm_text)
            repeatDaysText = view.findViewById(R.id.alarm_repeat_text)
            challengeIcon = view.findViewById(R.id.challenge_icon)
            remainTimeText = view.findViewById(R.id.remain_time_text)
            alarmSwitch = view.findViewById(R.id.alarm_switch)
            invisible = view.findViewById(R.id.invisible_layer)

            view.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(menu: ContextMenu?, view: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            val contextMenuItems = view!!.resources.getStringArray(R.array.context_menu)

            for (i in contextMenuItems.indices)
                menu!!.add(Menu.NONE, i, i, contextMenuItems[i])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmRow {
        val rowBlankLayout = LayoutInflater.from(parent.context)
                                           .inflate(R.layout.alarms_row, parent, false)
        _dbHelper = DatabaseHelper(parent.context)

        return AlarmRow(rowBlankLayout)
    }

    override fun getItemCount(): Int {
        return alarms.size
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: AlarmRow, position: Int) {
        val ut = Utilities()
        //val orderedAlarm = ut.reorderAlarms(alarms)
        val alarm = alarms[position]

        holder.setTimeText!!.text = ut.getTime(alarm.SetTime)

        holder.quickAlarmIcon!!.isVisible = alarm.IsTemporary
        holder.quickAlarmText!!.isVisible = alarm.IsTemporary
        holder.challengeIcon!!.isVisible = (listType == "TOUGH_ALARMS")

        if (alarm.IsTemporary) {
            holder.quickAlarmIcon!!.setImageResource(R.drawable.ic_flash_on_icon)
            holder.quickAlarmText!!.text = if (alarm.IsTemporary) "Quick Alarm" else ""
        }

        holder.repeatDaysText!!.text = if (alarm.RepeatDays.isEmpty()) ut.getDate(alarm.SetTime) else alarm.RepeatDays
        holder.remainTimeText!!.text = alarm.getElapsedTime()

        if (alarm.IsActive) {
            if (alarm.IsTemporary) {
                holder.quickAlarmIcon!!.setColorFilter(Color.parseColor("#17a2b8"))
                holder.quickAlarmText!!.setTextColor(Color.parseColor("#17a2b8"))
            }

            holder.setTimeText!!.setTextColor(Color.parseColor("#18121E"))
            holder.repeatDaysText!!.setTextColor(Color.parseColor("#18121E"))
            holder.remainTimeText!!.setTextColor(Color.parseColor("#17a2b8"))

            if (listType == "TOUGH_ALARMS")
                holder.challengeIcon!!.setColorFilter(Color.parseColor("#E57373"))
        }
        else {
            if (alarm.IsTemporary) {
                holder.quickAlarmIcon!!.setColorFilter(Color.parseColor("#546E7A"))
                holder.quickAlarmText!!.setTextColor(Color.parseColor("#546E7A"))
            }

            holder.setTimeText!!.setTextColor(Color.parseColor("#546E7A"))
            holder.repeatDaysText!!.setTextColor(Color.parseColor("#546E7A"))
            holder.remainTimeText!!.setTextColor(Color.parseColor("#546E7A"))

            if (listType == "TOUGH_ALARMS")
                holder.challengeIcon!!.setColorFilter(Color.parseColor("#546E7A"))
        }

        holder.alarmSwitch!!.isChecked = alarm.IsActive

        holder.alarmSwitch!!.setOnCheckedChangeListener { _, isChecked ->
            toggleAlarmStatus(alarms, position, isChecked)
        }

        // Set Click Listener to display alarm details via AlarmDetailsActivity
        holder.invisible!!.setOnClickListener { v ->
            val context = v!!.context
            val intent = Intent(context, AlarmDetailsActivity::class.java)

            // Prepare DisplayAlarm Activity, passing the selected Alarm object
            intent.putExtra(AlarmDetailsActivity.KEY_ALARM_DETAILS, bundleOf())

            context.startActivity(intent)
        }

        // Set Hold Listener to display the ContextMenu
        holder.invisible!!.setOnCreateContextMenuListener { _, _, _ -> }
    }

    fun setAdapterData(alarms : ArrayList<Alarm>) { this.alarms = alarms }

    fun setListType(listType : String) { this.listType = listType }

    private fun toggleAlarmStatus(alarms : ArrayList<Alarm>, p : Int, isChecked : Boolean) {
        val alarm = alarms[p]

        alarm.IsActive = isChecked
        alarm.computeActiveTime(Date())

        if (_dbHelper!!.editAlarm(alarm))
            notifyDataSetChanged()
    }

    fun setItemPosition(pos : Int) { itemPosition = pos }
    fun getItemPosition() : Int { return itemPosition }
}