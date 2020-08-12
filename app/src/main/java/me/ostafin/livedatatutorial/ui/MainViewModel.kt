package me.ostafin.livedatatutorial.ui

import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxrelay3.PublishRelay
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.*
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import me.ostafin.livedatatutorial.base.BaseViewModel
import me.ostafin.livedatatutorial.util.Event
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

class MainViewModel @Inject constructor(
    private val id: Long,
    private val bookRepository: BookRepository
) : BaseViewModel() {

    private val _showToast: MutableLiveData<String> = MutableLiveData()
    val showToast: LiveData<String> = _showToast

    private val _navigation: MutableLiveData<Event<Navigation>> = MutableLiveData()
    val navigation: LiveData<Event<Navigation>> = _navigation

    private val _mainItems: MutableLiveData<List<MainItem>> = MutableLiveData()
    val mainItems: LiveData<List<MainItem>> = _mainItems

    private val _progressBarVisibility: MutableLiveData<Boolean> = MutableLiveData()
    val progressBarVisibility: LiveData<Boolean> = _progressBarVisibility

    private val getBooksButtonClickedRelay: PublishRelay<Unit> = PublishRelay.create()

    private val navigationRelay: PublishRelay<Navigation> = PublishRelay.create()
    val navigationObs: PublishRelay<Navigation> = navigationRelay

    init {
//        _navigation.postEventWithValue(Navigation.MainActivity)
        navigationRelay.accept(Navigation.MainActivity)
    }

    fun <T> MutableLiveData<Event<T>>.postEventWithValue(value: T) {
        this.value = Event(value)
    }

    override fun onInitialize() {
        navigationRelay.accept(Navigation.MainActivity)
        observeButtonClicks()

        RxJavaPlugins.setErrorHandler {
            Log.e("ELO UNHANDLED", it.message, it)
        }
    }

    private fun observeButtonClicks() {
        getBooksButtonClickedRelay
            .flatMapFirstSingle {
                getBookItems().handleProgress(_progressBarVisibility).asSafeApiCall()
            }
            .subscribeBy(
                onNext = {
                    when (it) {
                        is SafeApiCall.Success -> {
                            _mainItems.postValue(it.data)
                        }
                        is SafeApiCall.Error -> {
                            Log.e("ELO", it.throwable.message, it.throwable)
                        }
                    }
                }
            )
            .addTo(disposables)
    }

    private fun <T, R> Observable<T>.flatMapFirstSingle(block: (T) -> Single<R>): Observable<R> {
        return this
            .toFlowable(BackpressureStrategy.DROP)
            .flatMapSingle({ block(it) }, false, 1)
            .toObservable()
    }

    sealed class SafeApiCall<T> {
        data class Success<T>(val data: T) : SafeApiCall<T>()
        data class Error<T>(val throwable: Throwable) : SafeApiCall<T>()

        val isSuccess: Boolean
            get() = this is Success
    }

    fun <T> Single<T>.asSafeApiCall(): Single<SafeApiCall<T>> {
        return this
            .map { SafeApiCall.Success(it) as SafeApiCall<T> }
            .onErrorReturn { SafeApiCall.Error(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun <T> Observable<T>.asSafeCall(): Observable<SafeApiCall<T>> {
        return this
            .map { SafeApiCall.Success(it) as SafeApiCall<T> }
            .onErrorReturn { SafeApiCall.Error(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun bookItemClicked(item: MainItem) {
        Log.d("ELO", "item clicked")
    }

    fun favoriteButtonClicked(item: MainItem) {
        Log.d("ELO", "item fav clicked")
    }

    private fun getBookItems(): Single<List<MainItem>> {
        return Singles
            .zip(
                ApiService.getBooks(),
                ApiService.getFavorites()
            ) { books, favorites ->
                books.map {
                    val isFav = Random.nextBoolean()

                    if (isFav) {
                        MainItem.FavItem(
                            id = it.id,
                            title = it.title
                        )
                    } else {
                        MainItem.NotFavItem(
                            id = it.id,
                            title = it.title
                        )
                    }

                }
            }
    }

    fun <T> Single<T>.handleProgress(mutableLiveData: MutableLiveData<Boolean>): Single<T> {
        return this
            .doOnSubscribe {
                mutableLiveData.postValue(true)
            }
            .doAfterTerminate {
                mutableLiveData.postValue(false)
            }
    }

    fun <T> Observable<T>.handleProgress(mutableLiveData: MutableLiveData<Boolean>): Observable<T> {
        return this
            .doOnSubscribe {
                mutableLiveData.postValue(true)
            }
            .doAfterTerminate {
                mutableLiveData.postValue(false)
            }
    }

    fun navigationButtonClicked() {
        _navigation.postEventWithValue(Navigation.MainActivity)
    }

    fun buttonClicked() {
        getBooksButtonClickedRelay.accept(Unit)
    }

    sealed class Navigation {
        object MainActivity : Navigation()
    }

}

object ApiService {

    fun getBooks(): Single<List<Book>> {
        Log.d("ELO", "isMainThread = ${isMainThread()}")
        return Single.just(
            listOf(
                Book(id = 1, title = "one"),
                Book(id = 2, title = "two")
            )
        ).delay(2, TimeUnit.SECONDS)
    }

    fun getFavorites(): Single<List<Long>> {
        return Single.just(listOf(1L)).delay(1, TimeUnit.SECONDS)
    }

}

fun isMainThread() = Looper.myLooper() == Looper.getMainLooper()

data class Book(val id: Long, val title: String)

sealed class MainItem {
    data class FavItem(
        val id: Long,
        val title: String
    ) : MainItem()

    data class NotFavItem(
        val id: Long,
        val title: String
    ) : MainItem()
}