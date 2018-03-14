package com.cabbage.fireticv2.presentation.roomlist

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.OnClick
import com.cabbage.fireticv2.R
import com.cabbage.fireticv2.data.game.ModelRoom
import com.cabbage.fireticv2.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_room_list.*
import kotlinx.android.synthetic.main.include_appbar_basic.*
import java.text.DateFormat
import java.util.*
import javax.inject.Inject

class RoomListActivity : BaseActivity() {

    @Inject lateinit var mViewModel: RoomListViewModel

    @OnClick(R.id.fabAdd)
    fun addOnClick(v: View) {
        mViewModel.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_list)
        ButterKnife.bind(this)
        setUpActionBar(toolbar)

        activityComponent.inject(this)

        rvRooms.layoutManager = LinearLayoutManager(this)
        rvRooms.adapter = MyAdapter()


        mViewModel.availableRooms().observe(this, Observer { list ->
            if (list != null) (rvRooms.adapter as MyAdapter).dataSet = list
        })

//        val mock = List(20, { _ -> ModelRoom(roomId = "abc", hostPlayerId = "abc", creationTime = Date().time) })
//        (rvRooms.adapter as MyAdapter).dataSet = mock
    }

    private class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {

        var dataSet: List<ModelRoom> = emptyList()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
            return MyViewHolder(v)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//            Timber.i("bind: $position")
            holder.bind(dataSet[position])
        }

        override fun getItemCount() = dataSet.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvRoomId: TextView = itemView.findViewById(R.id.tvRoomId)
        val tvHostId: TextView = itemView.findViewById(R.id.tvHostId)
        val tvCreationTime: TextView = itemView.findViewById(R.id.tvCreationTime)
        val tvInviteCode: TextView = itemView.findViewById(R.id.tvInviteCode)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)

        @SuppressLint("SetTextI18n")
        fun bind(item: ModelRoom) {

            val formatter = DateFormat.getDateTimeInstance()

            tvRoomId.text = "RoomId: ${item.roomId}"
            tvHostId.text = "HostId: ${item.hostPlayerId}"
            tvCreationTime.text = formatter.format(Date(item.creationTime))
            tvInviteCode.text = "Invite code: ${item.inviteCode}"
            tvStatus.text = "Status: ${item.progress}"
        }
    }
}