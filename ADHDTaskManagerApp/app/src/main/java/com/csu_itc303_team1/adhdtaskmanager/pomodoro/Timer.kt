package com.csu_itc303_team1.adhdtaskmanager.pomodoro

class Timer {

    private var hourToCount: Int = 0
    private var minToCount: Int = 0
    var secondToCount: Int = 0

    var isCounting = false
    var isPause = false
    var workState = WorkState.Work
    var breakTimer: Int = 0
    var workTimer: Int = 0

    fun loadBreakTimer() {
        hourToCount = 0
        minToCount = breakTimer
        secondToCount = 0
    }
    fun loadWorkTimer() {
        hourToCount = 0
        minToCount = workTimer
        secondToCount = 0
    }
    fun displayTime(): String {
        var result = ""

        var hr = hourToCount.toString()
        var min = minToCount.toString()
        var sec = secondToCount.toString()

        if (hourToCount > 0) {
            result += (if (hr.length < 2) hr.padStart(2, '0') else hr) + ":"
        }

        result += (if (min.length < 2) min.padStart(2, '0') else min) + ":"
        result += if (sec.length < 2) sec.padStart(2, '0') else sec

        return result
    }
    fun toSeconds(): Long {
        return hourToCount.toLong() * 60 * 60 + minToCount.toLong() * 60 + secondToCount.toLong()
    }
    fun restoreFromSeconds(s: Long) {
        secondToCount = (s % 60).toInt()
        minToCount = (((s-secondToCount)/60) % 60).toInt()
        hourToCount = ((s-secondToCount-minToCount*60)/3600).toInt()
    }
    fun minusOneSecond() {
        if (hourToCount == 0 && minToCount == 0 && secondToCount <= 1) {
            secondToCount = 0
            isCounting = false
            return
        }
        if (secondToCount == 0 && minToCount > 0) {
            secondToCount = 60
            minToCount--
        } else if (secondToCount == 0 && minToCount == 0) {
            secondToCount = 60
            minToCount = 60
            hourToCount--
        }
        secondToCount--
    }
    fun resetTimer(){
        isCounting = false
        isPause = false
        //reset timer, set timer back to work stat
        workState = WorkState.Work
        secondToCount=0
        minToCount= workTimer
        hourToCount=0
    }
    fun initTimer(minValue: Int, secondValue: Int) {
        isCounting = false
        isPause = true
        //reset timer, set timer back to work stat
        workState = WorkState.Work
        secondToCount=secondValue
        minToCount= minValue
        hourToCount=0
    }

}