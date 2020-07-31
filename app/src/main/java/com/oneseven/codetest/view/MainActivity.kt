package com.oneseven.codetest.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.oneseven.codetest.R
import com.oneseven.codetest.databinding.ActivityMainBinding
import com.oneseven.codetest.viewmodel.UserInfoFactory
import com.oneseven.codetest.viewmodel.UserInfoRepository
import com.oneseven.codetest.viewmodel.UserInfoViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding : ActivityMainBinding

    private lateinit var infoViewModel: UserInfoViewModel
    private lateinit var infoFactory: UserInfoFactory
    private lateinit var infoRepository: UserInfoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main) as ActivityMainBinding

        infoRepository = UserInfoRepository()
        infoFactory = UserInfoFactory(infoRepository)
        infoViewModel = ViewModelProvider(this, infoFactory).get(UserInfoViewModel::class.java)

        infoViewModel.getUserInfos().observe(this, Observer {
            Toast.makeText(this, "load completed! size = ${it.size}", Toast.LENGTH_SHORT).show()
            activityMainBinding.loading = false;
        })

    }

    fun onInitUserInfosBtn(view : View) {
        activityMainBinding.loading = true;
        infoViewModel.loadMoreUserInfos()
    }
}
