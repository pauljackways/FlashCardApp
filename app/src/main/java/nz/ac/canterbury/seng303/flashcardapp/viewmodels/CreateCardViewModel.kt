package nz.ac.canterbury.seng303.flashcardapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import nz.ac.canterbury.seng303.flashcardapp.models.Card


class CreateCardViewModel: ViewModel() {
    var question by mutableStateOf("")
        private set
    fun updateQuestion(newQuestion: String) {
        question = newQuestion
    }
    var options by mutableStateOf(listOf<Card.Option>())
        private set
    fun addOption() {
        options = options + Card.Option(false, "")
    }
    fun initNewCard(options: Int) {
        repeat(options) {
            addOption()
        }
    }

    fun updateOption(index: Int, answer: Boolean, newOption: String) {
        options = options.toMutableList().apply {
            this[index] = this[index].copy(option = newOption, answer = answer)
        }
    }
    fun removeOption(index: Int) {
        options = options.toMutableList().apply {
            removeAt(index)
        }
    }
}