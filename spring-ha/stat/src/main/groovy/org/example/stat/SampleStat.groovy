package org.example.stat

import groovy.transform.ToString

/**
 * Статистические данные
 */
@SuppressWarnings('unused')
@ToString(includeNames = true)
class SampleStat {
    /** Минимальное значение */
    Double min

    /** Максимальное значение */
    Double max

    /** Суммарное значение */
    Double sum

    /** Кол-во значений */
    int count

    /** Средне арифметическое */
    Double avg

    /** Стандартное отклонение */
    Double stdev

    /** 10 прецентиль */
    Double centile10

    /** 25 прецентиль */
    Double centile25

    /** 50 прецентиль */
    Double centile50

    /** 68 прецентиль - 1 ая сигма */
    Double centile68

    /** 75 прецентиль */
    Double centile75

    /** 90 прецентиль */
    Double centile90

    /** 95 прецентиль - 2 ая сигма */
    Double centile95

    /** 99 прецентиль */
    Double centile99

    /** 99.73 прецентиль - 3 я сигма */
    Double centile9973
}
