package blackjack.ui

import blackjack.domain.bet.Bets
import blackjack.domain.card.Card
import blackjack.domain.card.Denomination
import blackjack.domain.card.Suit
import blackjack.domain.player.Player
import blackjack.domain.player.Players
import blackjack.domain.player.state.hands.Hands
import blackjack.strategy.ui.output.OutputStrategy
import blackjack.util.FavoriteStringFixture.BLANK
import blackjack.util.FavoriteStringFixture.COMMA_AND_ONE_SPACE
import blackjack.util.FavoriteStringFixture.NEW_LINE

class ResultView(private val outputStrategy: OutputStrategy) {

    fun showReadiedPlayers(dealer: Player, players: Players) {
        outputStrategy.execute(
            READIED_PLAYERS_INFORMATION.format(dealer.name.name, playerNameJoinToString(players))
        )
        outputStrategy.execute(formatPlayerHandsInformation(dealer))
        outputStrategy.execute(playerAndHandsJoinToString(players))
        outputStrategy.execute(BLANK)
    }

    private fun playerNameJoinToString(players: Players): String =
        players.players.joinToString(COMMA_AND_ONE_SPACE) { it.name.name }

    private fun playerAndHandsJoinToString(players: Players): String =
        players.players.joinToString(NEW_LINE) { formatPlayerHandsInformation(it) }

    fun showPlayerHands(player: Player) {
        outputStrategy.execute(
            HANDS_INFORMATION.format(player.name.name, handsJoinToString(player.state.hands))
        )
        outputStrategy.execute(BLANK)
    }

    private fun formatPlayerHandsInformation(player: Player) =
        HANDS_INFORMATION.format(player.name.name, handsJoinToString(player.state.hands))

    private fun handsJoinToString(hands: Hands): String =
        hands.hands.joinToString(COMMA_AND_ONE_SPACE) { cardJoinToString(it) }

    private fun cardJoinToString(card: Card): String =
        denominationName(card.denomination) + suitName(card.suit)

    fun noticeDealerDraw() {
        outputStrategy.execute(DEALER_DRAW_CARD)
        outputStrategy.execute(BLANK)
    }

    fun showEndedPlayers(dealer: Player, players: Players) {
        outputStrategy.execute(formatHandsInformationAndScore(dealer))
        outputStrategy.execute(handsInformationAndScoreJoinToString(players))
        outputStrategy.execute(BLANK)
    }

    private fun handsInformationAndScoreJoinToString(endedGamePlayer: Players): String =
        endedGamePlayer.players.joinToString(NEW_LINE) { formatHandsInformationAndScore(it) }

    private fun formatHandsInformationAndScore(player: Player): String =
        HANDS_INFORMATION_AND_SCORE.format(
            player.name.name,
            handsJoinToString(player.state.hands),
            player.state.hands.score().score
        )

    fun showProfitResult(dealer: Player, players: Players, bets: Bets) {
        outputStrategy.execute(PLAYERS_PROFIT_RESULT_INTRODUCE)
        outputStrategy.execute(
            PLAYER_PROFIT_RESULT.format(
                dealer.name.name,
                dealerMatchResultJoinToString(players, dealer, bets)
            )
        )
        outputStrategy.execute(gamePlayersMatchResultJoinToString(players, dealer, bets))
        outputStrategy.execute(BLANK)
    }

    private fun gamePlayersMatchResultJoinToString(gamers: Players, dealer: Player, bets: Bets): String =
        gamers.players
            .associateWith { it.profit(dealer, bets.betMoney(it.name)) }
            .toList()
            .joinToString(NEW_LINE) { PLAYER_PROFIT_RESULT.format(it.first.name.name, it.second.toInt()) }

    private fun dealerMatchResultJoinToString(gamers: Players, dealer: Player, bets: Bets): Int =
        gamers.players
            .map { dealer.profit(it, bets.betMoney(it.name)) }
            .reduce(Double::plus)
            .toInt()

    private fun denominationName(denomination: Denomination): String {
        return when (denomination) {
            Denomination.ACE -> "A"
            Denomination.TWO -> "2"
            Denomination.THREE -> "3"
            Denomination.FOUR -> "4"
            Denomination.FIVE -> "5"
            Denomination.SIX -> "6"
            Denomination.SEVEN -> "7"
            Denomination.EIGHT -> "8"
            Denomination.NINE -> "9"
            Denomination.TEN -> "10"
            Denomination.JACK -> "J"
            Denomination.QUEEN -> "Q"
            Denomination.KING -> "K"
        }
    }

    private fun suitName(suit: Suit): String {
        return when (suit) {
            Suit.CLUB -> "??????"
            Suit.DIAMOND -> "???????????????"
            Suit.HEART -> "??????"
            Suit.SPADE -> "????????????"
        }
    }

    companion object {
        private const val READIED_PLAYERS_INFORMATION = "%s??? %s?????? 2?????? ????????? ??????????????????."
        private const val HANDS_INFORMATION = "%s??????: %s"
        private const val HANDS_INFORMATION_AND_SCORE = "%s??????: %s - ??????: %s"
        private const val DEALER_DRAW_CARD = "????????? 16????????? ????????? ????????? ??? ???????????????."
        private const val PLAYERS_PROFIT_RESULT_INTRODUCE = "## ?????? ??????"
        private const val PLAYER_PROFIT_RESULT = "%s: %s"
    }
}
