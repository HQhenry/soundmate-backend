package com.edu.basaoc.controller;

import com.edu.basaoc.model.ProfileRequestDto;
import com.edu.basaoc.model.ProfileResponseDto;
import com.edu.basaoc.model.entity.Account;
import com.edu.basaoc.model.ArtistDto;
import com.edu.basaoc.model.entity.Profile;
import com.edu.basaoc.model.mapper.ProfileResponseDtoMapper;
import com.edu.basaoc.service.AccountService;
import com.edu.basaoc.service.ProfileService;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Set;

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

    @GetMapping
    public ResponseEntity<ProfileResponseDto> getProfile(Principal principal) {
        Account account = accountService.findByUsername(principal.getName());
        return ResponseEntity.ok().body(profileResponseDtoMapper.entityToDto(account.getProfile()));
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
