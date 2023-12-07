fun main() {
    fun ways(map: Map<Long, Long>): Long {
        return map.entries.map {(time: Long, distance: Long) ->
            (1L ..< time).count {
                it * (time - it) > distance
            }
        }.reduce(Math::multiplyExact).toLong()

    }

    fun part1(input: List<String>): Long {
        val (times, distances) = input.map { line ->
            line.split(Regex("\\s+")).drop(1).map { it.toLong() }
        }
        return ways(times.zip(distances).toMap())
    }

    fun part2(input: List<String>): Long {
        val (time, distance) = input.map { line ->
            line.split(Regex("\\s+")).drop(1).joinToString("").toLong()
        }
        return ways(mapOf( time to distance))
    }

    val testInput = readInput("Day06_test")
    val input = readInput("Day06")

    check(part1(testInput) == 288L)
    println(part1(input))

    check(part2(testInput) == 71503L)
    println(part2(input))
}