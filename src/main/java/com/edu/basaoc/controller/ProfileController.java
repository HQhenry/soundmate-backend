package com.edu.basaoc.controller;

import com.edu.basaoc.model.ProfileRequestDto;
import com.edu.basaoc.model.ProfileResponseDto;
import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.model.ArtistDto;
import com.edu.basaoc.model.entity.Profile;
import com.edu.basaoc.model.mapper.ProfileResponseDtoMapper;
import com.edu.basaoc.service.*;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin
public class ProfileController {

    private final AccountService accountService;
    private final ProfileService profileService;

    private final ProfileResponseDtoMapper profileResponseDtoMapper = Mappers.getMapper(ProfileResponseDtoMapper.class);

    public ProfileController(AccountService accountService, ProfileService profileService) {
        this.accountService = accountService;
        this.profileService = profileService;
    }

    @GetMapping(value = "/get-top-artists")
    public ResponseEntity<Set<ArtistDto>> getTopArtists(Principal principal) {
        return ResponseEntity.ok().body(profileService.getTopArtists(accountService.findByUsername(principal.getName())));
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<ProfileResponseDto> getProfile(@PathVariable Long profileId, Principal principal) {
        ProfileResponseDto profileResponseDto = profileResponseDtoMapper.entityToDto(profileService.getProfileById(profileId));
        return ResponseEntity.ok().body(profileResponseDto);
    }

    @GetMapping
    public ResponseEntity<ProfileResponseDto> getProfileById(Principal principal) {
        Account account = accountService.findByUsername(principal.getName());
        ProfileResponseDto profileResponseDto = profileResponseDtoMapper.entityToDto(account.getProfile());
        return ResponseEntity.ok().body(profileResponseDto);
    }

    @PutMapping
    public ResponseEntity<ProfileResponseDto> updateProfile(
            @RequestBody ProfileRequestDto profileRequestDto,
            Principal principal
    ) {
        Account account = accountService.findByUsername(principal.getName());
        Profile profile = profileService.updateProfile(account.getProfile(), profileRequestDto);
        return ResponseEntity.ok().body(profileResponseDtoMapper.entityToDto(profile));
    }
}
