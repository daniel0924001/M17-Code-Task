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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oneseven.codetest.R
import com.oneseven.codetest.databinding.ActivityMainBinding
import com.oneseven.codetest.databinding.DialogUserDetailBinding
import com.oneseven.codetest.viewmodel.UserInfoFactory
import com.oneseven.codetest.viewmodel.UserInfoRepository
import com.oneseven.codetest.viewmodel.UserInfoViewModel
import com.oneseven.codetest.viewmodel.UserListAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding : ActivityMainBinding
    private lateinit var dialogUserDetailBinding : DialogUserDetailBinding

    private lateinit var infoViewModel: UserInfoViewModel
    private lateinit var infoFactory: UserInfoFactory
    private lateinit var infoRepository: UserInfoRepository

    private lateinit var userListAdapter: UserListAdapter
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll

    private lateinit var loading: MutableLiveData<Boolean>

    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main) as ActivityMainBinding

        infoRepository = UserInfoRepository()
        infoFactory = UserInfoFactory(infoRepository)
        infoViewModel = ViewModelProvider(this, infoFactory).get(UserInfoViewModel::class.java)

        loading = infoViewModel.getUILoading()
        loading.observe(this, Observer {
            activityMainBinding.loading = it
        })

        initRecyclerView()
        initDialog()

        infoViewModel.getUserInfos().observe(this, Observer {
            Log.i("MainActivity", "User Data updated with size = $it.size()")
            loading.value = false
            userListAdapter.updateList()
        })

        infoViewModel.getRecentThrowable().observe(this, Observer {
            Log.e("MainActivity", "Exception: $it")
            loading.value = false
            Toast.makeText(this, "Exception: $it", Toast.LENGTH_LONG).show()
        })

        infoViewModel.getUserDetail().observe(this, Observer {
            dialogUserDetailBinding.userDetail = it
            dialog.show()
        })

    }

    private fun initDialog() {
        dialogUserDetailBinding = DataBindingUtil
            .inflate(LayoutInflater.from(this), R.layout.dialog_user_detail, null, false);
        dialog = Dialog(this)
        dialog.setContentView(dialogUserDetailBinding.getRoot());
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        activityMainBinding.searchResult.layoutManager = layoutManager
        userListAdapter = UserListAdapter(infoViewModel.getUserInfos().value!!).apply {
            itemClick = { userName ->
                Log.e("MainActivity", "open user name: $userName")
                infoViewModel.loadUserDetail(userName)
            }
        }
        activityMainBinding.searchResult.adapter = userListAdapter

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager, loading)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                loading.value = true
                infoViewModel.loadMoreUserInfos(activityMainBinding.inputName.text.toString())
            }
        })
        activityMainBinding.searchResult.addOnScrollListener(scrollListener)
    }

    fun onInitUserInfosBtn(view : View) {
        loading.value = true
        infoViewModel.loadMoreUserInfos(activityMainBinding.inputName.text.toString())
        userListAdapter.clear()

        hideKeyboard()
    }

    fun onDialogDismissBtn(view : View) {
        dialog.dismiss()
    }

    fun AppCompatActivity.hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }
}


