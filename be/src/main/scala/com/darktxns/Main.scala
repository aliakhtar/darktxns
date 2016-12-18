package com.darktxns

import com.darktxns.dnm.download.DownloaderMain

object Main
{
    def main(args: Array[String]): Unit =
    {
        val downloader = new DownloaderMain
        downloader.begin()

        while (! downloader.finished())
        {
            Thread.sleep(1000)
        }
    }
}
