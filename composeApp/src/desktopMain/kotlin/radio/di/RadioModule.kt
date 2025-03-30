package radio.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import radio.data.api.RadioBrowserApi
import radio.data.repository.RadioImpl
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module
import player.domain.repository.PlayerRepository
import player.presentation.viewmodel.PlayerViewModel
import radio.domain.repository.RadioRepository
import radio.presentation.viewmodel.RadioViewModel

val RadioModule = module {

    single { RadioBrowserApi() }

    single<RadioRepository> { RadioImpl(get()) }

    singleOf(::RadioImpl) withOptions {
        // definition options
        named("radioRepository")
        bind<RadioRepository>()
        createdAtStart()
    }

    single {
        RadioViewModel(get<RadioRepository>(), get<RadioBrowserApi>())
    }

}