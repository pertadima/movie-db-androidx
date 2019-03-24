package id.co.moviedb.base

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.co.moviedb.networking.NetworkError
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by pertadima on 24,March,2019
 */

class BaseViewModel: ViewModel() {
    private val disposables = CompositeDisposable()

    private val isLoading = MutableLiveData<Boolean>()
    fun observeLoading(): LiveData<Boolean> = isLoading

    protected val isError = MutableLiveData<NetworkError?>()
    fun observeError(): LiveData<NetworkError?> = isError

    protected val isEmptyData = MutableLiveData<Boolean>()
    fun observeEmptyData(): LiveData<Boolean> = isEmptyData

    private fun launch(job: () -> Disposable) {
        disposables.add(job())
    }

    protected fun <T> Single<T>.onResult(action: (T) -> Unit, error: (NetworkError) -> Unit) {
        launch {
            this
                .doOnSubscribe {
                    isLoading.postValue(true)
                }
                .doOnError {
                    isLoading.postValue(false)
                }
                .doOnSuccess {
                    isLoading.postValue(false)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                    { result ->
                        action.invoke(result)
                        isLoading.postValue(false)
                    },
                    { err ->
                        error.invoke(NetworkError(err))
                        isLoading.postValue(false)
                    }
                )
        }
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}