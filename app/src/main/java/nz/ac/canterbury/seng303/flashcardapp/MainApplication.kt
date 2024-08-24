package nz.ac.canterbury.seng303.flashcardapp


import android.app.Application
import kotlinx.coroutines.FlowPreview
import nz.ac.canterbury.seng303.flashcardapp.datastore.dataAccessModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication: Application() {

    @OptIn(FlowPreview::class)
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(dataAccessModule)
        }
    }
}