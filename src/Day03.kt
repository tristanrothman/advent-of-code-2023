fun main() {
    fun part1(input: List<String>): Int {
        return input.toSchematic().calculatePart1()
    }

    fun part2(input: List<String>): Int {
        return input.toSchematic().calculatePart2()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

fun List<String>.calculatePart1(): Int {
    return this.mapIndexed { row, chars ->
        chars.foldIndexed(Line()) { column, line, char ->
            if (char.isDigit()) {
                line.num += char
                this.checkAdjacent(row, column, line)
            } else {
                if (line.num.isNotEmpty()) {
                    if (line.symbol) {
                        line.sum += line.num.toInt()
                    }
                    line.num = ""
                    line.symbol = false

                }
                line
            }
        }
    }.sumOf { it.sum }
}

fun List<String>.calculatePart2(): Int {
    val partsByGear = mutableMapOf<Part, MutableList<Int>>()

    this.forEachIndexed { row, chars ->
        chars.foldIndexed(Line()) { column, line, char ->
            if (char.isDigit()) {
                line.num += char
                this.checkAdjacent(row, column, line)
            } else {
                if (line.num.isNotEmpty()) {
                    if (line.parts.isNotEmpty()) {
                        val partNumber = line.num.toInt()
                        line.parts.forEach { pos ->
                            partsByGear.getOrPut(pos) { mutableListOf() }.add(partNumber)
                        }
                    }
                    line.num = ""
                    line.parts.clear()
                }
                line
            }
        }
    }

    return partsByGear.values.filter { it.size == 2 }.sumOf { it.reduce(Math::multiplyExact) }
}

fun List<String>.checkAdjacent(row: Int, column: Int, line: Line): Line {
    for (x in listOf(-1, 0, 1)) {
        for (y in listOf(-1, 0, 1)) {
            if (x == 0 && y == 0) continue

            val i = x + row
            val j = y + column
            if (i in indices && j in 0 until this.first().length) {
                val adj = this[i][j]
                if (!adj.isDigit() && adj != '.') {
                    line.symbol = true
                }
                if (adj == '*') line.parts.add(i to j)
            }
        }
    }
    return line
}

fun List<String>.toSchematic(): List<String> {
    val empty = CharArray(this.first().length + 1) { '.' }.joinToString("")
    return buildList {
        add(empty)
        addAll(this@toSchematic.map { "$it." })
        add(empty)
    }
}

data class Line(
    var sum: Int = 0,
    var num: String = "",
    var symbol: Boolean = false,
    var parts: MutableSet<Part> = mutableSetOf(),
)

typealias Part = Pair<Int, Int>