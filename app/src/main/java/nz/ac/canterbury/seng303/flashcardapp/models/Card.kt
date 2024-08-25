package nz.ac.canterbury.seng303.flashcardapp.models

class Card(
    val id: Int,
    val question: String,
    var options: List<Option>,
    val timestamp: Long,
    val isArchived: Boolean
) : Identifiable {

    // Custom data class to hold each option
    data class Option(
        var answer: Boolean,
        val option: String
    )

    companion object {
        fun getCards(): List<Card> {
            return listOf(

                // Add more cards here
            )
        }
    }

    override fun getIdentifier(): Int {
        return id
    }
}
