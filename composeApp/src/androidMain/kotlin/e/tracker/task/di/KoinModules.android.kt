package e.tracker.task.di

import e.tracker.task.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual val nativeModule: Module = module {
    single { getDatabaseBuilder(get()) }
}