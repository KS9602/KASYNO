package com.example.MakaoService.services;

import com.example.MakaoService.dto.CreateMatchRequest;
import com.example.MakaoService.dto.JoinMatchRequest;
import com.example.MakaoService.dto.MatchListItemDTO;
import com.example.MakaoService.dto.MatchSnapshotDTO;
import com.example.MakaoService.dto.PlayerSummaryDTO;
import com.example.MakaoService.entities.MatchModel;
import com.example.MakaoService.entities.MatchPlayerModel;
import com.example.MakaoService.entities.MatchStatus;
import com.example.MakaoService.entities.PlayerModel;
import com.example.MakaoService.exceptions.IllegalActionException;
import com.example.MakaoService.exceptions.InvalidMatchPasswordException;
import com.example.MakaoService.exceptions.MatchNotFoundException;
import com.example.MakaoService.exceptions.PlayerAlreadyInMatchException;
import com.example.MakaoService.repositories.MatchPlayerRepository;
import com.example.MakaoService.repositories.MatchRepository;
import com.example.MakaoService.repositories.PlayerRepository;
import com.example.MakaoService.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private static final int DEFAULT_MAX_PLAYERS = 4;

    private final MatchRepository matchRepository;
    private final MatchPlayerRepository matchPlayerRepository;
    private final PlayerRepository playerRepository;
    private final PlayerIdentityService playerIdentityService;
    private final PasswordEncoder passwordEncoder;

    public List<MatchListItemDTO> list() {
        return matchRepository.findAllByStatusOrderByIdDesc(MatchStatus.WAITING).stream()
                .map(match -> new MatchListItemDTO(
                        match.getId(),
                        match.getName(),
                        match.getStatus(),
                        (int) matchPlayerRepository.countByMatchId(match.getId()),
                        match.getMaxPlayers()
                ))
                .toList();
    }

    @Transactional
    public MatchSnapshotDTO create(CreateMatchRequest request, AuthenticatedUser user) {
        PlayerModel creator = playerIdentityService.resolvePlayer(user);

        MatchModel match = new MatchModel();
        match.setName(request.name());
        match.setPasswordHash(passwordEncoder.encode(request.password()));
        match.setMaxPlayers(request.maxPlayers() != null ? request.maxPlayers() : DEFAULT_MAX_PLAYERS);
        match.setStatus(MatchStatus.WAITING);
        match.setCreatedBy(creator.getId());
        matchRepository.save(match);

        addPlayer(match.getId(), creator.getId());

        return snapshot(match.getId());
    }

    @Transactional
    public MatchSnapshotDTO join(Long matchId, JoinMatchRequest request, AuthenticatedUser user) {
        MatchModel match = getMatch(matchId);
        PlayerModel player = playerIdentityService.resolvePlayer(user);

        if (match.getStatus() != MatchStatus.WAITING) {
            throw new IllegalActionException("Rozgrywka już się rozpoczęła");
        }
        if (!passwordEncoder.matches(request.password(), match.getPasswordHash())) {
            throw new InvalidMatchPasswordException("Nieprawidłowe hasło");
        }
        if (matchPlayerRepository.findByMatchIdAndPlayerId(matchId, player.getId()).isPresent()) {
            throw new PlayerAlreadyInMatchException("Już dołączyłeś do tej rozgrywki");
        }
        if (matchPlayerRepository.countByMatchId(matchId) >= match.getMaxPlayers()) {
            throw new IllegalActionException("Rozgrywka jest pełna");
        }

        addPlayer(matchId, player.getId());

        return snapshot(matchId);
    }

    public MatchSnapshotDTO snapshot(Long matchId) {
        MatchModel match = getMatch(matchId);
        List<PlayerSummaryDTO> players = matchPlayerRepository.findAllByMatchId(matchId).stream()
                .map(matchPlayer -> playerRepository.findById(matchPlayer.getPlayerId())
                        .orElseThrow(() -> new IllegalStateException("Rozgrywka zawiera gracza bez encji Player — niespójne dane")))
                .map(player -> new PlayerSummaryDTO(player.getId(), player.getUsername()))
                .toList();
        return new MatchSnapshotDTO(match.getId(), match.getName(), match.getStatus(), match.getMaxPlayers(), players);
    }

    private MatchModel getMatch(Long matchId) {
        return matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException("Nie znaleziono rozgrywki"));
    }

    private void addPlayer(Long matchId, Long playerId) {
        MatchPlayerModel matchPlayer = new MatchPlayerModel();
        matchPlayer.setMatchId(matchId);
        matchPlayer.setPlayerId(playerId);
        matchPlayer.setSeatNo((int) matchPlayerRepository.countByMatchId(matchId));
        matchPlayer.setJoinedAt(Instant.now());
        matchPlayerRepository.save(matchPlayer);
    }
}
