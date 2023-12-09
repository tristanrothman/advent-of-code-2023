import javax.swing.text.StyledEditorKit.BoldAction

fun main() {
    tailrec fun extrapolate(sequence: List<List<Long>>, value: Long = 0L, factor:Int = 1, back: Boolean): Long {
        val current = sequence.last()
        val v = if(back) current.first() * factor else current.last()
        val result = value + v
        return if (current.all { it == 0L }) {
            result
        } else {
            val diff = sequence.plusElement(current.windowed(2).map { (a, b) -> b.minus(a) })
            extrapolate(diff, result, factor * -1, back)
        }
    }


    fun sum(input: List<String>, back: Boolean = false): Long {
        return input.map { line ->
            line.split("\\s+".toRegex()).map { it.toLong() }
        }.sumOf { extrapolate(sequence = listOf(it), back = back) }
    }


    val input = readInput("Day09")

    val testInput = readInput("Day09_test")

    check(sum(testInput) == 114L)
    println(sum(input))
    check(sum(testInput, true) == 2L)
    println(sum(input, true))
}