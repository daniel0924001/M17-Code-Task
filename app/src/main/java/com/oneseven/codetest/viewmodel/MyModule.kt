package com.oneseven.codetest.viewmodel

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModule = module {
    viewModel { UserInfoViewModel(get()) }
}

val repoModule = module {
    single { UserInfoRepository() }
}