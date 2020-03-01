package com.vesna.roundup.weeks

import androidx.lifecycle.ViewModel
import com.vesna.roundup.domain.GetAllWeeksSinceAccountCreation
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class MainViewModel(getWeeks: GetAllWeeksSinceAccountCreation) : ViewModel() {

    private val states = BehaviorSubject.create<State>()
    private val events = PublishSubject.create<Event>()

    private val disposables = CompositeDisposable()

    init {
        disposables.add(
            getWeeks.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ states.onNext(State(it))}, { /* TODO */})
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun states(): Observable<State> = states
    fun events(): Observable<Event> = events
}