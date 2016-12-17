package com.darktxns.dnm.download

import com.darktxns.dnm.DatasetType
import com.darktxns.dnm.DatasetType.DatasetType

object DownloadLink
{
    /* The parsed links are just the file names, they are appended to the below dir to get the full download URL */

    val DOWNLOAD_DIR = "https://archive.org/download/dnmarchives/"
    val EXTENSION = ".tar.xz"
}

class DownloadLink(val fileName: String)
{
    /**
      * Full url to download the file, e.g http://.....xz
      */
    val fullUrl:String = DownloadLink.DOWNLOAD_DIR + fileName

    /**
      * Human readable identifier of the market.
      */
    val marketName:String = parseMarketName(fileName)
    val guessedType:DatasetType = parseType(fileName)


    /**
      * Parses out the human readable market name from the fileName.
      *
      * - is used as the word separator. There are names like agora-forums, agora-markets, etc.
      *
      * The following method just parses out the first word before -, capitalizes the first letter,
      * and returns it. E>g for the above, it would return Agora.
      *
      * Some market names contain 'market', e.g 'Foomarket'. These are split into two words, i.e 'Foo Market'
      */
    private def parseMarketName(fileName:String):String =
    {
        if (fileName == "2015-sr2doug-claimedsr2leaks.tar.xz") //This dataset is named inconsistently from others,
            return "SilkRoad2"                                 // hence needs its own exception

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

    /**
      * If the filename contains -forum , consider it a forum dataset, otherwise, market
      */
    private def parseType(fileName:String):DatasetType =
    {
        if (! fileName.toLowerCase().contains("forum"))
            DatasetType.Market
        else
            DatasetType.Forum

    }


    override def toString = s"DownloadLink($fullUrl, $marketName, $guessedType, $fileName)"
}
