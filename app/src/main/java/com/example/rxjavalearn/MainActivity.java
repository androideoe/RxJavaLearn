package com.example.rxjavalearn;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * RxJava 学习
 * 1.RxJava是基于事件流、异步操作库(是什么）
 * 2.RxJava是一种扩展的观察者模式，被观察者(Observable)通过订阅（subscribe)按顺序发送事件给观察者(Observer)，
 * 观察者按顺序接收并作出对应响应的动作。
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "RxJava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 1.create方式创建被观察者
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                // 发送事件
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        });

        // 2.创建观察者
        Observer<Integer> observer = new Observer<Integer>() {
            // 响应被观察者的事件
            private Disposable disposable;
            @Override
            public void onSubscribe(Disposable d) {
//                Log.d(TAG, "开始采用subscribe连接");
                disposable = d;
            }

            @Override
            public void onNext(Integer value) {
//                Log.d(TAG, "收到onNext事件：" + value);
                // 结束观察方法
                if (value == 2) {
                    disposable.dispose();
                }

            }

            @Override
            public void onError(Throwable e) {
//                Log.d(TAG, "收到onError事件：" + e.getMessage());
            }

            @Override
            public void onComplete() {
//                Log.d(TAG, "收到onComplete事件...");
            }
        };

        // 3.建立连接，订阅
        observable.subscribe(observer);

        // Rxjava链式调用方式
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                // 发送事件
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });





    }
}