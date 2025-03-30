package main.di

import org.koin.dsl.module
import player.data.repository.PlayerImpl
import player.domain.repository.PlayerRepository
import player.presentation.viewmodel.PlayerViewModel
import radio.data.api.RadioBrowserApi
import radio.data.repository.RadioImpl
import radio.domain.repository.RadioRepository
import radio.presentation.viewmodel.RadioViewModel

val sharedModule = module {
    single<PlayerRepository> { PlayerImpl() }
    single { RadioBrowserApi() }
    single<RadioRepository> { RadioImpl(get()) }
    single { PlayerViewModel(get()) }
    single { RadioViewModel(get(), get()) }
}