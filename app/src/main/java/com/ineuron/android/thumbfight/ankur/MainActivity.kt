package com.ineuron.android.thumbfight.ankur

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.ineuron.android.thumbfight.ankur.databinding.ActivityMainBinding
import com.ineuron.android.thumbfight.ankur.game.GameManager
import com.ineuron.android.thumbfight.ankur.game.GameManagerCallback
import com.ineuron.android.thumbfight.ankur.game.Player

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), GameManagerCallback {

    private lateinit var binding: ActivityMainBinding
    lateinit var gameManager: GameManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gameManager = GameManager()
        gameManager.callback = this

        if (IConstants.LOG_ENABLED) {
            Log.d(TAG, "onCreate")
        }
    }

    fun onStartGameButtonClicked(view: View) {
        gameManager.startGame()
    }

    fun onLeftThumbButtonClicked(view: View) {
        gameManager.clickReceived(Player.LEFT)
    }

    fun onRightThumbButtonClicked(view: View) {
        gameManager.clickReceived(Player.RIGHT)
    }

    // start of callbacks

    override fun timeChanged(secsLeft: Int) {
        binding.timeLeftTextView.text = getString(R.string.time_left_text, secsLeft)
    }

    override fun setPlayer(player: Player) {
        binding.playerTextView.text = player.name
    }

    override fun scoreChanged(player: Player, score: Int) {
        if (player == Player.LEFT) {
            binding.leftScoreTextView.text = getString(R.string.score_text, score)
        } else {
            binding.rightScoreTextView.text = getString(R.string.score_text, score)
        }
    }

    override fun showWinner(winners: Array<Player>) {
        var message = getString(R.string.tie_message)
        if (winners.size == 1) {
            if (winners.contains(Player.LEFT)) {
                message = getString(R.string.winner_message, Player.LEFT.name)
            } else {
                message = getString(R.string.winner_message, Player.RIGHT.name)
            }
        }

        AlertDialog.Builder(this)
                .setTitle(R.string.game_over_text)
                .setMessage(message)
                .setPositiveButton(R.string.ok_button, null)
                .show()
    }
}