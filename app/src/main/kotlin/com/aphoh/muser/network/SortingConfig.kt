package com.aphoh.muser.network

import com.aphoh.muser.network.SortingConfig.Category.*

/**
 * Created by Will on 6/22/16.
 */

class SortingConfig private constructor(val category: Category, val time: Time?){

    enum class Category(val string: String){
        HOT("hot"),
        NEW("new"),
        RISING("rising"),
        TOP("top"),
        RANDOM("random")
    }

    enum class Time(val string: String){
        DAY("day"),
        WEEK("week"),
        MONTH("month"),
        YEAR("year"),
        ALL("all")
    }



    companion object{
        fun hot() = SortingConfig(HOT, null)
        fun newest() = SortingConfig(NEW, null)
        fun rising() = SortingConfig(RISING, null)
        fun top(time: Time) = SortingConfig(TOP, time)
        fun random() = SortingConfig(RANDOM, null)
    }

}