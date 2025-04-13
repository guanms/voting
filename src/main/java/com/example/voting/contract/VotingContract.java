package com.example.voting.contract;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tuples.generated.Tuple7;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/hyperledger-web3j/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.6.3.
 */
@SuppressWarnings("rawtypes")
public class VotingContract extends Contract {
    public static final String BINARY = "0x608060405234801561000f575f5ffd5b5060043610610060575f3560e01c806336effcaa14610064578063a1b53cc014610093578063b1e3a48f146100a8578063b384abef146100bf578063b424ab84146100d2578063cd89b2e3146100e9575b5f5ffd5b6100776100723660046109ed565b610109565b60405161008a9796959493929190610a32565b60405180910390f35b6100a66100a1366004610b32565b61027e565b005b6100b06103db565b60405161008a93929190610c91565b6100a66100cd366004610d19565b610791565b6100db60015481565b60405190815260200161008a565b6100fc6100f73660046109ed565b6108e1565b60405161008a9190610d39565b5f6060805f5f5f5f5f5f5f8a81526020019081526020015f209050805f0154816001018260020183600301548460040154856005015f9054906101000a900460ff16866008015485805461015c90610dbe565b80601f016020809104026020016040519081016040528092919081815260200182805461018890610dbe565b80156101d35780601f106101aa576101008083540402835291602001916101d3565b820191905f5260205f20905b8154815290600101906020018083116101b657829003601f168201915b505050505095508480546101e690610dbe565b80601f016020809104026020016040519081016040528092919081815260200182805461021290610dbe565b801561025d5780601f106102345761010080835404028352916020019161025d565b820191905f5260205f20905b81548152906001019060200180831161024057829003601f168201915b50505050509450975097509750975097509750975050919395979092949650565b6002815110156102a1576040516313bd20b360e01b815260040160405180910390fd5b600180545f91826102b183610e0a565b909155505f818152602081905260409020818155909150600181016102d68782610e6e565b50600281016102e58682610e6e565b5042600382018190556102f9908590610f28565b600482015560058101805460ff191660011790555f5b835181101561039a5781600601604051806060016040528083815260200186848151811061033f5761033f610f41565b60209081029190910181015182525f91810182905283546001818101865594835291819020835160039093020191825582015191929091908201906103849082610e6e565b506040919091015160029091015560010161030f565b50817fe9722cdfea16904f7c251713498b56fbb15389ba08f367afd2503b89c260e1b8876040516103cb9190610f55565b60405180910390a2505050505050565b60608060605f60015490505f816001600160401b038111156103ff576103ff610a83565b604051908082528060200260200182016040528015610428578160200160208202803683370190505b5090505f826001600160401b0381111561044457610444610a83565b60405190808252806020026020018201604052801561047757816020015b60608152602001906001900390816104625790505b5090505f836001600160401b0381111561049357610493610a83565b6040519080825280602002602001820160405280156104bc578160200160208202803683370190505b5090505f805b858110156105f7575f818152602081905260409020600581015460ff1680156104ef575080600401544211155b156105ee57805f015486848151811061050a5761050a610f41565b60200260200101818152505080600101805461052590610dbe565b80601f016020809104026020016040519081016040528092919081815260200182805461055190610dbe565b801561059c5780601f106105735761010080835404028352916020019161059c565b820191905f5260205f20905b81548152906001019060200180831161057f57829003601f168201915b50505050508584815181106105b3576105b3610f41565b602002602001018190525080600401548484815181106105d5576105d5610f41565b6020908102919091010152826105ea81610e0a565b9350505b506001016104c2565b50806001600160401b0381111561061057610610610a83565b604051908082528060200260200182016040528015610639578160200160208202803683370190505b509750806001600160401b0381111561065457610654610a83565b60405190808252806020026020018201604052801561068757816020015b60608152602001906001900390816106725790505b509650806001600160401b038111156106a2576106a2610a83565b6040519080825280602002602001820160405280156106cb578160200160208202803683370190505b5095505f5b81811015610786578481815181106106ea576106ea610f41565b602002602001015189828151811061070457610704610f41565b60200260200101818152505083818151811061072257610722610f41565b602002602001015188828151811061073c5761073c610f41565b602002602001018190525082818151811061075957610759610f41565b602002602001015187828151811061077357610773610f41565b60209081029190910101526001016106d0565b505050505050909192565b5f828152602081905260409020600581015460ff166107c357604051639b8cc47560e01b815260040160405180910390fd5b80600401544211156107e857604051637a19ed0560e01b815260040160405180910390fd5b335f90815260078201602052604090205460ff161561081a57604051637c9a1cf960e01b815260040160405180910390fd5b6006810154821061083e5760405163e66ea08f60e01b815260040160405180910390fd5b335f9081526007820160205260408120805460ff191660011790556008820180549161086983610e0a565b919050555080600601828154811061088357610883610f41565b5f918252602082206002600390920201018054916108a083610e0a565b9091555050604051828152339084907fd91ecafccf238642dccd41161308b3eebe62a12bc5819daf5164780c221ec95b9060200160405180910390a3505050565b5f81815260208181526040808320600601805482518185028101850190935280835260609492939192909184015b828210156109e2578382905f5260205f2090600302016040518060600160405290815f820154815260200160018201805461094990610dbe565b80601f016020809104026020016040519081016040528092919081815260200182805461097590610dbe565b80156109c05780601f10610997576101008083540402835291602001916109c0565b820191905f5260205f20905b8154815290600101906020018083116109a357829003601f168201915b505050505081526020016002820154815250508152602001906001019061090f565b505050509050919050565b5f602082840312156109fd575f5ffd5b5035919050565b5f81518084528060208401602086015e5f602082860101526020601f19601f83011685010191505092915050565b87815260e060208201525f610a4a60e0830189610a04565b8281036040840152610a5c8189610a04565b60608401979097525050608081019390935290151560a083015260c0909101529392505050565b634e487b7160e01b5f52604160045260245ffd5b604051601f8201601f191681016001600160401b0381118282101715610abf57610abf610a83565b604052919050565b5f82601f830112610ad6575f5ffd5b81356001600160401b03811115610aef57610aef610a83565b610b02601f8201601f1916602001610a97565b818152846020838601011115610b16575f5ffd5b816020850160208301375f918101602001919091529392505050565b5f5f5f5f60808587031215610b45575f5ffd5b84356001600160401b03811115610b5a575f5ffd5b610b6687828801610ac7565b94505060208501356001600160401b03811115610b81575f5ffd5b610b8d87828801610ac7565b9350506040850135915060608501356001600160401b03811115610baf575f5ffd5b8501601f81018713610bbf575f5ffd5b80356001600160401b03811115610bd857610bd8610a83565b8060051b610be860208201610a97565b9182526020818401810192908101908a841115610c03575f5ffd5b6020850192505b83831015610c485782356001600160401b03811115610c27575f5ffd5b610c368c602083890101610ac7565b83525060209283019290910190610c0a565b979a9699509497505050505050565b5f8151808452602084019350602083015f5b82811015610c87578151865260209586019590910190600101610c69565b5093949350505050565b606081525f610ca36060830186610c57565b828103602084015280855180835260208301915060208160051b840101602088015f5b83811015610cf857601f19868403018552610ce2838351610a04565b6020958601959093509190910190600101610cc6565b50508581036040870152610d0c8188610c57565b9998505050505050505050565b5f5f60408385031215610d2a575f5ffd5b50508035926020909101359150565b5f602082016020835280845180835260408501915060408160051b8601019250602086015f5b82811015610db257603f19878603018452815180518652602081015160606020880152610d8f6060880182610a04565b604092830151979092019690965294506020938401939190910190600101610d5f565b50929695505050505050565b600181811c90821680610dd257607f821691505b602082108103610df057634e487b7160e01b5f52602260045260245ffd5b50919050565b634e487b7160e01b5f52601160045260245ffd5b5f60018201610e1b57610e1b610df6565b5060010190565b601f821115610e6957805f5260205f20601f840160051c81016020851015610e475750805b601f840160051c820191505b81811015610e66575f8155600101610e53565b50505b505050565b81516001600160401b03811115610e8757610e87610a83565b610e9b81610e958454610dbe565b84610e22565b6020601f821160018114610ecd575f8315610eb65750848201515b5f19600385901b1c1916600184901b178455610e66565b5f84815260208120601f198516915b82811015610efc5787850151825560209485019460019092019101610edc565b5084821015610f1957868401515f19600387901b60f8161c191681555b50505050600190811b01905550565b80820180821115610f3b57610f3b610df6565b92915050565b634e487b7160e01b5f52603260045260245ffd5b602081525f610f676020830184610a04565b939250505056fea264697066735822122017634b199e87cd725b2af8ee1c0c74d330bec2932fb10f37200532251d4e82f864736f6c634300081d0033";

    private static String librariesLinkedBinary;

    public static final String FUNC_CREATEVOTING = "createVoting";

    public static final String FUNC_GETACTIVEVOTINGS = "getActiveVotings";

    public static final String FUNC_GETVOTINGDETAILS = "getVotingDetails";

    public static final String FUNC_GETVOTINGRESULTS = "getVotingResults";

    public static final String FUNC_VOTE = "vote";

    public static final String FUNC_VOTINGCOUNT = "votingCount";

    public static final Event VOTED_EVENT = new Event("Voted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event VOTINGCREATED_EVENT = new Event("VotingCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected VotingContract(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected VotingContract(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected VotingContract(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected VotingContract(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<VotedEventResponse> getVotedEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(VOTED_EVENT, transactionReceipt);
        ArrayList<VotedEventResponse> responses = new ArrayList<VotedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            VotedEventResponse typedResponse = new VotedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.votingId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.voter = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.candidateId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static VotedEventResponse getVotedEventFromLog(Log log) {
        EventValuesWithLog eventValues = staticExtractEventParametersWithLog(VOTED_EVENT, log);
        VotedEventResponse typedResponse = new VotedEventResponse();
        typedResponse.log = log;
        typedResponse.votingId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.voter = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.candidateId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<VotedEventResponse> votedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getVotedEventFromLog(log));
    }

    public Flowable<VotedEventResponse> votedEventFlowable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTED_EVENT));
        return votedEventFlowable(filter);
    }

    public static List<VotingCreatedEventResponse> getVotingCreatedEvents(
            TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = staticExtractEventParametersWithLog(VOTINGCREATED_EVENT, transactionReceipt);
        ArrayList<VotingCreatedEventResponse> responses = new ArrayList<VotingCreatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            VotingCreatedEventResponse typedResponse = new VotingCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.votingId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.title = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static VotingCreatedEventResponse getVotingCreatedEventFromLog(Log log) {
        EventValuesWithLog eventValues = staticExtractEventParametersWithLog(VOTINGCREATED_EVENT, log);
        VotingCreatedEventResponse typedResponse = new VotingCreatedEventResponse();
        typedResponse.log = log;
        typedResponse.votingId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.title = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<VotingCreatedEventResponse> votingCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getVotingCreatedEventFromLog(log));
    }

    public Flowable<VotingCreatedEventResponse> votingCreatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTINGCREATED_EVENT));
        return votingCreatedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> createVoting(String _title, String _description,
            BigInteger _duration, List<String> _candidateNames) {
        final Function function = new Function(
                FUNC_CREATEVOTING, 
                Arrays.<Type>asList(new Utf8String(_title),
                new Utf8String(_description),
                new Uint256(_duration),
                new DynamicArray<Utf8String>(
                        Utf8String.class,
                        org.web3j.abi.Utils.typeMap(_candidateNames, Utf8String.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple3<List<BigInteger>, List<String>, List<BigInteger>>> getActiveVotings(
            ) {
        final Function function = new Function(FUNC_GETACTIVEVOTINGS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<DynamicArray<Utf8String>>() {}, new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteFunctionCall<Tuple3<List<BigInteger>, List<String>, List<BigInteger>>>(function,
                new Callable<Tuple3<List<BigInteger>, List<String>, List<BigInteger>>>() {
                    @Override
                    public Tuple3<List<BigInteger>, List<String>, List<BigInteger>> call() throws
                            Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<List<BigInteger>, List<String>, List<BigInteger>>(
                                convertToNative((List<Uint256>) results.get(0).getValue()), 
                                convertToNative((List<Utf8String>) results.get(1).getValue()), 
                                convertToNative((List<Uint256>) results.get(2).getValue()));
                    }
                });
    }

    public RemoteFunctionCall<Tuple7<BigInteger, String, String, BigInteger, BigInteger, Boolean, BigInteger>> getVotingDetails(
            BigInteger _votingId) {
        final Function function = new Function(FUNC_GETVOTINGDETAILS, 
                Arrays.<Type>asList(new Uint256(_votingId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple7<BigInteger, String, String, BigInteger, BigInteger, Boolean, BigInteger>>(function,
                new Callable<Tuple7<BigInteger, String, String, BigInteger, BigInteger, Boolean, BigInteger>>() {
                    @Override
                    public Tuple7<BigInteger, String, String, BigInteger, BigInteger, Boolean, BigInteger> call(
                            ) throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple7<BigInteger, String, String, BigInteger, BigInteger, Boolean, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (Boolean) results.get(5).getValue(), 
                                (BigInteger) results.get(6).getValue());
                    }
                });
    }

    public RemoteFunctionCall<List> getVotingResults(BigInteger _votingId) {
        final Function function = new Function(FUNC_GETVOTINGRESULTS, 
                Arrays.<Type>asList(new Uint256(_votingId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Candidate>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> vote(BigInteger _votingId,
            BigInteger _candidateId) {
        final Function function = new Function(
                FUNC_VOTE, 
                Arrays.<Type>asList(new Uint256(_votingId),
                new Uint256(_candidateId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> votingCount() {
        final Function function = new Function(FUNC_VOTINGCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static VotingContract load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new VotingContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static VotingContract load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new VotingContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static VotingContract load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new VotingContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static VotingContract load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new VotingContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<VotingContract> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(VotingContract.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<VotingContract> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(VotingContract.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    public static RemoteCall<VotingContract> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(VotingContract.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<VotingContract> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(VotingContract.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class Candidate extends DynamicStruct {
        public BigInteger id;

        public String name;

        public BigInteger voteCount;

        public Candidate(BigInteger id, String name, BigInteger voteCount) {
            super(new Uint256(id),
                    new Utf8String(name),
                    new Uint256(voteCount));
            this.id = id;
            this.name = name;
            this.voteCount = voteCount;
        }

        public Candidate(Uint256 id, Utf8String name, Uint256 voteCount) {
            super(id, name, voteCount);
            this.id = id.getValue();
            this.name = name.getValue();
            this.voteCount = voteCount.getValue();
        }
    }

    public static class VotedEventResponse extends BaseEventResponse {
        public BigInteger votingId;

        public String voter;

        public BigInteger candidateId;
    }

    public static class VotingCreatedEventResponse extends BaseEventResponse {
        public BigInteger votingId;

        public String title;
    }
}
