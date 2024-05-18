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


val PlayerModule = module(createdAtStart=true) {

//    single<PlayerRepository>(named("playerRepository")) {
//        PlayerImpl()
//    }

    singleOf(::PlayerImpl) withOptions {
        // definition options
        named("playerRepository")
        bind<PlayerRepository>()
        createdAtStart()
    }


    single<PlayerViewModel>{
        PlayerViewModel()
    } withOptions {
        // definition options
        named("playerViewModel")
        createdAtStart()
    }




//    single<PlayerRepository> { PlayerImpl() as PlayerRepository } withOptions {
//        named("playerRepository")
//        createdAtStart()
//    }
}