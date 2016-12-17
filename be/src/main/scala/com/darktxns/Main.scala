package com.darktxns

import com.darktxns.archiveOrg.DownloadLinkExtractor
import com.darktxns.io.Reader

object Main
{
    def main(args: Array[String]): Unit =
    {
        val rawHtml = Reader readResource "dnmarchives.html"
        val links = new DownloadLinkExtractor(rawHtml).call()

        println("Links: " + links.size)

        links.foreach(println)
    }
}
