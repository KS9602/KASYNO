package com.example.GameService.services;

import com.example.GameService.config.security.AuthenticatedUser;
import com.example.GameService.entities.CardModel;
import com.example.GameService.entities.GameModel;
import com.example.GameService.enums.AttackType;
import com.example.GameService.enums.CardSuit;
import com.example.GameService.enums.CardType;
import com.example.GameService.enums.CardLocation;
import com.example.GameService.exceptions.rules.InvalidMoveException;
import com.example.GameService.exceptions.rules.NotThisPlayerTurnException;
import com.example.GameService.repositories.CardRepository;
import com.example.GameService.repositories.RoomPlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RulesService {

    private final RoomPlayerRepository roomPlayerRepository;
    private final CardRepository cardRepository;

    public void checkTurn(GameModel game, AuthenticatedUser player) {
        if (!game.getCurrentPlayerId().equals(player.playerId())) {
            throw new NotThisPlayerTurnException("Not your turn");
        }
    }

    public boolean isPlayable(GameModel game, CardModel card) {
        try {
            checkFirstCardDef(game, card);
            return true;
        } catch (InvalidMoveException e) {
            return false;
        }
    }

    public int amountToDraw(GameModel game) {
        return switch (game.getAttackType()) {
            case TWO, THREE, KING_HEARTS, KING_SPADES -> game.getAttackAmount();
            case FOUR -> 0;
            case NONE -> 1;
        };
    }

    public void playSequence(
            GameModel game,
            List<CardModel> cards,
            Long roomId,
            Long playerId,
            CardType chosenRank,
            CardSuit chosenSuit
    ) {
        if (cards.isEmpty()) {
            throw new InvalidMoveException("No cards to play");
        }

        if (game.getDrawnCardId() != null
                && (cards.size() != 1 || !cards.get(0).getId().equals(game.getDrawnCardId()))) {
            throw new InvalidMoveException("You already drew this turn - you can only play the drawn card");
        }

        checkFirstCardDef(game, cards.get(0));

        for (int i = 1; i < cards.size(); i++) {
            if (!continuesChain(cards.get(i - 1), cards.get(i))) {
                throw new InvalidMoveException("Cards don't form a valid sequence");
            }
        }

        for (int i = 0; i < cards.size(); i++) {
            boolean isLast = i == cards.size() - 1;
            applyEffect(game, cards.get(i), roomId, isLast ? chosenRank : null, isLast ? chosenSuit : null);
        }

        CardModel last = cards.get(cards.size() - 1);
        if (last.getType() == CardType.QUEEN) {
            game.setLastRank(null);
            game.setLastSuit(null);
        } else {
            game.setLastRank(last.getType());
            game.setLastSuit(last.getSuit());
        }

        boolean justSetRequest = (last.getType() == CardType.JACK && chosenRank != null)
                || last.getType() == CardType.ACE;

        advanceTurnAfterPlay(game, roomId, playerId, justSetRequest);
    }

    public void resolveAfterDraw(GameModel game, Long roomId, Long playerId) {
        if (game.getAttackType() == AttackType.FOUR) {
            resolveFourSkip(game, roomId, playerId);
            autoResolveForcedSkips(game, roomId);
            return;
        }

        game.setAttackType(AttackType.NONE);
        game.setAttackAmount(0);
        game.setAttackSuit(null);

        decrementRequest(game);

        if (game.getTurnReturnsTo() != null) {
            game.setCurrentPlayerId(game.getTurnReturnsTo());
            game.setTurnReturnsTo(null);
        } else {
            game.setCurrentPlayerId(nextPlayerId(roomId, playerId));
        }

        autoResolveForcedSkips(game, roomId);
    }

    public Long nextPlayerId(Long roomId, Long currentPlayerId) {
        List<Long> order = orderedPlayerIds(roomId);
        int idx = order.indexOf(currentPlayerId);
        return order.get((idx + 1) % order.size());
    }

    public Long previousPlayerId(Long roomId, Long currentPlayerId) {
        List<Long> order = orderedPlayerIds(roomId);
        int idx = order.indexOf(currentPlayerId);
        return order.get((idx - 1 + order.size()) % order.size());
    }

    private List<Long> orderedPlayerIds(Long roomId) {
        return roomPlayerRepository.findAllByRoomId(roomId).stream()
                .sorted((a, b) -> a.getJoinedAt().compareTo(b.getJoinedAt()))
                .map(rp -> rp.getPlayerId())
                .toList();
    }

    private void checkFirstCardDef(GameModel game, CardModel card) {
        if (game.getAttackType() != AttackType.NONE) {
            boolean valid = switch (game.getAttackType()) {
                case TWO -> card.getType() == CardType.TWO
                        || (card.getType() == CardType.THREE && card.getSuit() == game.getAttackSuit());
                case THREE -> card.getType() == CardType.THREE
                        || (card.getType() == CardType.TWO && card.getSuit() == game.getAttackSuit());
                case FOUR -> card.getType() == CardType.FOUR;
                case KING_HEARTS, KING_SPADES -> card.getType() == CardType.KING
                        && (card.getSuit() == CardSuit.DIAMONDS || card.getSuit() == CardSuit.CLUBS);
                case NONE -> true;
            };
            if (!valid) {
                throw new InvalidMoveException("This card doesn't defend against the active attack");
            }
            return;
        }

        if (game.getRequestedRank() != null) {
            if (card.getType() == CardType.JACK) {
                return;
            }
            if (card.getType() != game.getRequestedRank()) {
                throw new InvalidMoveException("You must play the requested card: " + game.getRequestedRank());
            }
            return;
        }

        if (game.getRequestedSuit() != null) {
            if (card.getType() == CardType.ACE) {
                return;
            }
            if (card.getSuit() != game.getRequestedSuit()) {
                throw new InvalidMoveException("You must play the requested suit: " + game.getRequestedSuit());
            }
            return;
        }

        if (!matches(card, game.getLastRank(), game.getLastSuit())) {
            throw new InvalidMoveException("This card doesn't match the table");
        }
    }

    private boolean matches(CardModel card, CardType lastRank, CardSuit lastSuit) {
        if (card.getType() == CardType.QUEEN) {
            return true;
        }
        if (lastRank == null && lastSuit == null) {
            return true;
        }
        return card.getType() == lastRank || card.getSuit() == lastSuit;
    }

    private boolean continuesChain(CardModel previous, CardModel card) {
        if (previous.getType() == CardType.QUEEN || card.getType() == CardType.QUEEN) {
            return true;
        }
        if (card.getType() == previous.getType()) {
            return true;
        }
        return card.getSuit() == previous.getSuit()
                && Math.abs(card.getType().ordinal() - previous.getType().ordinal()) == 1;
    }

    private void applyEffect(GameModel game, CardModel card, Long roomId, CardType chosenRank, CardSuit chosenSuit) {
        switch (card.getType()) {
            case TWO -> {
                boolean chained = game.getAttackType() == AttackType.TWO || game.getAttackType() == AttackType.THREE;
                game.setAttackType(AttackType.TWO);
                game.setAttackAmount((chained ? game.getAttackAmount() : 0) + 2);
                game.setAttackSuit(card.getSuit());
            }
            case THREE -> {
                boolean chained = game.getAttackType() == AttackType.TWO || game.getAttackType() == AttackType.THREE;
                game.setAttackType(AttackType.THREE);
                game.setAttackAmount((chained ? game.getAttackAmount() : 0) + 3);
                game.setAttackSuit(card.getSuit());
            }
            case FOUR -> {
                boolean chained = game.getAttackType() == AttackType.FOUR;
                game.setAttackType(AttackType.FOUR);
                game.setAttackAmount((chained ? game.getAttackAmount() : 0) + 1);
                game.setAttackSuit(null);
            }
            case KING -> {
                if (card.getSuit() == CardSuit.HEARTS) {
                    game.setAttackType(AttackType.KING_HEARTS);
                    game.setAttackAmount(5);
                    game.setAttackSuit(null);
                } else if (card.getSuit() == CardSuit.SPADES) {
                    game.setAttackType(AttackType.KING_SPADES);
                    game.setAttackAmount(5);
                    game.setAttackSuit(null);
                } else if (game.getAttackType() == AttackType.KING_HEARTS
                        || game.getAttackType() == AttackType.KING_SPADES) {
                    game.setAttackType(AttackType.NONE);
                    game.setAttackAmount(0);
                }
            }
            case JACK -> {
                game.setRequestedRank(chosenRank);
                game.setRequestRemainingTurns(Math.max(countPlayers(roomId) - 1, 0));
            }
            case ACE -> {
                game.setRequestedSuit(chosenSuit != null ? chosenSuit : card.getSuit());
                game.setRequestRemainingTurns(Math.max(countPlayers(roomId) - 1, 0));
            }
            default -> {
            }
        }
    }

    private void advanceTurnAfterPlay(GameModel game, Long roomId, Long playerId, boolean justSetRequest) {
        game.setDrawnCardId(null);

        if (!justSetRequest) {
            decrementRequest(game);
        }

        if (game.getAttackType() == AttackType.KING_SPADES && countPlayers(roomId) > 2) {
            game.setTurnReturnsTo(nextPlayerId(roomId, playerId));
            game.setCurrentPlayerId(previousPlayerId(roomId, playerId));
            autoResolveForcedSkips(game, roomId);
            return;
        }

        if (game.getTurnReturnsTo() != null) {
            game.setCurrentPlayerId(game.getTurnReturnsTo());
            game.setTurnReturnsTo(null);
        } else {
            game.setCurrentPlayerId(nextPlayerId(roomId, playerId));
        }

        autoResolveForcedSkips(game, roomId);
    }

    private void resolveFourSkip(GameModel game, Long roomId, Long playerId) {
        int skips = game.getAttackAmount();
        Long current = playerId;
        for (int i = 0; i < skips; i++) {
            current = nextPlayerId(roomId, current);
        }
        game.setAttackType(AttackType.NONE);
        game.setAttackAmount(0);
        game.setCurrentPlayerId(current);
    }

    private void autoResolveForcedSkips(GameModel game, Long roomId) {
        if (game.getAttackType() == AttackType.FOUR && !hasFour(game.getId(), game.getCurrentPlayerId())) {
            resolveFourSkip(game, roomId, game.getCurrentPlayerId());
            autoResolveForcedSkips(game, roomId);
        }
    }

    private boolean hasFour(Long gameId, Long playerId) {
        return cardRepository.findAllByPlayerIdAndGameIdAndLocation(playerId, gameId, CardLocation.HAND)
                .stream()
                .anyMatch(c -> c.getType() == CardType.FOUR);
    }

    private void decrementRequest(GameModel game) {
        if (game.getRequestedRank() == null && game.getRequestedSuit() == null) {
            return;
        }
        int remaining = game.getRequestRemainingTurns() - 1;
        if (remaining <= 0) {
            game.setRequestedRank(null);
            game.setRequestedSuit(null);
            game.setRequestRemainingTurns(0);
        } else {
            game.setRequestRemainingTurns(remaining);
        }
    }

    private int countPlayers(Long roomId) {
        return (int) roomPlayerRepository.countByRoomId(roomId);
    }
}
