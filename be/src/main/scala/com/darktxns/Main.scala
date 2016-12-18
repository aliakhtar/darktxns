package com.darktxns

import java.io.File

import com.darktxns.dnm.download.{DownloadLink, DownloadLinkExtractor}
import com.darktxns.io.Reader

object Main
{
    def main(args: Array[String]): Unit =
    {
        val env = Environment.build()

        println(env.toString)

        val datasets = new DownloadLinkExtractor(Reader.readResource("dnmarchives.html")).call()
        val total = datasets.size
        var done = 0
        datasets.foreach(d =>
        {
            val unzipped = new File("raw/" + d.fileName.replace(DownloadLink.EXTENSION, "") )
            println( unzipped.getAbsolutePath )
            done+= 1
        })

        println(s"Traveresed $done / $total")


        /*val task:Task = new DownloaderMain
        task.begin()

        while (! task.finished())
        {
            println( task.status() )
            Thread.sleep(10000)
        }*/
    }
}
