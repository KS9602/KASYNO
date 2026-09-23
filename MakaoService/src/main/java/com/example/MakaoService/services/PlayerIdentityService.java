package com.example.MakaoService.services;

import com.example.MakaoService.entities.PlayerModel;
import com.example.MakaoService.repositories.PlayerRepository;
import com.example.MakaoService.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Jedyne miejsce w aplikacji tłumaczące tożsamość z JWT ({@link AuthenticatedUser#userId()},
 * czyli globalne id z AuthService) na wewnętrzną encję MakaoService ({@link PlayerModel}).
 * Żaden inny serwis nie powinien porównywać {@code userId()} bezpośrednio z {@code PlayerModel#getId()}.
 */
@Service
@RequiredArgsConstructor
public class PlayerIdentityService {

    private final PlayerRepository playerRepository;

    @Transactional
    public PlayerModel resolvePlayer(AuthenticatedUser user) {
        return playerRepository.findByGlobalUserId(user.userId())
                .orElseGet(() -> {
                    PlayerModel player = new PlayerModel();
                    player.setGlobalUserId(user.userId());
                    player.setUsername(user.username());
                    return playerRepository.saveAndFlush(player);
                });
    }
}
