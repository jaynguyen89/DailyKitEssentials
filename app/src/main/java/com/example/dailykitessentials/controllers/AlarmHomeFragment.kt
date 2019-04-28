package com.example.dailykitessentials.controllers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.dailykitessentials.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class AlarmHomeFragment : Fragment() {
    private val NORMAL_LIST_TYPE = "NORMAL_ALARMS"
    private val TOUGH_LIST_TYPE = "TOUGH_ALARMS"

    private val bottomNavigationItemListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.alarms_bottom_nav_normal -> {
                inflateAlarmListFor(NORMAL_LIST_TYPE)

                return@OnNavigationItemSelectedListener true
            }
            R.id.alarms_bottom_nav_normal_tough -> {
                inflateAlarmListFor(TOUGH_LIST_TYPE)

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.alarms_bottom_nav_view, container, false)
        val alarmsBottomNavView = view.findViewById<BottomNavigationView>(R.id.alarms_bottom_nav)

        // Listen to event: switching between bottom nav items
        alarmsBottomNavView.setOnNavigationItemSelectedListener(bottomNavigationItemListener)

        // Set default Alarm List to display normal alarms
        inflateAlarmListFor(NORMAL_LIST_TYPE)

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = AlarmHomeFragment()
    }

    private fun inflateAlarmListFor(type : String) {
        val childFragmentManager : FragmentManager = childFragmentManager
        val alarmListFragment = AlarmsListFragment.newInstance(type)

        childFragmentManager.beginTransaction()
            .replace(R.id.sub_frame, alarmListFragment, "alarm_list_fragment")
            .addToBackStack("alarm_list_fragment")
            .commit()
    }
}
