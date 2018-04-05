package com.cabbage.fireticv2

import timber.log.Timber

/**
 * Created by Leo on 2017-02-27.
 * This tree will print to logcat, with line number in front of message.
 */
class ForestFire : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String {
        return element.fileName + ":" + element.lineNumber
//        return super.createStackElementTag(element) + ':' + element.lineNumber
    }
}