package blackjack.domain.player

import blackjack.domain.card.Card
import blackjack.domain.player.state.End
import blackjack.domain.player.state.PlayingState
import blackjack.domain.player.state.Running

class Player(
    private val _name: Name,
    val hands: Hands = Hands.initialize(),
    private val playingState: PlayingState = Running,
) {
    val name: String
        get() = _name.name

    fun addPlayingCards(extraCards: List<Card>): Player {
        val addedHands: Hands = hands.plus(extraCards)
        val sumScore = addedHands.score()
        if (sumScore.isOverBlackJack()) {
            return Player(_name, addedHands, End)
        }
        return Player(_name, addedHands, playingState)
    }

    fun isFinished(): Boolean = playingState.isFinish()

    fun continuePlay(command: String): Player = continuePlay(Command.values(command))

    private fun continuePlay(command: Command): Player = Player(_name, hands, PlayingState.of(command.nextDraw))

    companion object {
        fun fromName(name: String): Player = Player(Name(name))
    }
}
