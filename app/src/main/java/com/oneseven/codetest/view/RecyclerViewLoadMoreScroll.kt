package com.oneseven.codetest.view

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewLoadMoreScroll(private val mLayoutManager: RecyclerView.LayoutManager, private val isLoading: MutableLiveData<Boolean>) :
    RecyclerView.OnScrollListener() {

    private var visibleThreshold = 5
    private lateinit var mOnLoadMoreListener: OnLoadMoreListener
    private var lastVisibleItem: Int = 0
    private var totalItemCount:Int = 0

    fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener
    }


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy <= 0) return

        totalItemCount = mLayoutManager.itemCount

        if (mLayoutManager is LinearLayoutManager) {
            lastVisibleItem = (mLayoutManager).findLastVisibleItemPosition()
        }

        if (!isLoading.value!! && totalItemCount <= lastVisibleItem + visibleThreshold) {
            mOnLoadMoreListener.onLoadMore()
            isLoading.value = true
        }

    }

}