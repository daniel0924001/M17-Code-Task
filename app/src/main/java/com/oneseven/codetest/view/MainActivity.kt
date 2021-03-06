package com.oneseven.codetest.view

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oneseven.codetest.R
import com.oneseven.codetest.databinding.ActivityMainBinding
import com.oneseven.codetest.databinding.DialogUserDetailBinding
import com.oneseven.codetest.viewmodel.UserInfoViewModel
import com.oneseven.codetest.viewmodel.UserListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding : ActivityMainBinding
    private lateinit var dialogUserDetailBinding : DialogUserDetailBinding

    private val infoViewModel: UserInfoViewModel by viewModel()

    private var userListAdapter: UserListAdapter? = null
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll

    private lateinit var loading: MutableLiveData<Boolean>

    private var dialog : Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main) as ActivityMainBinding

        loading = infoViewModel.getUILoading()
        loading.observe(this, Observer {
            activityMainBinding.loading = it
        })

        initRecyclerView()
        initDialog()

        infoViewModel.getUserInfos().observe(this, Observer {
            Log.i("MainActivity", "User Data updated with size = $it.size()")
            loading.value = false
            userListAdapter?.updateList()
        })

        infoViewModel.getRecentThrowable().observe(this, Observer {
            Log.e("MainActivity", "Exception: $it")
            loading.value = false
            Toast.makeText(this, "Exception: $it", Toast.LENGTH_LONG).show()
        })

        infoViewModel.getUserDetail().observe(this, Observer {
            dialogUserDetailBinding.userDetail = it
            dialog?.show()
        })

    }

    private fun initDialog() {
        dialogUserDetailBinding = DataBindingUtil
            .inflate(LayoutInflater.from(this), R.layout.dialog_user_detail, null, false)
        dialog = Dialog(this)
        dialog!!.setContentView(dialogUserDetailBinding.root)
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        activityMainBinding.searchResult.layoutManager = layoutManager
        bindAdapter()

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager, loading)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                loading.value = true
                infoViewModel.loadMoreUserInfos(activityMainBinding.inputName.text.toString())
            }
        })
        activityMainBinding.searchResult.addOnScrollListener(scrollListener)
    }

    private fun bindAdapter() {
        userListAdapter?.clear()
        userListAdapter = UserListAdapter(infoViewModel.getUserInfos().value!!).apply {
            itemClick = { userName ->
                Log.e("MainActivity", "open user name: $userName")
                infoViewModel.loadUserDetail(userName)
            }
        }
        activityMainBinding.searchResult.adapter = userListAdapter
    }

    fun onInitUserInfosBtn(view : View) {
        loading.value = true
        bindAdapter()
        infoViewModel.loadMoreUserInfos(activityMainBinding.inputName.text.toString())

        hideKeyboard()
    }

    fun onDialogDismissBtn(view : View) {
        dialog?.dismiss()
    }

    private fun AppCompatActivity.hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun onDestroy() {
        dialog?.dismiss()
        super.onDestroy()
    }
}


