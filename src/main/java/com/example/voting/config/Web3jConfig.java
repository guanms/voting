package com.example.voting.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3jConfig {

    @Value("${web3j.client-address}")
    private String clientAddress;

    /**
     * 创建并返回一个Web3j实例用于区块链交互
     * 该方法通过HTTP服务连接指定的以太坊客户端节点地址，构建Web3j实例。
     * Web3j是访问以太坊网络功能的核心入口，可用于发送交易、查询区块数据等操作。
     *
     * @return Web3j 实例 - 配置了指定客户端地址的Web3j对象，用于与以太坊网络进行交互
     * 注：
     * - 使用前需确保clientAddress已正确配置有效的HTTP服务地址（如Infura节点或本地geth客户端地址）
     * - 返回的Web3j实例是线程安全的，建议在应用中复用该实例
     */
    @Bean
    public Web3j web3j(){
        return Web3j.build(new HttpService(clientAddress));
    }
}
