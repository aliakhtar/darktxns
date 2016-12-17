package com.darktxns.dnm.download

import com.darktxns.io.Reader

object DownloaderMain
{
    def begin():Unit =
    {
        val rawHtml = Reader readResource "dnmarchives.html"
        val links = new DownloadLinkExtractor(rawHtml).call()

        println("Starting download..")

        val toDl = links.find(_.fileName == "zanzibarspice.tar.xz").get

        val dest = new Downloader(toDl).call()

        Runtime.getRuntime.exec(s"tar -xvf ${dest.getAbsolutePath}").waitFor()

        println("Unzipped? ")
    }
}
