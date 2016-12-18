package com.darktxns

import com.darktxns.dnm.download.DownloaderMain

object Main
{
    def main(args: Array[String]): Unit =
    {
        val task:Task = new DownloaderMain
        task.begin()

        while (! task.finished())
        {
            println( task.status() )
            Thread.sleep(10000)
        }
    }
}
