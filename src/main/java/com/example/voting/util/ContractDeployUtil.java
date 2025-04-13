package com.example.voting.util;

import com.example.voting.contract.VotingContract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;


public class ContractDeployUtil {

    private static final Logger logger = LoggerFactory.getLogger(ContractDeployUtil.class);

    public static String deployIfNotExists(Web3j web3j, Credentials credentials){
        String contractAddress = null;
        try {
            // check if contract already exists
            Path configPath = Paths.get("contract.properties");
            if (Files.exists(configPath)){
                logger.info("file already exists");
                Properties properties = new Properties();
                properties.load(Files.newInputStream(configPath));
                contractAddress = properties.getProperty("contract.address");

                if (isContractValid(web3j, contractAddress)) {
                    return contractAddress;
                }
            }
            // if not, deploy it
            VotingContract contract = VotingContract.deploy(web3j, credentials, new DefaultGasProvider(){
                @Override
                public BigInteger getGasLimit(String contractFunc) {
                    return BigInteger.valueOf(5_000_000L); // 更高的gas limit
                }
            }).send();
            contractAddress = contract.getContractAddress();

            // save contract address to file
            Properties properties = new Properties();
            properties.setProperty("contract.address", contractAddress);
            try {
                logger.info("save contractAddress to file");
                properties.store(Files.newOutputStream(configPath), "");
            } catch (Exception e) {
                logger.info("save contractAddress to file error");
            }

        }catch (Exception e){
            e.printStackTrace();
            logger.info("deploy contract error");
        }
        return contractAddress;
    }

    private static boolean isContractValid(Web3j web3j, String address) {
        try {
            return web3j.ethGetCode(address, DefaultBlockParameterName.LATEST).send().getCode() != null;
        } catch (Exception e) {
            return false;
        }
    }
}
