package com.darktxns.archiveOrg

object DownloadLink
{
    val DOWNLOAD_DIR = "https://archive.org/download/dnmarchives/"
    val EXTENSION = ".tar.xz"
}

class DownloadLink(fileName: String)
{
    val fullUrl:String = DownloadLink.DOWNLOAD_DIR + fileName
    val marketName:String = parseMarketName(fileName)


    private def parseMarketName(fileName:String):String =
    {
        val processed = fileName
            .trim
            .substring(0, fileName.length - DownloadLink.EXTENSION.length) //Parse out the extension
            .toLowerCase
            .replace("market", " Market")
            .capitalize

        if (! processed.contains("-"))
            return processed

        processed.splitAt( processed.indexOf("-") )._1
    }


    override def toString = s"DownloadLink($fullUrl, $marketName)"
}
