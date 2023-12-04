import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { Card(it) }.sumOf { it.points }
    }

    fun part2(input: List<String>): Int {
        val cardCopies = IntArray(input.size) { 1 }
        val originals = input.map { Card(it) }.toMutableList()

        originals.forEachIndexed { index, card ->
            if(card.copies > 0) {
                for (i in 1..card.copies) {
                    if (index + i >= cardCopies.size) break
                    cardCopies[index + i] += cardCopies[index]
                }
            }
        }

        return cardCopies.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")


    val input = readInput("Day04")
    part1(input).println()

    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    part2(input).println()
}

class Card(str: String) {
    val number: Int
    private val winningNumbers: MutableSet<Int> = mutableSetOf()
    private val cardNumbers: MutableSet<Int> = mutableSetOf()
    val copies: Int
    val points: Int

    init {
        val regex = Regex("Card\\s+(\\d+):\\s+(\\b\\d+\\b(?:\\s+\\d+\\b)*)\\s*\\|\\s*(\\b\\d+\\b(?:\\s+\\d+\\b)*)")
        val result = regex.find(str) ?: error("Input error")
        number = result.groups[1]!!.value.toInt()
        winningNumbers.addAll(result.groups[2]!!.value.split(Regex("\\s+")).map { it.toInt() })
        cardNumbers.addAll(result.groups[3]!!.value.split(Regex("\\s+")).map { it.toInt() })
        copies = winningNumbers.intersect(cardNumbers).size
        points = copies.let { if (it == 0) 0 else 2.toDouble().pow(it - 1).toInt() }
    }
}