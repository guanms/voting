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
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tuples.generated.Tuple7;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.9.8.
 */
@SuppressWarnings("rawtypes")
public class VotingContract extends Contract {
    public static final String BINARY = "6080604052348015600e575f80fd5b506111188061001c5f395ff3fe608060405234801561000f575f80fd5b506004361061007a575f3560e01c8063b1e3a48f11610058578063b1e3a48f146100e7578063b384abef146100fe578063b424ab8414610111578063cd89b2e314610128575f80fd5b806336effcaa1461007e578063a1b53cc0146100ad578063a598d03c146100c2575b5f80fd5b61009161008c366004610b0d565b610148565b6040516100a49796959493929190610b52565b60405180910390f35b6100c06100bb366004610c54565b6102bc565b005b6100d56100d0366004610b0d565b61044e565b6040516100a496959493929190610d7e565b6100ef610593565b6040516100a493929190610e03565b6100c061010c366004610e8b565b61080c565b61011a60015481565b6040519081526020016100a4565b61013b610136366004610b0d565b61099d565b6040516100a49190610eab565b5f8181526020819052604081208054600382015460048301546005840154600785015460018601805460609788978a97889788978897949693959394600288019460ff90921691869061019a90610f30565b80601f01602080910402602001604051908101604052809291908181526020018280546101c690610f30565b80156102115780601f106101e857610100808354040283529160200191610211565b820191905f5260205f20905b8154815290600101906020018083116101f457829003601f168201915b5050505050955084805461022490610f30565b80601f016020809104026020016040519081016040528092919081815260200182805461025090610f30565b801561029b5780601f106102725761010080835404028352916020019161029b565b820191905f5260205f20905b81548152906001019060200180831161027e57829003601f168201915b50505050509450975097509750975097509750975050919395979092949650565b6002815110156103135760405162461bcd60e51b815260206004820152601e60248201527f4174206c6561737420322063616e64696461746573207265717569726564000060448201526064015b60405180910390fd5b600180545f918261032383610f7c565b909155505f818152602081905260409020818155909150600181016103488782610fe0565b50600281016103578682610fe0565b50426003820181905561036b90859061109b565b600482015560058101805460ff191660011790555f5b835181101561040c578160060160405180606001604052808381526020018684815181106103b1576103b16110ae565b60209081029190910181015182525f91810182905283546001818101865594835291819020835160039093020191825582015191929091908201906103f69082610fe0565b5060409190910151600290910155600101610381565b507fe9722cdfea16904f7c251713498b56fbb15389ba08f367afd2503b89c260e1b8828760405161043e9291906110c2565b60405180910390a1505050505050565b5f602081905290815260409020805460018201805491929161046f90610f30565b80601f016020809104026020016040519081016040528092919081815260200182805461049b90610f30565b80156104e65780601f106104bd576101008083540402835291602001916104e6565b820191905f5260205f20905b8154815290600101906020018083116104c957829003601f168201915b5050505050908060020180546104fb90610f30565b80601f016020809104026020016040519081016040528092919081815260200182805461052790610f30565b80156105725780601f1061054957610100808354040283529160200191610572565b820191905f5260205f20905b81548152906001019060200180831161055557829003601f168201915b50505050600383015460048401546005909401549293909290915060ff1686565b60608060605f805b6001548110156105f0575f8181526020819052604090206005015460ff1680156105d557505f818152602081905260409020600401544211155b156105e857816105e481610f7c565b9250505b60010161059b565b508067ffffffffffffffff81111561060a5761060a610ba3565b604051908082528060200260200182016040528015610633578160200160208202803683370190505b5093508067ffffffffffffffff81111561064f5761064f610ba3565b60405190808252806020026020018201604052801561068257816020015b606081526020019060019003908161066d5790505b5092508067ffffffffffffffff81111561069e5761069e610ba3565b6040519080825280602002602001820160405280156106c7578160200160208202803683370190505b5091505f805b600154811015610804575f818152602081905260409020600581015460ff1680156106fc575080600401544211155b156107fb57805f0154878481518110610717576107176110ae565b60200260200101818152505080600101805461073290610f30565b80601f016020809104026020016040519081016040528092919081815260200182805461075e90610f30565b80156107a95780601f10610780576101008083540402835291602001916107a9565b820191905f5260205f20905b81548152906001019060200180831161078c57829003601f168201915b50505050508684815181106107c0576107c06110ae565b602002602001018190525080600401548584815181106107e2576107e26110ae565b6020908102919091010152826107f781610f7c565b9350505b506001016106cd565b505050909192565b5f828152602081905260409020600581015460ff166108645760405162461bcd60e51b8152602060048201526014602482015273566f74696e67206973206e6f742061637469766560601b604482015260640161030a565b80600401544211156108ab5760405162461bcd60e51b815260206004820152601060248201526f159bdd1a5b99c81a185cc8195b99195960821b604482015260640161030a565b6108b58133610aa9565b156108fb5760405162461bcd60e51b8152602060048201526016602482015275165bdd481a185d9948185b1c9958591e481d9bdd195960521b604482015260640161030a565b6007810180546001810182555f91825260209091200180546001600160a01b0319163317905560068101805483908110610937576109376110ae565b5f9182526020822060026003909202010180549161095483610f7c565b90915550506040805184815260208101849052338183015290517fd91ecafccf238642dccd41161308b3eebe62a12bc5819daf5164780c221ec95b9181900360600190a1505050565b5f81815260208181526040808320600601805482518185028101850190935280835260609492939192909184015b82821015610a9e578382905f5260205f2090600302016040518060600160405290815f8201548152602001600182018054610a0590610f30565b80601f0160208091040260200160405190810160405280929190818152602001828054610a3190610f30565b8015610a7c5780601f10610a5357610100808354040283529160200191610a7c565b820191905f5260205f20905b815481529060010190602001808311610a5f57829003601f168201915b50505050508152602001600282015481525050815260200190600101906109cb565b505050509050919050565b5f805b6007840154811015610b0257826001600160a01b0316846007018281548110610ad757610ad76110ae565b5f918252602090912001546001600160a01b031603610afa576001915050610b07565b600101610aac565b505f90505b92915050565b5f60208284031215610b1d575f80fd5b5035919050565b5f81518084528060208401602086015e5f602082860101526020601f19601f83011685010191505092915050565b87815260e060208201525f610b6a60e0830189610b24565b8281036040840152610b7c8189610b24565b60608401979097525050608081019390935290151560a083015260c0909101529392505050565b634e487b7160e01b5f52604160045260245ffd5b604051601f8201601f1916810167ffffffffffffffff81118282101715610be057610be0610ba3565b604052919050565b5f82601f830112610bf7575f80fd5b813567ffffffffffffffff811115610c1157610c11610ba3565b610c24601f8201601f1916602001610bb7565b818152846020838601011115610c38575f80fd5b816020850160208301375f918101602001919091529392505050565b5f805f8060808587031215610c67575f80fd5b843567ffffffffffffffff811115610c7d575f80fd5b610c8987828801610be8565b945050602085013567ffffffffffffffff811115610ca5575f80fd5b610cb187828801610be8565b93505060408501359150606085013567ffffffffffffffff811115610cd4575f80fd5b8501601f81018713610ce4575f80fd5b803567ffffffffffffffff811115610cfe57610cfe610ba3565b8060051b610d0e60208201610bb7565b9182526020818401810192908101908a841115610d29575f80fd5b6020850192505b83831015610d6f57823567ffffffffffffffff811115610d4e575f80fd5b610d5d8c602083890101610be8565b83525060209283019290910190610d30565b979a9699509497505050505050565b86815260c060208201525f610d9660c0830188610b24565b8281036040840152610da88188610b24565b606084019690965250506080810192909252151560a0909101529392505050565b5f8151808452602084019350602083015f5b82811015610df9578151865260209586019590910190600101610ddb565b5093949350505050565b606081525f610e156060830186610dc9565b828103602084015280855180835260208301915060208160051b840101602088015f5b83811015610e6a57601f19868403018552610e54838351610b24565b6020958601959093509190910190600101610e38565b50508581036040870152610e7e8188610dc9565b9998505050505050505050565b5f8060408385031215610e9c575f80fd5b50508035926020909101359150565b5f602082016020835280845180835260408501915060408160051b8601019250602086015f5b82811015610f2457603f19878603018452815180518652602081015160606020880152610f016060880182610b24565b604092830151979092019690965294506020938401939190910190600101610ed1565b50929695505050505050565b600181811c90821680610f4457607f821691505b602082108103610f6257634e487b7160e01b5f52602260045260245ffd5b50919050565b634e487b7160e01b5f52601160045260245ffd5b5f60018201610f8d57610f8d610f68565b5060010190565b601f821115610fdb57805f5260205f20601f840160051c81016020851015610fb95750805b601f840160051c820191505b81811015610fd8575f8155600101610fc5565b50505b505050565b815167ffffffffffffffff811115610ffa57610ffa610ba3565b61100e816110088454610f30565b84610f94565b6020601f821160018114611040575f83156110295750848201515b5f19600385901b1c1916600184901b178455610fd8565b5f84815260208120601f198516915b8281101561106f578785015182556020948501946001909201910161104f565b508482101561108c57868401515f19600387901b60f8161c191681555b50505050600190811b01905550565b80820180821115610b0757610b07610f68565b634e487b7160e01b5f52603260045260245ffd5b828152604060208201525f6110da6040830184610b24565b94935050505056fea2646970667358221220c7d4342d823d252f9e0bf6b06e5939a84839ec4ae03f6bdee859b22e577ce4a864736f6c634300081a0033";

    public static final String FUNC_CREATEVOTING = "createVoting";

    public static final String FUNC_GETACTIVEVOTINGS = "getActiveVotings";

    public static final String FUNC_GETVOTINGDETAILS = "getVotingDetails";

    public static final String FUNC_GETVOTINGRESULTS = "getVotingResults";

    public static final String FUNC_VOTE = "vote";

    public static final String FUNC_VOTINGCOUNT = "votingCount";

    public static final String FUNC_VOTINGS = "votings";

    public static final Event VOTED_EVENT = new Event("Voted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event VOTINGCREATED_EVENT = new Event("VotingCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected VotingContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected VotingContract(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected VotingContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected VotingContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<VotedEventResponse> getVotedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(VOTED_EVENT, transactionReceipt);
        ArrayList<VotedEventResponse> responses = new ArrayList<VotedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VotedEventResponse typedResponse = new VotedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.votingId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.candidateId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.voter = (String) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static VotedEventResponse getVotedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(VOTED_EVENT, log);
        VotedEventResponse typedResponse = new VotedEventResponse();
        typedResponse.log = log;
        typedResponse.votingId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.candidateId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.voter = (String) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<VotedEventResponse> votedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getVotedEventFromLog(log));
    }

    public Flowable<VotedEventResponse> votedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTED_EVENT));
        return votedEventFlowable(filter);
    }

    public static List<VotingCreatedEventResponse> getVotingCreatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(VOTINGCREATED_EVENT, transactionReceipt);
        ArrayList<VotingCreatedEventResponse> responses = new ArrayList<VotingCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VotingCreatedEventResponse typedResponse = new VotingCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.votingId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.title = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static VotingCreatedEventResponse getVotingCreatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(VOTINGCREATED_EVENT, log);
        VotingCreatedEventResponse typedResponse = new VotingCreatedEventResponse();
        typedResponse.log = log;
        typedResponse.votingId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.title = (String) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<VotingCreatedEventResponse> votingCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getVotingCreatedEventFromLog(log));
    }

    public Flowable<VotingCreatedEventResponse> votingCreatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTINGCREATED_EVENT));
        return votingCreatedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> createVoting(String _title, String _description, BigInteger _duration, List<String> _candidateNames) {
        final Function function = new Function(
                FUNC_CREATEVOTING, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_title), 
                new org.web3j.abi.datatypes.Utf8String(_description), 
                new org.web3j.abi.datatypes.generated.Uint256(_duration), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(_candidateNames, org.web3j.abi.datatypes.Utf8String.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple3<List<BigInteger>, List<String>, List<BigInteger>>> getActiveVotings() {
        final Function function = new Function(FUNC_GETACTIVEVOTINGS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<DynamicArray<Utf8String>>() {}, new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteFunctionCall<Tuple3<List<BigInteger>, List<String>, List<BigInteger>>>(function,
                new Callable<Tuple3<List<BigInteger>, List<String>, List<BigInteger>>>() {
                    @Override
                    public Tuple3<List<BigInteger>, List<String>, List<BigInteger>> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<List<BigInteger>, List<String>, List<BigInteger>>(
                                convertToNative((List<Uint256>) results.get(0).getValue()), 
                                convertToNative((List<Utf8String>) results.get(1).getValue()), 
                                convertToNative((List<Uint256>) results.get(2).getValue()));
                    }
                });
    }

    public RemoteFunctionCall<Tuple7<BigInteger, String, String, BigInteger, BigInteger, Boolean, BigInteger>> getVotingDetails(BigInteger _votingId) {
        final Function function = new Function(FUNC_GETVOTINGDETAILS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_votingId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple7<BigInteger, String, String, BigInteger, BigInteger, Boolean, BigInteger>>(function,
                new Callable<Tuple7<BigInteger, String, String, BigInteger, BigInteger, Boolean, BigInteger>>() {
                    @Override
                    public Tuple7<BigInteger, String, String, BigInteger, BigInteger, Boolean, BigInteger> call() throws Exception {
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
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_votingId)), 
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

    public RemoteFunctionCall<TransactionReceipt> vote(BigInteger _votingId, BigInteger _candidateId) {
        final Function function = new Function(
                FUNC_VOTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_votingId), 
                new org.web3j.abi.datatypes.generated.Uint256(_candidateId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> votingCount() {
        final Function function = new Function(FUNC_VOTINGCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple6<BigInteger, String, String, BigInteger, BigInteger, Boolean>> votings(BigInteger param0) {
        final Function function = new Function(FUNC_VOTINGS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple6<BigInteger, String, String, BigInteger, BigInteger, Boolean>>(function,
                new Callable<Tuple6<BigInteger, String, String, BigInteger, BigInteger, Boolean>>() {
                    @Override
                    public Tuple6<BigInteger, String, String, BigInteger, BigInteger, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<BigInteger, String, String, BigInteger, BigInteger, Boolean>(
                                (BigInteger) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (Boolean) results.get(5).getValue());
                    }
                });
    }

    @Deprecated
    public static VotingContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new VotingContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static VotingContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new VotingContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static VotingContract load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new VotingContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static VotingContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new VotingContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<VotingContract> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(VotingContract.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<VotingContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(VotingContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<VotingContract> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(VotingContract.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<VotingContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(VotingContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class Candidate extends DynamicStruct {
        public BigInteger id;

        public String name;

        public BigInteger voteCount;

        public Candidate(BigInteger id, String name, BigInteger voteCount) {
            super(new org.web3j.abi.datatypes.generated.Uint256(id), 
                    new org.web3j.abi.datatypes.Utf8String(name), 
                    new org.web3j.abi.datatypes.generated.Uint256(voteCount));
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

        public BigInteger candidateId;

        public String voter;
    }

    public static class VotingCreatedEventResponse extends BaseEventResponse {
        public BigInteger votingId;

        public String title;
    }
}
