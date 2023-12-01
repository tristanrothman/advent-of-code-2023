fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            line.filter { it.isDigit() }.toCharArray().let { "${it.first()}${it.last()}".toInt() }
        }
    }

    fun part2(input: List<String>): Int {
        return part1(
            input.map { line ->
                buildString {
                    line.forEachIndexed { index, c ->
                        if (c.isDigit()) {
                            append(c)
                        } else {
                            Digit.entries.forEach { digit ->
                                if (line.substring(index).startsWith(digit.name.lowercase())) append(digit.num)
                            }
                        }

                    }
                }
            }
        )
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day01_Part01_test")
    check(part1(testInput1) == 142)

    val input = readInput("Day01")
    part1(input).println()

    val testInput2 = readInput("Day01_Part02_test")
    check(part2(testInput2) == 281)

    part2(input).println()
}

enum class Digit(
    val num: Int
) {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
}