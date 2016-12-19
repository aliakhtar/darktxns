package com.darktxns

import com.darktxns.blockchain.BlockchainReader

object Main
{
    def main(args: Array[String]): Unit =
    {
        val env = Environment.build()
        new BlockchainReader
        /*val datasets = new DownloadLinkExtractor(Reader.readResource("dnmarchives.html")).call()

        val task:Task = new S3UploaderMain(env, datasets)
        task.begin()

        while (! task.finished())
        {
            println( task.status() )
            Thread.sleep(10000)
        }*/
    }
}
