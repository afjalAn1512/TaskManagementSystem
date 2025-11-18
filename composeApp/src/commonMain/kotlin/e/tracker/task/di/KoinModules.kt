package e.tracker.task.di

import androidx.room.RoomDatabase
import e.tracker.task.database.TaskDao
import e.tracker.task.database.TaskDatabase
import e.tracker.task.repository.TaskRepository
import e.tracker.task.viewmodel.TaskViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val dataModule = module {
    singleOf(::TaskRepository) { bind<TaskRepository>() }
    single<TaskDao> {
        val dbBuilder = get<RoomDatabase.Builder<TaskDatabase>>()
        dbBuilder.build().taskDao()
    }
}


val viewModelModule = module {
    viewModelOf(::TaskViewModel)
}

expect val nativeModule: Module
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(dataModule, viewModelModule, nativeModule)
    }
}