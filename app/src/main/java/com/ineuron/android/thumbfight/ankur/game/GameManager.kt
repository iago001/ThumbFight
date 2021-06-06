package com.ineuron.android.thumbfight.ankur.game

import android.os.CountDownTimer

class GameManager {

    var currentPlayer = Player.LEFT
    var timeRemaining = 0
    var timer: CountDownTimer? = null
    var scoreMap: HashMap<Player, Int> = HashMap()
    var callback: GameManagerCallback? = null

    fun startGame() {
        // reset time and current player
        timeRemaining = 10
        currentPlayer = Player.LEFT

        // reset the timer
        timer = object: CountDownTimer(timeRemaining * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var timeLeft = (millisUntilFinished / 1000).toInt()
                callback?.timeChanged(timeLeft)
                processEvent(timeLeft)
            }

            override fun onFinish() {

            }
        }

        // reset the score
        scoreMap[Player.LEFT] = 0
        scoreMap[Player.RIGHT] = 0

        // inform the UI
        callback?.setPlayer(Player.LEFT)
        callback?.timeChanged(timeRemaining)
        callback?.scoreChanged(Player.LEFT, 0)
        callback?.scoreChanged(Player.RIGHT, 0)

        // start the timer
        timer?.start()
    }

    fun clickReceived(player: Player) {
        if (currentPlayer == player) {
            if (scoreMap[player] != null) {
                scoreMap[player] = scoreMap[player]?.plus(1) ?: 1
            }

            callback?.scoreChanged(Player.LEFT, scoreMap[Player.LEFT] ?: 0)
            callback?.scoreChanged(Player.RIGHT, scoreMap[Player.RIGHT] ?: 0)
        }
    }

    fun processEvent(timeLeft: Int) {
        //
        if (timeLeft <= 5 && timeLeft > 0) {
            currentPlayer = Player.RIGHT
            callback?.setPlayer(Player.RIGHT)
        }

        // game over
        if (timeLeft < 1) {
            val leftPlayerScore = scoreMap[Player.LEFT] ?: 0
            val rightPlayerScore = scoreMap[Player.RIGHT] ?: 0

            if (leftPlayerScore > rightPlayerScore) {
                callback?.showWinner(arrayOf(Player.LEFT))
            } else if (rightPlayerScore > leftPlayerScore) {
                callback?.showWinner(arrayOf(Player.RIGHT))
            } else {
                callback?.showWinner(arrayOf(Player.RIGHT, Player.LEFT))
            }
        }
    }

}

interface GameManagerCallback {
    fun timeChanged(secsLeft: Int)
    fun setPlayer(player: Player)
    fun scoreChanged(player: Player, score: Int)
    fun showWinner(winners: Array<Player>)
}

enum class Player {
    LEFT, RIGHT
}