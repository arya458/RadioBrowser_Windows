package player.di

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import player.data.repository.PlayerImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module
import player.domain.repository.PlayerRepository
import player.presentation.viewmodel.PlayerViewModel

val PlayerModule = module(createdAtStart = true) {
    singleOf(::PlayerImpl) withOptions {
        bind<PlayerRepository>()
        createdAtStart()
    }

    single {
        PlayerViewModel(get<PlayerRepository>())
    }
}