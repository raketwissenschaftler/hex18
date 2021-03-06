package com.treecio.hexplore.model

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.raizlabs.android.dbflow.list.FlowQueryList
import com.treecio.hexplore.R

class UserAdapter(private val userList:FlowQueryList<User>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        holder?.txtName?.text = userList[position]?.name ?: "Unknown"
        holder?.txtShakeCount?.text = userList[position]?.handshakeCount.toString()
        holder?.txtDescription?.text = "No description yet"

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.person_card, parent, false)
        return ViewHolder(v);
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val txtName = itemView.findViewById<TextView>(R.id.person_card_name)
        val txtShakeCount = itemView.findViewById<TextView>(R.id.person_card_shake_count)
        val txtDescription = itemView.findViewById<TextView>(R.id.person_card_description)
    }

}
