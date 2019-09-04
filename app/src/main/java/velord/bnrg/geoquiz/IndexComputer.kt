package velord.bnrg.geoquiz

class IndexComputer(private val start: Int = 0,
                    private val end: Int,
                    var currentIndex : Int = start) {

    private val size = end + 1

    private val fNext =  { step: Int ->
        currentIndex = (currentIndex + step) % size
        currentIndex
    }

    private val fPrev = { step: Int ->
        currentIndex = (currentIndex - step) % size
        if (currentIndex < 0)
            currentIndex += size
        currentIndex
    }

    fun compute(direction: Direction, step: Int = 1) =
        when(direction) {
            Direction.NEXT -> fNext(step)
            Direction.PREV -> fPrev(step)
            else -> throw IllegalArgumentException()
        }

}

enum class Direction {
    NEXT, PREV
}

