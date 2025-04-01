package com.example.voting.service.impl;

import com.example.voting.contract.VotingContract;
import com.example.voting.dto.*;
import com.example.voting.service.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VotingServiceImpl implements VotingService {

    @Autowired
    private Web3j web3j;

    @Value("${web3j.contract-address}")
    private String contractAddress;

    @Value("${web3j.wallet-private-key}")
    private String walletPrivateKey;

    private final Credentials credentials = Credentials.create(walletPrivateKey);

    public VotingContract loadContract() {
        return VotingContract.load(
                contractAddress,
                web3j,
                credentials,
                new DefaultGasProvider()
        );
    }


    @Override
    public VotingDto createVoting(CreateVotingRequest request) throws Exception {
        VotingContract contract = loadContract();

        List<String> candidateNames = request.getCandidateNames();

        contract.createVoting(
                request.getTitle(),
                request.getDescription(),
                BigInteger.valueOf(request.getDuration()),
                candidateNames
        ).send();

        return getLatestVoting();

    }

    public VotingDto getLatestVoting() throws Exception {
        VotingContract contract = loadContract();
        BigInteger votingCount = contract.votingCount().send();
        if (votingCount.equals(BigInteger.ZERO)) {
            return null;
        }

        return getVotingById(votingCount.subtract(BigInteger.ONE).longValue());
    }



    @Override
    public VotingDto getVotingById(Long votingId) throws Exception {
        VotingContract contract = loadContract();

        Tuple6<BigInteger, String, String, BigInteger, BigInteger, Boolean> votingTuple =
                contract.votings(BigInteger.valueOf(votingId)).send();
        Voting voting = new Voting(votingTuple);  // 转换为 Voting 对象
        List<VotingContract.Candidate> candidates = contract.getVotingResults(BigInteger.valueOf(votingId)).send();

        VotingDto dto = new VotingDto();
        dto.setId(votingId);
        dto.setTitle(voting.getTitle());
        dto.setDescription(voting.getDescription());
        dto.setStartTime(voting.getStartTime().longValue());
        dto.setEndTime(voting.getEndTime().longValue());
        dto.setIsActive(voting.getIsActive());

        List<CandidateDto> candidateDtos = candidates.stream()
                .map(c -> {
                    CandidateDto cd = new CandidateDto();
                    cd.setId(c.id.longValue());
                    cd.setName(c.name);
                    cd.setVoteCount(c.voteCount.longValue());
                    return cd;
                })
                .collect(Collectors.toList());

        dto.setCandidates(candidateDtos);

        return dto;

    }

    @Override
    public List<VotingDto> getActiveVotings() throws Exception {
        VotingContract contract = loadContract();

        Tuple3<List<BigInteger>, List<String>, List<BigInteger>> activeVotings =
                contract.getActiveVotings().send();

        // 转换逻辑
        List<VotingDto> votingDtos = new ArrayList<>();
        for (int i = 0; i < activeVotings.component1().size(); i++) {
            VotingDto dto = new VotingDto();
            dto.setId(activeVotings.component1().get(i).longValue());
            dto.setTitle(activeVotings.component2().get(i));
            dto.setEndTime(activeVotings.component3().get(i).longValue());
            votingDtos.add(dto);
        }

        return votingDtos;

    }

    @Override
    public void vote(VoteRequest request) throws Exception {
        VotingContract contract = loadContract();

        contract.vote(
                BigInteger.valueOf(request.getVotingId()),
                BigInteger.valueOf(request.getCandidateId())
        ).send();

    }
}
