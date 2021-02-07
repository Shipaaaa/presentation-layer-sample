@file:Suppress("unused")

package com.redmadrobot.app.infrastructure

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class SchedulersProvider @Inject constructor() {

    open fun ui(): Scheduler = AndroidSchedulers.mainThread()
    open fun io(): Scheduler = Schedulers.io()
    open fun computation(): Scheduler = Schedulers.computation()

}

fun <T> Observable<T>.scheduleIoToUi(schedulers: SchedulersProvider): Observable<T> {
    return subscribeOn(schedulers.io()).observeOn(schedulers.ui())
}

fun <T> Single<T>.scheduleIoToUi(schedulers: SchedulersProvider): Single<T> {
    return subscribeOn(schedulers.io()).observeOn(schedulers.ui())
}

fun Completable.scheduleIoToUi(schedulers: SchedulersProvider): Completable {
    return subscribeOn(schedulers.io()).observeOn(schedulers.ui())
}

fun <T> Maybe<T>.scheduleIoToUi(schedulers: SchedulersProvider): Maybe<T> {
    return subscribeOn(schedulers.io()).observeOn(schedulers.ui())
}


fun <T> Observable<T>.scheduleComputationToUi(schedulers: SchedulersProvider): Observable<T> {
    return subscribeOn(schedulers.computation()).observeOn(schedulers.ui())
}

fun <T> Single<T>.scheduleComputationToUi(schedulers: SchedulersProvider): Single<T> {
    return subscribeOn(schedulers.computation()).observeOn(schedulers.ui())
}

fun Completable.scheduleComputationToUi(schedulers: SchedulersProvider): Completable {
    return subscribeOn(schedulers.computation()).observeOn(schedulers.ui())
}

fun <T> Maybe<T>.scheduleComputationToUi(schedulers: SchedulersProvider): Maybe<T> {
    return subscribeOn(schedulers.computation()).observeOn(schedulers.ui())
}
