package com.example.voting.config;

import com.example.voting.util.ContractDeployUtil;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

@Configuration
public class BlockchainConfig {

    private final Logger logger = LoggerFactory.getLogger(BlockchainConfig.class);

    @Autowired
    private Web3j web3j;

    @Value("${web3j.wallet-private-key}")
    private String walletPrivateKey;

    @PostConstruct
    private void init(){
        logger.info("Deploying contract, walletPrivateKey:{}", walletPrivateKey);
        Credentials credentials = Credentials.create(walletPrivateKey);
        String contractAddress = ContractDeployUtil.deployIfNotExists(web3j, credentials);
        logger.info("Contract deployed at address:{}", contractAddress);
    }
}
