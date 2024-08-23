package nz.ac.canterbury.seng303.flashcardapp.models

class Card(
    val id: Int,
    val question: String,
    val options: List<Option>,
    val timestamp: Long,
    val isArchived: Boolean
) : Identifiable {

    // Custom data class to hold each option
    data class Option(
        val option: String,
        val answer: Boolean
    )

    companion object {
        fun getCards(): List<Card> {
            return listOf(
                Card(
                    id = 1,
                    question = "Which of the following are programming languages?",
                    options = listOf(
                        Option("Python", true),
                        Option("JavaScript", true),
                        Option("HTML", false),
                        Option("CSS", false),
                        Option("C++", true)
                    ),
                    timestamp = System.currentTimeMillis(),
                    isArchived = false
                ),
                Card(
                    id = 2,
                    question = "Which of the following are fruit?",
                    options = listOf(
                        Option("Apple", true),
                        Option("Carrot", false),
                        Option("Banana", true),
                        Option("Tomato", true),
                        Option("Potato", false)
                    ),
                    timestamp = System.currentTimeMillis(),
                    isArchived = false
                ),
                // Add more cards here
            )
        }
    }

    override fun getIdentifier(): Int {
        return id
    }
}
