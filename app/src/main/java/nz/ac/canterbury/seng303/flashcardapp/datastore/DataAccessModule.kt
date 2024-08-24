package nz.ac.canterbury.seng303.flashcardapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.FlowPreview
import nz.ac.canterbury.seng303.flashcardapp.models.Card
import nz.ac.canterbury.seng303.flashcardapp.viewmodels.CardViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "nz.ac.canterbury.seng303.lab1.shared.preferences")

@FlowPreview
val dataAccessModule = module {
    single<Storage<Card>> {
        PersistentStorage(
            gson = get(),
            type = object: TypeToken<List<Card>>(){}.type,
            preferenceKey = stringPreferencesKey("cards"),
            dataStore = androidContext().dataStore
        )
    }

    single { Gson() }

    viewModel {
        CardViewModel(
            cardStorage = get()
        )
    }
}