package com.yue.ordernow.data

class MenuItemRepository private constructor() {

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: MenuItemRepository? = null

        fun getInstance(): MenuItemRepository =
            instance ?: synchronized(this) {
                instance ?: MenuItemRepository().also { instance = it }
            }
    }
}