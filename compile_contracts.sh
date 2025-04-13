#!/bin/bash

# 定义路径
CONTRACTS_DIR="src/main/resources/contracts"   # 存放.sol文件的目录
OUTPUT_DIR="src/main/resources/contracts/build"          # 目标输出目录

# 清空旧编译结果（可选）
# shellcheck disable=SC2115
rm -rf $OUTPUT_DIR/*
mkdir -p $OUTPUT_DIR

# 遍历所有合约文件并编译
# shellcheck disable=SC2045
for contract in $(ls $CONTRACTS_DIR/*.sol); do
  contract_name=$(basename "$contract" .sol)

  # 编译生成ABI和BIN
  solc --abi --bin $contract -o $OUTPUT_DIR --overwrite
  rm -rf $OUTPUT_DIR/"$contract_name".sol # 清理多余目录
done
