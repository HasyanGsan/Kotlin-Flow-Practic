package ua.cn.stu.foundation.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.takeWhile

private sealed class Element<T>

private class ItemElement<T>(
    val item: T
) : Element<T>()

private class ErrorElement<T>(
    val error: Throwable
) : Element<T>()

private class CompletedElement<T> : Element<T>()

fun <T> Flow<T>.finiteShareIn(coroutineScope: CoroutineScope) : Flow<T> {
    return this
        .map<T, Element<T>> { item -> ItemElement(item) }
        .onCompletion {
            emit(CompletedElement())
        }
        .catch { exception ->
            emit(ErrorElement(exception))
        }
        .shareIn(coroutineScope, SharingStarted.Eagerly, 1)
        .map {
           if(it is ErrorElement) throw it.error
               return@map it
        }
        .takeWhile { it is ItemElement }
        .map { (it as ItemElement).item }
}
