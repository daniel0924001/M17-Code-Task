package com.oneseven.codetest.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oneseven.codetest.R
import com.oneseven.codetest.databinding.ActivityMainBinding
import com.oneseven.codetest.viewmodel.UserInfoFactory
import com.oneseven.codetest.viewmodel.UserInfoRepository
import com.oneseven.codetest.viewmodel.UserInfoViewModel
import com.oneseven.codetest.viewmodel.UserListAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding : ActivityMainBinding

    private lateinit var infoViewModel: UserInfoViewModel
    private lateinit var infoFactory: UserInfoFactory
    private lateinit var infoRepository: UserInfoRepository

    private lateinit var userListAdapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main) as ActivityMainBinding

        infoRepository = UserInfoRepository()
        infoFactory = UserInfoFactory(infoRepository)
        infoViewModel = ViewModelProvider(this, infoFactory).get(UserInfoViewModel::class.java)

        initRecyclerView();

        infoViewModel.getUserInfos().observe(this, Observer {
            Log.i("MainActivity", "User Data updated with size = $it.size()")
            activityMainBinding.loading = false
            userListAdapter.notifyDataSetChanged()
        })

        infoViewModel.getRecentThrowable().observe(this, Observer {
            Log.e("MainActivity", "Exception: ${it.toString()}")
            activityMainBinding.loading = false
            Toast.makeText(this, "Exception: ${it.toString()}", Toast.LENGTH_LONG).show()
        })

    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        activityMainBinding.searchResult.layoutManager = layoutManager
        userListAdapter = UserListAdapter(infoViewModel.getUserInfos().value!!)
        activityMainBinding.searchResult.adapter = userListAdapter
    }

    fun onInitUserInfosBtn(view : View) {
        activityMainBinding.loading = true;
        infoViewModel.loadMoreUserInfos(activityMainBinding.inputName.text.toString())
        userListAdapter.clear()

        hideKeyboard()
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


