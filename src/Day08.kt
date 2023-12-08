fun main() {
    fun lcm(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

    fun parse(input: List<String>): DesertMap {
        return DesertMap(
            instructions = input.first().map { Direction.valueOf(it.toString()) },
            nodes = LinkedHashMap(input.drop(2).associate { line ->
                line.split(" = ").let { (node, lr) ->
                    node to lr.drop(1).dropLast(1).split(", ").let { (left, right) ->
                        Node(left, right)
                    }
                }
            })
        )
    }

    fun calculate(desertMap: DesertMap, start: String, pred: (String) -> Boolean): Long {
        var current = start
        var steps = 0
        while (!pred(current)) {
            current = when (desertMap.instructions[steps % desertMap.instructions.size]) {
                Direction.L -> desertMap.nodes[current]!!.left
                Direction.R -> desertMap.nodes[current]!!.right
            }
            steps++
        }

        return steps.toLong()
    }

    fun part1(input: List<String>): Long {
        return calculate(parse(input), "AAA") { it == "ZZZ" }
    }

    fun part2(input: List<String>): Long {
        val desertMap = parse(input)

        return desertMap.nodes.keys.filter { it.last() == 'A' }.map {
            calculate(desertMap, it) { start ->
                start.last() == 'Z'
            }
        }.reduce { acc, l -> lcm(acc, l) }
    }

    val input = readInput("Day08")

    val testInput1 = readInput("Day08_test1")
    val testInput2 = readInput("Day08_test2")

    check(part1(testInput1) == 2L)
    check(part1(testInput2) == 6L)
    println(part1(input))

    val testInput3 = readInput("Day08_test3")
    check(part2(testInput3) == 6L)
    println(part2(input))
}

data class DesertMap(
    val instructions: List<Direction>,
    val nodes: LinkedHashMap<String, Node>,
)

data class Node(
    val left: String,
    val right: String,
)

enum class Direction {
    `L`,
    `R`;
}