package velord.bnrg.geoquiz

class IndexComputer(private val start: Int = 0,
                    private val end: Int,
                    var currentIndex : Int = 0) {

    private val size = end + 1

    private val fNext =  { step: Int ->
        currentIndex = (currentIndex + step) % size
        currentIndex
    }

    private val fPrev = { step: Int ->

        currentIndex = (currentIndex - step) % size
        if (currentIndex < 0)
            currentIndex = size + currentIndex
        currentIndex
    }

    fun next(step: Int = 1): Int = fNext(step)

    fun prev(step: Int = 1): Int = fPrev(step)

}