package dev.johnoreilly.common.ui.extension

import dev.johnoreilly.common.model.Player

fun Player.extractTeamCode(): Triple<Long, Long, Long> {
    val regex = """t(\w+)@x2\.png""".toRegex()
    val matchResult = regex.find(teamPhotoUrl)
    val teamCode = try {
        matchResult?.groups?.get(1)?.value!!.toInt()
    } catch (e: Exception) {
        0
    }
    val backgroundColor = when (teamCode) {
        1 -> Pair(0xFFb40808, 0xFFea0c0c)
        3 -> Pair(0xFFbe000a, 0xFFff0203)
        4 -> Pair(0xFF231f20, 0xFF444041)
        6 -> Pair(0xFFFFFFFF, 0xFFe3e3e3)
        7 -> Pair(0xFF7d1142, 0xFFa6245f)
        8 -> Pair(0xFF1934be, 0xFF2145f6)
        11 -> Pair(0xFF00019e, 0xFF3334b1)
        13 -> Pair(0xFF0b56a4, 0xFF003090)
        14 -> Pair(0xFF911712, 0xFFdc0714)
        17 -> Pair(0xFFc2112e, 0xFFd93d56)
        20 -> Pair(0xFFfc2651, 0xFFc80028)
        21 -> Pair(0xFF6f2130, 0xFF963849)
        31 -> Pair(0xFF093466, 0xFF0d5dba)
        36 -> Pair(0xFF0054a6, 0xFF1471cc)
        39 -> Pair(0xFFffca5e, 0xFFf99808)
        40 -> Pair(0xFF0d4dde, 0xFF0333a0)
        43 -> Pair(0xFF6a9bc2, 0xFF98c5e9)
        54 -> Pair(0xFFFFFFFF, 0xFFe3e3e3)
        91 -> Pair(0xFFaf0c13, 0xFFdf1e26)
        94 -> Pair(0xFFc10000, 0xFFff1c24)
        else -> Pair(0xFF000000, 0xFF000000)
    }
    val textColor = if (backgroundColor.toList().contains(0xFFFFFFFF)) 0xFF35003A else 0xFFFFFFFF
    return Triple(backgroundColor.first, backgroundColor.second, textColor)
}
