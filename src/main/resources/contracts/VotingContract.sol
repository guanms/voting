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
        address[] voters;
    }

    mapping(uint256 => Voting) public votings;
    uint256 public votingCount;
    event VotingCreated(uint256 votingId, string title);
    event Voted(uint256 votingId, uint256 candidateId, address voter);

    /// @notice 创建投票活动
    /// @param _title 投票活动标题
    /// @param _description 投票活动描述
    /// @param _duration 投票活动持续时间
    /// @param _candidateNames 投票活动候选人列表
    function createVoting(string memory _title, string memory _description, uint256 _duration, string[] memory _candidateNames) public {
        require(_candidateNames.length>=2, "At least 2 candidates required");

        uint256 votingId = votingCount++;
        Voting storage newVoting = votings[votingId];
        newVoting.id = votingId;
        newVoting.title = _title;
        newVoting.description = _description;
        newVoting.startTime = block.timestamp;
        newVoting.endTime = block.timestamp + _duration;
        newVoting.isActive = true;

        for (uint i=0; i<_candidateNames.length; i++){
            newVoting.candidates.push(Candidate(i, _candidateNames[i], 0));
        }
        /// 触发投票活动创建事件
        emit VotingCreated(votingId, _title);
    }

    /// @notice 投票
    /// @param _votingId 投票活动ID
    /// @param _candidateId 投票候选人ID
    function vote(uint _votingId, uint _candidateId) public{
        Voting storage voting = votings[_votingId];
        require(voting.isActive, "Voting is not active");
        require(block.timestamp <= voting.endTime, "Voting has ended");
        require(!_hasVoted(voting, msg.sender), "You have already voted");

        // 标记该地址已投票
        voting.voters.push(msg.sender);
        // 增加候选人得票数
        voting.candidates[_candidateId].voteCount++;

        emit Voted(_votingId, _candidateId, msg.sender);
    }

    function _hasVoted(Voting storage voting, address voter) internal view returns (bool) {
        for(uint i=0; i<voting.voters.length; i++){
            if(voting.voters[i] == voter){
                return true;
            }
        }
        return false;
    }



    /// @notice 获取投票结果
    /// @param _votingId 投票活动ID
    function getVotingResults(uint _votingId) public view returns (Candidate[] memory){
        return votings[_votingId].candidates;
    }

    /**
     * @notice 获取活跃投票列表（重构后）
     * @return ids 活跃投票的ID数组
     * @return titles 活跃投票的标题数组
     * @return endTimes 活跃投票的结束时间数组
     * @dev 返回精简的投票信息（避免gas过高）
     */
    function getActiveVotings() public view returns (uint256[] memory ids, string[] memory titles, uint256[] memory endTimes) {
        uint256 activeCount = 0;

        // 第一次遍历计算数量
        for (uint256 i = 0; i < votingCount; i++) {
            if (votings[i].isActive && block.timestamp <= votings[i].endTime) {
                activeCount++;
            }
        }

        // 初始化数组
        ids = new uint256[](activeCount);
        titles = new string[](activeCount);
        endTimes = new uint256[](activeCount);

        // 第二次遍历填充数据
        uint256 index = 0;
        for (uint256 i = 0; i < votingCount; i++) {
            Voting storage v = votings[i];
            if (v.isActive && block.timestamp <= v.endTime) {
                ids[index] = v.id;
                titles[index] = v.title;
                endTimes[index] = v.endTime;
                index++;
            }
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
        Voting storage v = votings[_votingId];
        return (
            v.id,
            v.title,
            v.description,
            v.startTime,
            v.endTime,
            v.isActive,
            v.voters.length
        );
    }

}
