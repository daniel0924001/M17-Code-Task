package com.oneseven.codetest.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.oneseven.codetest.BR
import com.oneseven.codetest.R
import com.oneseven.codetest.model.UserInfo

class UserListAdapter(private val data : MutableList<UserInfo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var previousSize: Int = 0

    var itemClick: ((String?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = when(viewType) {
            0 -> ItemViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_user_big, parent, false))
            else -> ItemViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.item_user_small, parent, false))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).dataBinding.setVariable(BR.userInfo, this.data.get(position))
        holder.itemView.setOnClickListener {
            itemClick?.invoke(this.data.get(position).login)
        }
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    override fun getItemViewType(position: Int): Int {
        if(this.data.get(position).itemType == -1) {
            this.data.get(position).itemType = (0..1).random()
        }
        return this.data.get(position).itemType
    }

    fun clear() {
        data.clear()
    }

    fun updateList() {
        val increase : Int = itemCount - previousSize
        notifyItemRangeInserted(previousSize, increase)
        previousSize = itemCount
    }
}