// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

/**
 * @dev 主投票合约，管理投票生命周期
 * - 每个投票包含多个候选人
 * - 每个地址在每个投票中只能投一次
 * - 投票结果不可篡改
 * @custom:upgrade-ability 此合约设计为不可升级，确保投票结果不可变
 */
contract VotingContract {
    /// @notice 候选人信息
    /// @param id 候选人ID(自增)
    /// @param name 候选人名称
    /// @param voteCount 候选人得票数
    struct Candidate{
        uint256 id;
        string name;
        uint256 voteCount;
    }

    /// @notice 存储每个投票活动的基本信息
    /// @param id 投票活动ID(自增)
    /// @param title 投票活动标题
    /// @param description 投票活动描述
    /// @param startTime 投票活动开始时间
    /// @param endTime 投票活动结束时间
    /// @param isActive 投票活动是否激活
    /// @param candidates 投票活动候选人列表
    /// @param voters 投票活动投票人列表
    struct Voting{
        uint256 id;
        string title;
        string description;
        uint256 startTime;
        uint256 endTime;
        bool isActive;
        Candidate[] candidates;
        mapping(address => bool) voted;  // 替换原voters数组
        uint256 voterCount;             // 新增投票人数统计
    }

   // 优化：添加自定义错误类型降低gas消耗
    error InsufficientCandidates();
    error VotingNotActive();
    error VotingEnded();
    error AlreadyVoted();
    error InvalidCandidate();

    // 优化：使用更紧凑的存储结构
    mapping(uint256 => Voting) private _votings;
    uint256 public votingCount;

    // 优化：为事件添加indexed参数
    event VotingCreated(uint256 indexed votingId, string title);
    event Voted(uint256 indexed votingId, uint256 candidateId, address indexed voter);

    /// @notice 创建投票活动
    /// @param _title 投票活动标题
    /// @param _description 投票活动描述
    /// @param _duration 投票活动持续时间
    /// @param _candidateNames 投票活动候选人列表
    function createVoting(string memory _title, string memory _description, uint256 _duration, string[] memory _candidateNames) public {
        if (_candidateNames.length < 2) revert InsufficientCandidates();

        uint256 votingId = votingCount++;
        Voting storage newVoting = _votings[votingId];

        // 初始化投票基础信息
        newVoting.id = votingId;
        newVoting.title = _title;
        newVoting.description = _description;
        newVoting.startTime = block.timestamp;
        newVoting.endTime = block.timestamp + _duration;
        newVoting.isActive = true;

        // 优化：预分配数组空间
        // 修改为逐个push：
        for (uint i = 0; i < _candidateNames.length; i++) {
            newVoting.candidates.push(Candidate({
                id: i,
                name: _candidateNames[i],
                voteCount: 0
            }));
        }

        emit VotingCreated(votingId, _title);
    }


    /// @notice 投票
    /// @param _votingId 投票活动ID
    /// @param _candidateId 投票候选人ID
    function vote(uint _votingId, uint _candidateId) public {
        Voting storage voting = _votings[_votingId];

        if (!voting.isActive) revert VotingNotActive();
        if (block.timestamp > voting.endTime) revert VotingEnded();
        if (voting.voted[msg.sender]) revert AlreadyVoted();
        if (_candidateId >= voting.candidates.length) revert InvalidCandidate();

        // 优化：使用mapping记录投票状态
        voting.voted[msg.sender] = true;
        voting.voterCount++;
        voting.candidates[_candidateId].voteCount++;

        emit Voted(_votingId, _candidateId, msg.sender);
    }

    /// @notice 获取投票结果
    /// @param _votingId 投票活动ID
    function getVotingResults(uint _votingId) public view returns (Candidate[] memory) {
        return _votings[_votingId].candidates;
    }

    /**
     * @notice 获取活跃投票列表（重构后）
     * @return ids 活跃投票的ID数组
     * @return titles 活跃投票的标题数组
     * @return endTimes 活跃投票的结束时间数组
     * @dev 返回精简的投票信息（避免gas过高）
     */
    // 优化：使用单次遍历+动态数组
    function getActiveVotings() public view returns (uint256[] memory ids, string[] memory titles, uint256[] memory endTimes) {
        uint256 count = votingCount;
        uint256[] memory tempIds = new uint256[](count);
        string[] memory tempTitles = new string[](count);
        uint256[] memory tempEndTimes = new uint256[](count);

        uint256 validCount;
        for (uint256 i = 0; i < count; i++) {
            Voting storage v = _votings[i];
            if (v.isActive && block.timestamp <= v.endTime) {
                tempIds[validCount] = v.id;
                tempTitles[validCount] = v.title;
                tempEndTimes[validCount] = v.endTime;
                validCount++;
            }
        }

        // 裁剪数组到实际大小
        ids = new uint256[](validCount);
        titles = new string[](validCount);
        endTimes = new uint256[](validCount);

        for (uint256 i = 0; i < validCount; i++) {
            ids[i] = tempIds[i];
            titles[i] = tempTitles[i];
            endTimes[i] = tempEndTimes[i];
        }
    }

    /**
     * @notice 获取投票详情（新增函数）
     * @return id 投票ID
     * @return title 投票标题
     * @return description 投票描述
     * @return startTime 投票开始时间
     * @return endTime 投票结束时间
     * @return isActive 投票是否激活
     * @return voterCount 投票人数
     * @dev 需要单独调用getVotingResults获取候选人详情
     */
    function getVotingDetails(uint256 _votingId) public view returns (uint256 id, string memory title, string memory description, uint256 startTime, uint256 endTime, bool isActive, uint256 voterCount) {
        Voting storage v = _votings[_votingId];
        return (
            v.id,
            v.title,
            v.description,
            v.startTime,
            v.endTime,
            v.isActive,
            v.voterCount  // 使用存储的统计值
        );
    }

}
