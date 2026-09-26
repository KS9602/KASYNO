package com.example.MakaoService.controllers;

import com.example.MakaoService.dto.CreateMatchRequest;
import com.example.MakaoService.dto.JoinMatchRequest;
import com.example.MakaoService.dto.MatchListItemDTO;
import com.example.MakaoService.dto.MatchSnapshotDTO;
import com.example.MakaoService.security.AuthenticatedUser;
import com.example.MakaoService.services.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    private AuthenticatedUser authUser(Authentication authentication) {
        return (AuthenticatedUser) authentication.getPrincipal();
    }

    @GetMapping
    public List<MatchListItemDTO> listMatches() {
        return matchService.list();
    }

    @PostMapping
    public MatchSnapshotDTO createMatch(@RequestBody CreateMatchRequest request, Authentication authentication) {
        return matchService.create(request, authUser(authentication));
    }

    @PostMapping("/{matchId}/join")
    public MatchSnapshotDTO join(
            @PathVariable Long matchId,
            @RequestBody JoinMatchRequest request,
            Authentication authentication
    ) {
        return matchService.join(matchId, request, authUser(authentication));
    }

    @GetMapping("/{matchId}")
    public MatchSnapshotDTO getMatch(@PathVariable Long matchId) {
        return matchService.snapshot(matchId);
    }
}
