package radio.di

import radio.data.repository.RadioImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val RadioModule = module {
    singleOf(::RadioImpl)
}