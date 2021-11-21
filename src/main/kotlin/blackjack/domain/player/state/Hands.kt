package blackjack.domain.player.state

import blackjack.domain.card.Card
import blackjack.domain.card.Score
import blackjack.error.DuplicatePlayingCardException

@JvmInline
value class Hands private constructor(val hands: List<Card>) {

    fun isEmpty(): Boolean = this == EMPTY
    fun isStart(): Boolean = hands.size == 2

    fun score(): Score = calculateAceScore(sumScore())

    private fun sumScore() = hands
        .map(Card::score)
        .reduce(Score::plus)

    private fun calculateAceScore(sum: Score): Score {
        if (hands.any(Card::hasAce) && sum.canPlusExtraAceScore()) {
            return sum + Score.EXTRA_ACE_SCORE
        }
        return sum
    }

    operator fun plus(extraCards: List<Card>): Hands {
        if (hands.any(extraCards::contains)) {
            throw DuplicatePlayingCardException()
        }
        return Hands(hands + extraCards)
    }

    companion object {
        var EMPTY: Hands = from(listOf())
        fun from(cards: List<Card>): Hands = Hands(cards.toList())
    }
}
