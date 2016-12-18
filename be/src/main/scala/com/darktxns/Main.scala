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
            Thread.sleep(1000)
        }
    }
}
