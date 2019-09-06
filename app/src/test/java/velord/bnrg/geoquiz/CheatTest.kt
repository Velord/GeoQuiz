package velord.bnrg.geoquiz

import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec
import velord.bnrg.geoquiz.model.Cheat

class CheatTest: StringSpec() {
    init {
        "canCheat" {
            forAll(Gen.Companion.choose(0, 10)) { _ ->
                --Cheat.countCheatToken
                if (Cheat.countCheatToken > 0)
                    Cheat.canCheat() == true
                else
                    Cheat.canCheat() == false
            }
        }
    }
}