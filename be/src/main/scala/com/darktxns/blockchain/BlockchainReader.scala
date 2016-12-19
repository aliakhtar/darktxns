package com.darktxns.blockchain

import java.io.File
import java.util

import com.google.bitcoin.params.MainNetParams
import com.google.bitcoin.utils.BlockFileLoader
import com.google.common.collect.Lists

class BlockchainReader
{
    val params = new MainNetParams
    val file = new File("/data/bootstrap.dat")
    val files: util.ArrayList[File] = Lists.newArrayList(file)

    println("Loading blockchain....")
    val loader = new BlockFileLoader(params, files)

    for (i <- 0 to 4)
    {
        val block = loader.next()

        println(s"Got block # $i:  ${block.getHashAsString}, txns: ${block.getTransactions.size()}")

        block.getTransactions.forEach(txn =>
        {
            println(s"Txn: ${txn.getHashAsString}, ${txn.getPurpose}, ${txn.getSigOpCount}")
            println(s"Appears in: ${txn.getAppearsInHashes}")
            txn.getInputs.forEach(i => println(s"Input: ${i.toString}"))
            txn.getOutputs.forEach(o => println(s"Output: ${o.toString}"))
        })
    }
}
