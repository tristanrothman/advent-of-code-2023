fun main() {
    fun ranges(group: String): List<Range> {
        return group.split("\n").drop(1).map { line -> Range(line) }
    }

    fun List<Range>.convert(x: Long): Long {
        for (r in this) {
            if (x in r.src..<r.src + r.len)
                return x - r.src + r.dst
        }
        return x
    }

    fun List<Range>.convert(ranges: List<MinMax>): List<MinMax> {
        return ranges.flatMap { (min, max) ->
            val d = mutableListOf<MinMax>()
            val i = mutableListOf<MinMax>()

            for (r in this) {
                val rmin = maxOf(min, r.src)
                val rmax = minOf(max, r.src + r.len)
                if (rmin <= rmax) {
                    i.add(MinMax(rmin, rmax))
                    d.add(MinMax(rmin - r.src + r.dst, rmax - r.src + r.dst))
                }
            }

            i.sortBy { it.min }
            var pivot = min
            for ((rmin, rmax) in i) {
                if (rmin > pivot) {
                    d.add(MinMax(pivot, rmin - 1))
                }
                pivot = rmax + 1
            }
            if (pivot <= max) {
                d.add(MinMax(pivot, max))
            }

            d
        }
    }

    fun parseAlmanac(input: String): Almanac {
        val groups = input.split("\n\n")
        val seeds = groups[0].split(" ").drop(1).map { it.toLong() }
        return Almanac(
            seedRanges = seeds.chunked(2).map { (start, length) -> MinMax(start, start + length) },
            seeds = seeds,
            seedToSoil = ranges(groups[1]),
            soilToFertilizer = ranges(groups[2]),
            fertilizerToWater = ranges(groups[3]),
            waterToLight = ranges(groups[4]),
            lightToTemperature = ranges(groups[5]),
            temperatureToHumidity = ranges(groups[6]),
            humidityToLocation = ranges(groups[7]),
        )
    }

    fun part1(almanac: Almanac): Long {
        return almanac.seeds.minOf { seed ->
            val soil = almanac.seedToSoil.convert(seed)
            val fertilizer = almanac.soilToFertilizer.convert(soil)
            val water = almanac.fertilizerToWater.convert(fertilizer)
            val light = almanac.waterToLight.convert(water)
            val temperature = almanac.lightToTemperature.convert(light)
            val humidity = almanac.temperatureToHumidity.convert(temperature)
            almanac.humidityToLocation.convert(humidity)
        }
    }

    fun part2(almanac: Almanac): Long {
        val soil = almanac.seedToSoil.convert(almanac.seedRanges)
        val fertilizer = almanac.soilToFertilizer.convert(soil)
        val water = almanac.fertilizerToWater.convert(fertilizer)
        val light = almanac.waterToLight.convert(water)
        val temperature = almanac.lightToTemperature.convert(light)
        val humidity = almanac.temperatureToHumidity.convert(temperature)
        return almanac.humidityToLocation.convert(humidity).minOf { it.min }
    }

    val testInput = parseAlmanac(readText("Day05_test"))
    val input = parseAlmanac(readText("Day05"))

    check(part1(testInput) == 35L)
    part1(input).println()

    check(part2(testInput) == 46L)
    part2(input).println()
}

data class Almanac(
    val seedRanges: List<MinMax>,
    val seeds: List<Long> = listOf(),
    val seedToSoil: List<Range> = listOf(),
    val soilToFertilizer: List<Range> = listOf(),
    val fertilizerToWater: List<Range> = listOf(),
    val waterToLight: List<Range> = listOf(),
    val lightToTemperature: List<Range> = listOf(),
    val temperatureToHumidity: List<Range> = listOf(),
    val humidityToLocation: List<Range> = listOf(),
)

data class MinMax(
    val min: Long,
    val max: Long,
)

data class Range(private val string: String) {
    val dst: Long
    val src: Long
    val len: Long

    init {
        val (destination, source, length) = string.split(" ").map { n -> n.toLong() }
        dst = destination
        src = source
        len = length
    }
}
