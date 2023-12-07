fun main() {
    fun calculate(input: List<String>, joker: Boolean = false): Int {
        return input.map { line ->
            val (cards, bid) = line.split(" ").map { it.trim() }
            CamelHand(cards.map { CamelCard.valueOf(it.toString()) }, bid.toInt(), joker)
        }.sortedByDescending { it }.mapIndexed { index, hand -> (index + 1) * hand.bid }.sum()
    }

    val testInput = readInput("Day07_test")
    val input = readInput("Day07")

    check(calculate(testInput) == 6440)
    println(calculate(input))

    check(calculate(testInput, true) == 5905)
    println(calculate(input, true))
}

enum class CamelCard {
    A,
    K,
    Q,
    J,
    T,
    `9`,
    `8`,
    `7`,
    `6`,
    `5`,
    `4`,
    `3`,
    `2`;

    val jokerOrdinal: Int
        get() {
            return when (this) {
                A, K, Q -> this.ordinal
                J -> 13
                else -> this.ordinal - 1
            }
        }
}

data class CamelHand(
    val cards: List<CamelCard>,
    val bid: Int,
    val joker: Boolean = false,
    val handType: HandType = handType(cards, joker),
) : Comparable<CamelHand> {
    private val cardsComparator: Comparator<List<CamelCard>> = Comparator { hand1, hand2 ->
        hand1.zip(hand2).forEach { (card1, card2) ->
            val firstOrdinal = if(joker) card1.jokerOrdinal else card1.ordinal
            val secondOrdinal = if(joker) card2.jokerOrdinal else card2.ordinal
            if (firstOrdinal < secondOrdinal) return@Comparator -1
            if (firstOrdinal > secondOrdinal) return@Comparator 1
        }
        return@Comparator 0
    }

    override fun compareTo(other: CamelHand): Int {
        return if (this.handType != other.handType) {
            this.handType.compareTo(other.handType)
        } else cardsComparator.compare(this.cards, other.cards)
    }
}

fun handType(cards: List<CamelCard>, joker: Boolean): HandType {
    val groups = cards
        .groupBy { it }.entries
        .map { (card, num) -> CardCount(card, num.size) }
        .sortedByDescending { it.count }.toMutableList()

    if (joker) {
        val j = groups.firstOrNull { it.card == CamelCard.J }
        val biggest = groups.firstOrNull { it.card != CamelCard.J }
        if (j != null && biggest != null) {
            biggest.count += j.count
            groups.remove(j)
        }
    }

    return when {
        groups.size == 1 -> HandType.FIVE_OF_A_KIND
        groups.size == 2 && groups.first().count == 4 -> HandType.FOUR_OF_A_KIND
        groups.size == 2 && groups.first().count == 3 -> HandType.FULL_HOUSE
        groups.size == 3 && groups.first().count == 3 -> HandType.THREE_OF_A_KIND
        groups.size == 3 && groups.first().count == 2 -> HandType.TWO_PAIRS
        groups.size == 4 && groups.first().count == 2 -> HandType.ONE_PAIR
        else -> HandType.HIGH_CARD
    }
}

enum class HandType {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIRS,
    ONE_PAIR,
    HIGH_CARD;
}

data class CardCount(
    val card: CamelCard,
    var count: Int,
)