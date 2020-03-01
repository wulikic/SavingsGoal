package com.vesna.roundup.presentation.weekselection

import androidx.lifecycle.ViewModel
import com.vesna.roundup.domain.errors.FetchingAccountFailed
import com.vesna.roundup.domain.model.Period
import com.vesna.roundup.domain.usecase.GetAllWeeksSinceAccountCreation
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.joda.time.DateTime

class WeekSelectionViewModel(
    getWeeks: GetAllWeeksSinceAccountCreation
) : ViewModel() {

    private val states = BehaviorSubject.create<List<Period>>() // TODO add in progress state
    private val events = PublishSubject.create<WeekSelectionEvent>() // TODO more generic error

    private val disposables = CompositeDisposable()

    init {
        disposables.add(
            getWeeks.execute(DateTime.now())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { states.onNext(it) },
                    { e ->
                        if (e is FetchingAccountFailed) {
                            events.onNext(ProblemFetchingAccount)
                        } else {
                            events.onNext(GenericError)
                        }
                    })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun states(): Observable<List<Period>> = states
    fun events(): Observable<WeekSelectionEvent> = events
}