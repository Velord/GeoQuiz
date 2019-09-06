package velord.bnrg.geoquiz

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec
import velord.bnrg.geoquiz.model.Direction
import velord.bnrg.geoquiz.model.IndexComputer

class IndexComputerTest: StringSpec() {

    init {
        "compute" {
            forAll(Gen.Companion.choose(-100, 100)) { step: Int ->
                val indexComputer = IndexComputer(0, 20)
                val indexNext: (Int) -> Int = { n: Int ->
                    indexComputer.compute(Direction.NEXT, n) }
                val indexPrev: (Int) -> Int  = { n: Int ->
                    indexComputer.compute(Direction.PREV, n) }

                val next = { indexNext(step) }
                val prev = { indexPrev(step) }

                next() == ( prev() + next() - prev() )
            }
        }
    }
}