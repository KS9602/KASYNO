package com.example.GameService.services;

import com.example.GameService.config.security.AuthenticatedUser;
import com.example.GameService.entities.CardModel;
import com.example.GameService.entities.GameModel;
import com.example.GameService.entities.RoomPlayerModel;
import com.example.GameService.enums.AttackType;
import com.example.GameService.enums.CardLocation;
import com.example.GameService.enums.CardSuit;
import com.example.GameService.enums.CardType;
import com.example.GameService.exceptions.rules.InvalidMoveException;
import com.example.GameService.exceptions.rules.NotThisPlayerTurnException;
import com.example.GameService.repositories.CardRepository;
import com.example.GameService.repositories.RoomPlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RulesServiceTest {

    @Mock
    private RoomPlayerRepository roomPlayerRepository;
    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private RulesService rulesService;

    private static final Long ROOM_ID = 1L;
    private static final Long GAME_ID = 100L;
    private static final Long PLAYER_A = 10L;
    private static final Long PLAYER_B = 20L;
    private static final Long PLAYER_C = 30L;

    private GameModel game;

    @BeforeEach
    void setUp() {
        game = new GameModel();
        game.setId(GAME_ID);
        game.setRoomId(ROOM_ID);
        game.setCurrentPlayerId(PLAYER_A);
    }

    private void twoPlayerRoom() {
        when(roomPlayerRepository.findAllByRoomId(ROOM_ID)).thenReturn(List.of(
                roomPlayer(PLAYER_A, 1),
                roomPlayer(PLAYER_B, 2)
        ));
    }

    private void threePlayerRoom() {
        when(roomPlayerRepository.findAllByRoomId(ROOM_ID)).thenReturn(List.of(
                roomPlayer(PLAYER_A, 1),
                roomPlayer(PLAYER_B, 2),
                roomPlayer(PLAYER_C, 3)
        ));
    }

    private static RoomPlayerModel roomPlayer(Long playerId, int minutePozycji) {
        RoomPlayerModel rp = new RoomPlayerModel();
        rp.setRoomId(ROOM_ID);
        rp.setPlayerId(playerId);
        rp.setJoinedAt(LocalDateTime.of(2024, 1, 1, 0, minutePozycji));
        return rp;
    }

    private static CardModel card(CardType type, CardSuit suit) {
        CardModel c = new CardModel();
        c.setType(type);
        c.setSuit(suit);
        return c;
    }


    @Test
    void checkTurn_rzucaWyjatekGdyToNieTwojaTura() {
        AuthenticatedUser intruz = new AuthenticatedUser(PLAYER_B, "intruz");

        assertThrows(NotThisPlayerTurnException.class,
                () -> rulesService.checkTurn(game, intruz));
    }

    @Test
    void checkTurn_nicNieRobiGdyToTwojaTura() {
        AuthenticatedUser wlasciciel = new AuthenticatedUser(PLAYER_A, "gracz");

        assertDoesNotThrow(() -> rulesService.checkTurn(game, wlasciciel));
    }


    @Test
    void amountToDraw_zwracaWlasciwaLiczbeKartWZaleznosciOdAktywnegoAtaku() {
        game.setAttackType(AttackType.NONE);
        assertThat(rulesService.amountToDraw(game)).isEqualTo(1);

        game.setAttackType(AttackType.FOUR);
        game.setAttackAmount(3);
        assertThat(rulesService.amountToDraw(game)).isEqualTo(0);

        game.setAttackType(AttackType.TWO);
        game.setAttackAmount(4);
        assertThat(rulesService.amountToDraw(game)).isEqualTo(4);
    }


    @Test
    void playSequence_zwyklaKartaDopasowanaPoKolorzeAktualizujeStolIPrzesuwaTure() {
        twoPlayerRoom();
        game.setLastRank(CardType.SEVEN);
        game.setLastSuit(CardSuit.HEARTS);

        CardModel szostkaKier = card(CardType.SIX, CardSuit.HEARTS);

        rulesService.playSequence(game, List.of(szostkaKier), ROOM_ID, PLAYER_A, null, null);

        assertThat(game.getLastRank()).isEqualTo(CardType.SIX);
        assertThat(game.getLastSuit()).isEqualTo(CardSuit.HEARTS);
        assertThat(game.getCurrentPlayerId()).isEqualTo(PLAYER_B);
    }

    @Test
    void playSequence_zagranieDwojkiOtwieraAtakTypuTwo() {
        twoPlayerRoom();

        CardModel dwojkaPik = card(CardType.TWO, CardSuit.SPADES);

        rulesService.playSequence(game, List.of(dwojkaPik), ROOM_ID, PLAYER_A, null, null);

        assertThat(game.getAttackType()).isEqualTo(AttackType.TWO);
        assertThat(game.getAttackAmount()).isEqualTo(2);
        assertThat(game.getAttackSuit()).isEqualTo(CardSuit.SPADES);
    }

    @Test
    void playSequence_dolozenieTrojkiDoAtakuDwojkiSumujeKare() {
        twoPlayerRoom();
        game.setAttackType(AttackType.TWO);
        game.setAttackAmount(2);
        game.setAttackSuit(CardSuit.HEARTS);

        CardModel trojkaKier = card(CardType.THREE, CardSuit.HEARTS);

        rulesService.playSequence(game, List.of(trojkaKier), ROOM_ID, PLAYER_A, null, null);

        assertThat(game.getAttackType()).isEqualTo(AttackType.THREE);
        assertThat(game.getAttackAmount()).isEqualTo(5);
    }

    @Test
    void playSequence_kartaNieBroniacaPrzedAtakiemRzucaWyjatek() {
        game.setAttackType(AttackType.TWO);
        game.setAttackAmount(2);
        game.setAttackSuit(CardSuit.HEARTS);

        CardModel piatkaKier = card(CardType.FIVE, CardSuit.HEARTS);

        assertThrows(InvalidMoveException.class,
                () -> rulesService.playSequence(game, List.of(piatkaKier), ROOM_ID, PLAYER_A, null, null));
    }

    @Test
    void playSequence_damaNieBroniPrzedAktywnymAtakiem() {
        game.setAttackType(AttackType.TWO);
        game.setAttackAmount(2);
        game.setAttackSuit(CardSuit.HEARTS);

        CardModel damaPik = card(CardType.QUEEN, CardSuit.SPADES);

        assertThrows(InvalidMoveException.class,
                () -> rulesService.playSequence(game, List.of(damaPik), ROOM_ID, PLAYER_A, null, null));
    }

    @Test
    void playSequence_graczBezCzworkiJestOdRazuPomijanyAAtakCzyszczonyJednymSkokiem() {
        threePlayerRoom();
        when(cardRepository.findAllByPlayerIdAndGameIdAndLocation(PLAYER_B, GAME_ID, CardLocation.HAND))
                .thenReturn(List.of());

        CardModel czworkaKier = card(CardType.FOUR, CardSuit.HEARTS);

        rulesService.playSequence(game, List.of(czworkaKier), ROOM_ID, PLAYER_A, null, null);

        assertThat(game.getAttackType()).isEqualTo(AttackType.NONE);
        assertThat(game.getAttackAmount()).isEqualTo(0);
        assertThat(game.getCurrentPlayerId()).isEqualTo(PLAYER_C);
    }
}
