package com.example.voting.controller;

import com.example.voting.dto.CreateVotingRequest;
import com.example.voting.dto.VoteRequest;
import com.example.voting.dto.VotingDto;
import com.example.voting.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/votings")
public class VotingController {

    @Autowired
    private VotingService votingService;

    @CrossOrigin
    @PostMapping
    public VotingDto createVoting(@RequestBody CreateVotingRequest request) throws Exception {
        return votingService.createVoting(request);
    }

    @CrossOrigin
    @GetMapping("/active")
    public List<VotingDto> getActiveVotings() throws Exception {
        return votingService.getActiveVotings();
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public VotingDto getVotingById(@PathVariable Long id) throws Exception {
        return votingService.getVotingById(id);
    }

    @CrossOrigin
    @PostMapping("/vote")
    public void vote(@RequestBody VoteRequest request) throws Exception {
        votingService.vote(request);
    }

}
