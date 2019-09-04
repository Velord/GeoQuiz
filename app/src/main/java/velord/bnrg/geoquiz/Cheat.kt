package velord.bnrg.geoquiz

object Cheat {
    var isCheater = false
    var countCheatToken = 3

    val canCheat: () -> Boolean = {
        if (countCheatToken > 0) true
        else false
    }
}