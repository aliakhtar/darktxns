package com.darktxns

trait Task
{
    def begin():Unit

    def finished():Boolean

    def status():String
}
