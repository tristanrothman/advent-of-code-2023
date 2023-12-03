fun main() {


    fun mapGames(input: List<String>): List<Game> {
        return input.map { line ->
            Game(
                id = line.substringBefore(':').substringAfter(' ').toInt(),
                set = line.substringAfter(':').split(';').map { hand ->
                    hand.split(',').map { cube ->
                        Cube(
                            num = cube.substringBeforeLast(' ').drop(1).toInt(),
                            colour = Colour.valueOf(cube.substringAfterLast(' ').uppercase())
                        )
                    }.let { cubes ->
                        Hand(
                            red = cubes.find { it.colour == Colour.RED } ?: Cube(0, Colour.RED),
                            green = cubes.find { it.colour == Colour.GREEN } ?: Cube(0, Colour.GREEN),
                            blue = cubes.find { it.colour == Colour.BLUE } ?: Cube(0, Colour.BLUE),
                        )
                    }
                }.toSet()
            )
        }
    }

    fun part1(input: List<String>): Int {
        return mapGames(input).filterNot { game ->
            game.set.any { cube ->
                (cube.red.num > 12).or(cube.green.num > 13).or(cube.blue.num > 14)
            }
        }.sumOf { it.id }
    }

    fun part2(input: List<String>): Int {
        return mapGames(input).sumOf { game ->
            game.power()
        }
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput).also { println(it) } == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

data class Cube(
    val num: Int,
    val colour: Colour
)

enum class Colour {
    RED,
    GREEN,
    BLUE,
}

data class Game(
    val id: Int,
    val set: Set<Hand>
) {
    private fun minRed() = set.maxBy { it.red.num }.red.num
    private fun minGreen() = set.maxBy { it.green.num }.green.num
    private fun minBlue() = set.maxBy { it.blue.num }.blue.num
    fun power() = minRed() * minGreen() * minBlue()
}

data class Hand(
    val red: Cube,
    val green: Cube,
    val blue: Cube,
)


