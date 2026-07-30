package com.michael.playlistmaker.data.search

interface StorageClient<T> {
    fun storeData(data: T)
    fun getData(): T?
}