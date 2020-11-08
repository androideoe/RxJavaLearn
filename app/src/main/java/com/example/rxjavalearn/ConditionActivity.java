package com.example.rxjavalearn;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;


/**
 * 条件/判断操作符
 */
public class ConditionActivity extends AppCompatActivity {
    private static final String TAG = "RxJava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);


        // all() 判断发送的每项数据是否都满足条件 满足 ture 不满足 false
        // takeWhile() 判断发送的每项数据是否都满足条件 满足 发送 不满足停止发送
        // skipWhile() 判断发送的每项数据是否都满足条件 不满足开始发送
        // takeUntil() 传人的Observable发送数据 第一个Observable停止发送数据
        // skipUntil() 传人的Observable发送数据 第一个Observable开始发送数据
        // SequenceEqual() 判断两个Observable发送数据是否相同 相同返回true 反之false
        // contains() 判断发送的数据包含指定数据 包含true 反之false
        // isEmpty() 判断发送的数据是否为空 若为空返回true 反之false
        // amb() 发送多个Observable，只发送先发送的Observable数据 其余丢弃
        // defaultIfEmpty() 在不发送next事件下，仅发送一个complete事件的前提下，发送一个默认值


        // all()
        Observable.just(1, 2, 3, 4)
                .all(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        // 发送的数据都<=5 返回true
                        return integer <= 5;
                    }
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean result) throws Exception {
                Log.d(TAG, "result :" + result);
            }
        });

        // takeWhile()
        Observable.interval(1, TimeUnit.SECONDS)
                .takeWhile(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        // <3才开始发送数据
                        return (aLong < 3);
                    }
                }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {
                Log.d(TAG, "收到onNext事件：" + aLong);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "收到onError事件：" + e.toString());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "收到onComplete事件");
            }
        });

        // skipWhile()
        Observable.interval(1,TimeUnit.SECONDS)
                .skipWhile(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
//         >5才开始发送数据
                        return aLong<=5;
                    }
                }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {
                Log.d(TAG, "收到onNext事件：" + aLong);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "收到onError事件：" + e.toString());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "收到onComplete事件");
            }
        });

        // takeUntil()
        Observable.interval(1, TimeUnit.SECONDS)
                .takeUntil(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        // >3停止发送数据
                        return aLong > 3;
                    }
                }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {
                Log.d(TAG, "收到onNext事件：" + aLong);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "收到onError事件：" + e.toString());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "收到onComplete事件");
            }
        });

        // skipUntil()
        Observable.interval(1, TimeUnit.SECONDS)
                // 传入的Observable发送数据，第一个Observable才发送数据
                .skipUntil(Observable.interval(5,1,TimeUnit.SECONDS))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, "收到onNext事件：" + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "收到onError事件：" + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "收到onComplete事件");
                    }
                });

        // SequenceEqual()
        Observable.sequenceEqual(
                Observable.just(1,2,3),
                Observable.just(1,2,3)
        ).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean result) throws Exception {
                Log.d(TAG, "result ：" + result);
            }
        });

//         contains()
        Observable.just(1,2,4,7)
                .contains(4)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        Log.d(TAG, "result ：" + result);
                    }
                });

        // isEmpty()
        Observable.just(1, 2, 4, 7)
                .isEmpty()
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        Log.d(TAG, "result ：" + result);
                    }
                });

        // amb()
        List<Observable<Integer>> observableList = new ArrayList<>();
        Observable<Integer> observable1 = Observable.just(1,2,3,4);
        Observable<Integer> observable2 = Observable.just(5,7,8).delay(1,TimeUnit.SECONDS);
        observableList.add(observable1);
        observableList.add(observable2);

        Observable.amb(observableList)
                .subscribe(new Consumer<Integer>() {
                   // 只发送先发送的数据，后发送的舍弃
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "接收的数据 ：" + integer);
                    }
                });

        // defaultIfEmpty()
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                emitter.onNext(1);
//                emitter.onNext(2);
                // 只发送Complete事件
                emitter.onComplete();

            }
        }).defaultIfEmpty(11)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "收到onNext事件：" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "收到onError事件：" + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "收到onComplete事件");
                    }
                });
    }
}