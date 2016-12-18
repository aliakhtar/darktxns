package com.darktxns.dnm

import java.io.File

import com.darktxns.dnm.dataset.Dataset

package object download
{
    case class DownloadResult(link:Dataset, file: File)
}
