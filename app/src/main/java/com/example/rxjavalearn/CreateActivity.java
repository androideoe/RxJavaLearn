package com.example.rxjavalearn;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 创建操作符
 */
public class CreateActivity extends AppCompatActivity {
    private static final String TAG = "RxJava";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        // 1.基础创建操作符 包括just()、fromArray()、fromIterable()、never()、empty()、error()
        // 2.延迟创建操作符 包括defer()、timer()、interval()、intervalRange()、range()、rangeLong()

//        Integer[] intArrays = {1, 2, 3, 4};
//
        // 1. 设置一个集合
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        Observable.just(1,2,3,4)
//        Observable.fromArray(intArrays)
//          Observable.fromIterable(list)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

//        // 延迟订阅
//        Integer i = 1;
//        Observable<Integer> deferObservable
//                = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
//            @Override
//            public ObservableSource<? extends Integer> call() throws Exception {
//                return Observable.just(i);
//            }
//        });
//        i = 10;
//        deferObservable.subscribe(new Observer<Integer>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });

        // timer 延迟发送指定事件，发送一个0用于检测
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, "onNext收到事件："+aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "收到onError事件");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "收到onComplete事件");
                    }
                });

        // interval() 隔一段事件发送事件 从0开始无限+1
        // 第1个参数第一次延迟时间
        // 第2个参数 间隔时间
        // 第3个参数 时间单位
        Observable.interval(2,1,TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, "onNext收到事件："+aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "收到onError事件");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "收到onComplete事件");
                    }
                });
        // intervalRange间隔一段时间，发送指定数量的事件
        // 参数1 事件起点
        // 参数2 发送事件的数量
        // 参数3 第一次间隔时间
        // 参数4 间隔发送的时间
        // 参数5 时间单位
        Observable.intervalRange(2, 6, 2, 2, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, "onNext收到事件："+aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "收到onError事件");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "收到onComplete事件");
                    }
                });
        // range() 连续发送一个事件序列，可指定范围 无延迟 rangeLong() long型
        // 参数1 起始位置
        // 参数2 事件数量
        Observable.range(2,5)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext收到事件："+integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "收到onError事件");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "收到onComplete事件");
                    }
                });

    }
}