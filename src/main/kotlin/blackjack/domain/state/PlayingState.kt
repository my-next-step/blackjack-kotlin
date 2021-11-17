package blackjack.domain.state

sealed class PlayingState {
    abstract fun isFinish(): Boolean

    companion object {
        fun of(nextDraw: Boolean) = if (nextDraw) Running else End
    }
}

object Running : PlayingState() {
    override fun isFinish(): Boolean = false
}

object End : PlayingState() {
    override fun isFinish(): Boolean = true
}
