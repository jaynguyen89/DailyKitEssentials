package com.example.dailykitessentials.controllers

import android.content.Intent
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailykitessentials.R
import com.example.dailykitessentials.adapters.AlarmAdapter
import com.example.dailykitessentials.helpers.DatabaseHelper
import com.example.dailykitessentials.models.Alarm
import com.example.dailykitessentials.providers.ListRowDecorator
import com.example.dailykitessentials.providers.RowSwipeListener
import com.example.dailykitessentials.providers.RowTouchListener
import com.example.dailykitessentials.providers.TouchListener

private const val KEY_LIST_TYPE = ""

interface ClickListener {
    fun onItemClicked(position : Int)
}

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AlarmsListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AlarmsListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AlarmsListFragment : Fragment() {

    private var listType: String? = null
    private var alarms: ArrayList<Alarm> = ArrayList()
    private var alarmAdapter : AlarmAdapter = AlarmAdapter()

    private var _dbHelper: DatabaseHelper? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            listType = it.getString(KEY_LIST_TYPE)
        }

        _dbHelper = DatabaseHelper(activity!!.applicationContext)
        alarms = listType?.let { _dbHelper!!.getAllAlarmsByType(it) }!!

        alarmAdapter.setAdapterData(alarms)
        alarmAdapter.setListType(listType!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.alarms_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val alarmRecyclerView = view.findViewById<RecyclerView>(R.id.alarms_list)
        val alarmRecyclerLayoutManager : RecyclerView.LayoutManager = LinearLayoutManager(context)

        alarmRecyclerView.layoutManager = alarmRecyclerLayoutManager
        alarmRecyclerView.itemAnimator = DefaultItemAnimator()

        //val swipeListener = RowSwipeListener()

        //val touchListener = ItemTouchHelper(swipeListener)
        //touchListener.attachToRecyclerView(alarmRecyclerView)

        val orientation = this.resources.configuration.orientation
        var decorator = context?.let { ListRowDecorator(it, orientation, RowSwipeListener()) }
        decorator?.let { alarmRecyclerView.addItemDecoration(it) }

        alarmRecyclerView.adapter = alarmAdapter

        context?.let {
            RowTouchListener(it, alarmRecyclerView, object : TouchListener {
                override fun onHold(view: View, position: Int) { /* See RowTouchListener */ }
                override fun onTouch(view: View, position: Int) { }
            })
        }?.let { alarmRecyclerView.addOnItemTouchListener(it) }

        // Make ContextMenu items work on option selected
        registerForContextMenu(alarmRecyclerView)
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param LIST_TYPE The type of alarms to be displayed (Normal/Tough).
         * @return A new instance of fragment AlarmsListFragment.
         */
        @JvmStatic
        fun newInstance(LIST_TYPE: String) =
            AlarmsListFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_LIST_TYPE, LIST_TYPE)
                }
            }
    }
}
