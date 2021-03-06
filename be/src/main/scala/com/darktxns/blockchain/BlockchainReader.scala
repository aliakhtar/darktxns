package com.darktxns.blockchain

import java.io.File
import java.util

import com.google.bitcoin.params.MainNetParams
import com.google.bitcoin.utils.BlockFileLoader
import com.google.common.collect.Lists

import scala.util.control.Breaks._

class BlockchainReader
{
    val params = new MainNetParams
    val file = new File("/data/bootstrap.dat")
    val files: util.ArrayList[File] = Lists.newArrayList(file)

    println("Loading blockchain....")
    val loader = new BlockFileLoader(params, files)

    for (i <- 0 to 100000)
    {
        val block = loader.next()

        if (block.getTransactions.size() > 1)
        {

            println(s"Got block # $i:  ${block.getHashAsString}, txns: ${block.getTransactions.size()}")

            block.getTransactions.forEach(txn =>
            {
                println(s"Txn: ${txn.getHashAsString}, ${txn.getPurpose}, ${txn.getSigOpCount}")
                println(s"Appears in: ${txn.getAppearsInHashes}")

                txn.getInputs.forEach(i => println(s"Input: ${i.toString}"))
                txn.getOutputs.forEach(o =>
                {
                    println(s"Output: ${o.getValue}, ${o.getSpentBy}")
                    if (o.getScriptPubKey == null || o.getScriptPubKey.getChunks == null)
                        break

                    if (o.getScriptPubKey.isSentToAddress || o.getScriptPubKey.isSentToP2SH)
                        println(s"To: ${o.getScriptPubKey.getToAddress(params)}")

                    try
                    {
                        println(s"from: ${o.getScriptPubKey.getFromAddress(params)}")
                    }
                    catch
                    {
                        case e:Exception => println(e.toString)
                    }
                    println(s"Amount: ${o.getValue}")
                })
            })
        }
    }
}
