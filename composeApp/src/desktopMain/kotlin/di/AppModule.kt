package di

import org.koin.dsl.module
import radio.data.api.RadioBrowserApi
import radio.data.repository.RadioImpl
import radio.domain.repository.RadioRepository
import radio.presentation.viewmodel.RadioViewModel

val appModule = module {
    single { RadioBrowserApi() }
    single<RadioRepository> { RadioImpl(get()) }
    single { RadioViewModel(get(), get()) }
} 