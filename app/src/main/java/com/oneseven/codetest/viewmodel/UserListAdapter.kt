package com.oneseven.codetest.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.oneseven.codetest.BR
import com.oneseven.codetest.R
import com.oneseven.codetest.model.UserInfo

class UserListAdapter(private val data : MutableList<UserInfo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var previousSize: Int = 0
    private var counter: Int = 0
    private var itemCount: Int = 0
    private var dataPositionList: MutableList<Int> = ArrayList()


    var itemClick: ((String?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), viewType, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val resId : Int = getItemViewType(position)

        val dataPosition = dataPositionList[position]
        if(resId == R.layout.item_user_big || resId == R.layout.item_user_small) {
            (holder as ItemViewHolder).dataBinding.setVariable(BR.userInfo, this.data[dataPosition])
            holder.itemView.setOnClickListener {
                itemClick?.invoke(this.data[dataPosition].login)
            }
        } else {
            (holder as ItemViewHolder).dataBinding.setVariable(BR.userInfoLeft, this.data[dataPosition])
            holder.dataBinding.setVariable(BR.userInfoRight, this.data[dataPosition + 1])
            holder.itemView.findViewById<View>(R.id.left_item).setOnClickListener {
                itemClick?.invoke(this.data[dataPosition].login)
            }
            holder.itemView.findViewById<View>(R.id.right_item).setOnClickListener {
                itemClick?.invoke(this.data[dataPosition + 1].login)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemCount
    }

    override fun getItemViewType(position: Int): Int {
        val dataPosition = dataPositionList[position]
        return this.data[dataPosition].itemType
    }

    private fun decideItemType() {

        when(if(counter != data.size - 1) {
            (0..2).random()
        } else {
            (0..1).random()
        }) {
            0 -> {
                dataPositionList.add(counter)
                this.data[counter++].itemType = R.layout.item_user_big
            }
            1 -> {
                dataPositionList.add(counter)
                this.data[counter++].itemType = R.layout.item_user_small
            }
            else -> {
                dataPositionList.add(counter)
                this.data[counter].itemType = R.layout.item_user_double
                counter += 2
            }
        }
    }

    fun clear() {
        data.clear()
        counter = 0
        itemCount = 0
        previousSize = 0
    }

    fun updateList() {
        while(counter < data.size) {
            decideItemType()
            itemCount++
        }
        val increase : Int = itemCount - previousSize
        notifyItemRangeInserted(previousSize, increase)
        previousSize = itemCount
    }
}