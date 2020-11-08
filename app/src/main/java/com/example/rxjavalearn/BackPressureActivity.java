package com.example.rxjavalearn;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.rxjavalearn.rxbus.RxBus;
import com.example.rxjavalearn.rxbus.RxTestBean;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * rxjava 背压策略
 * 产生的原因 解决异步订阅时 被观察者发送的事件速度和观察者响应事件速度严重不匹配的问题
 * 控制事件流速的策略
 * <p>
 * 避免出现 1.控制接收者速度（根据自身条件响应事件流） 2.控制被观察者的速度（反向控制被观察者发送速度）
 * 已经出现处理 缓存区 对超出缓存区大小的事件进行丢弃、保留、报错的措施 Flowable
 */
public class BackPressureActivity extends AppCompatActivity {
    private static final String TAG = "RxJava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_pressure);

        // 模拟出现背压现象
//        backPressureSample();


        // 使用Flowable解决背压问题
//        flowable();

        // 背压request、requestd方法
//        backPressureReq();
        startActivity(new Intent(BackPressureActivity.this, RxBusActivity.class));


    }

    private void backPressureReq() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                for (int j = 0; j < 15; j++) {
                    e.onNext(j);
                    Log.i(TAG, e.requested() + " 发送数据：" + j);
                    try {
                        Thread.sleep(50);
                    } catch (Exception ex) {
                    }
                }
            }
        }, BackpressureStrategy.BUFFER)
//        .subscribeOn(Schedulers.newThread())
//        .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    private Subscription subscription;

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscription = s;
                        s.request(10); // 观察者设置接收事件的数量,如果不设置接收不到事件
                    }

                    @Override
                    public void onNext(Integer integer) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.e(TAG, "onNext : " + (integer));
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, "onError : " + t.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete");
                    }
                });
    }

    private void backPressureSample() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                int i = 0;
                while (true) {
                    Thread.sleep(500);
                    i++;
                    e.onNext(i);
                    Log.i(TAG, "每500ms发送一次数据：" + i);
                }
            }
        }).subscribeOn(Schedulers.newThread())// 使被观察者存在独立的线程执行
                .observeOn(Schedulers.newThread())// 使观察者存在独立的线程执行
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Thread.sleep(5000);
                        Log.e(TAG, "每5000ms接收一次数据：" + integer);
                    }
                });
    }

    public void flowable() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                for (int j = 0; j <= 150; j++) {
                    e.onNext(j);
                    Log.i(TAG, " 发送数据：" + j);
                    try {
                        Thread.sleep(50);
                    } catch (Exception ex) {
                    }
                }
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(20); // 观察者设置接收事件的数量,如果不设置接收不到事件
                    }

                    @Override
                    public void onNext(Integer integer) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.e(TAG, "onNext : " + (integer));
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, "onError : " + t.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete");
                    }
                });
    }


}