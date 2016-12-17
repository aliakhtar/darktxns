package com.darktxns

import com.darktxns.dnm.{DownloadLinkExtractor, Downloader}
import com.darktxns.io.Reader

object Main
{
    def main(args: Array[String]): Unit =
    {
        val rawHtml = Reader readResource "dnmarchives.html"
        val links = new DownloadLinkExtractor(rawHtml).call()

        println("Starting download..")

        val toDl = links.find(l => l.fileName == "zanzibarspice.tar.xz").get

        val dest = new Downloader(toDl).call()

        Runtime.getRuntime.exec(s"tar -xvf ${dest.getAbsolutePath}").waitFor()

        println("Unzipped? ")

    }
}
