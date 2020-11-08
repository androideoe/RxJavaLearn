package com.example.rxjavalearn;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.schedulers.IoScheduler;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 组合、合并操作符
 */
public class MergeActivity extends AppCompatActivity {
    private static final String TAG = "RxJava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merge);


//        // 组合、合并操作符
//        // 1.组合多个被观察者 concat()  concatArray() merge() mergeArray() concatDelayError() mergeDelayError()
//        // 2.合并多个事件 Zip() CombineLatest() CombineLatestDelayError() reduce() collect()
//        // 3.发送事件前追加事件 startWith() startWithArray()
//        // 4.统计发送数量 count()
//
//        // concat() 组合多个多个被观察者一起发送数据，合并后按顺序串行执行 被观察者数量<=4个
//        // concatArray()可>4个
//        Observable.concat(
//                Observable.just(1,2,3),
//                Observable.just(4,5,6),
//                Observable.just(7,8,9),
//                Observable.just(10,11,12)
//
//        ).subscribe(new Observer<Integer>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                Log.d(TAG, "开始采用subscribe连接");
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                Log.d(TAG, "onNext收到事件："+integer);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d(TAG, "收到onError事件");
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "收到onComplete事件");
//            }
//        });
//
//        // merge() 组合多个多个被观察者一起发送数据，合并后按时间并行执行 被观察者数量<=4个
//        // mergeArray()可>4个
//        Observable.merge(
//                Observable.intervalRange(0, 3, 1, 1, TimeUnit.SECONDS),
//                Observable.intervalRange(2, 3, 1, 1, TimeUnit.SECONDS)
//        ).subscribe(new Observer<Long>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                Log.d(TAG, "开始采用subscribe连接");
//            }
//
//            @Override
//            public void onNext(Long aLong) {
//                Log.d(TAG, "onNext收到事件："+aLong);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d(TAG, "收到onError事件");
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "收到onComplete事件");
//            }
//        });
//
//        // 使用concat()、merge()时其中一个被观察者收到onError事件，其他事件立马终止发送
//        // 使用concatDelayError() mergeDelayError() 推迟到其他事件发送完成后才响应onError
//        Observable.concatArrayDelayError(
//                Observable.create(new ObservableOnSubscribe<Integer>() {
//                    @Override
//                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                        emitter.onNext(1);
//                        emitter.onNext(2);
//                        emitter.onNext(3);
//                        emitter.onError(new NullPointerException());
//                        emitter.onComplete();
//                    }
//                }),
//                Observable.just(4, 5, 6)).subscribe(new Observer<Integer>() {
//
//            @Override
//            public void onSubscribe(Disposable d) {
//                Log.d(TAG, "开始采用subscribe连接");
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                Log.d(TAG, "收到onNext事件：" + integer);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d(TAG, "收到onError事件: " + e.getMessage());
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "收到onComplete事件");
//            }
//        });

        // zip() 对多个被观察者的事件进行合并处理 生成一个新的事件并发送 新事件数量和合并前数量少的一致
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                Log.d(TAG, "被观察者1发送事件1");
//                Thread.sleep(1000);

                emitter.onNext(2);
                Log.d(TAG, "被观察者1发送事件2");
//                Thread.sleep(1000);

                emitter.onNext(3);
                Log.d(TAG, "被观察者1发送事件3");
//                Thread.sleep(1000);

                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io());


        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("A");
                Log.d(TAG, "被观察者2发送事件A");
//                Thread.sleep(1000);

                emitter.onNext("B");
                Log.d(TAG, "被观察者2发送事件B");
//                Thread.sleep(1000);

                emitter.onNext("C");
                Log.d(TAG, "被观察者2发送事件C");
//                Thread.sleep(1000);

                emitter.onNext("D");
                Log.d(TAG, "被观察者2发送事件D");
//                Thread.sleep(1000);

                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());

        // BiFunction 3个参数 合并前两个类型 合并后类型
        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer i, String string) throws Exception {
                return i + string;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "开始采用subscribe连接");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "收到合并后的事件：= " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "收到onError事件: " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "收到onComplete事件");
            }
        });

        // combineLatest() 与zip()不同 按时间合并，同一个时间点合并
        // 先发送数据Observable最后一个数据和另外一个Observable发送的每个数据结合
        Observable.combineLatest(
                Observable.just(1L, 2L, 3L),
                Observable.intervalRange(1, 4, 1, 1, TimeUnit.SECONDS),
                new BiFunction<Long, Long, Long>() {
                    @Override
                    public Long apply(Long l1, Long l2) throws Exception {
                        Log.d(TAG, "合并前的数据：" + l1 + " " + l2);
                        return l1 + l2;
                    }
                }

        ).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long along) throws Exception {
                Log.d(TAG, "合并后的的数据：" + along);
            }
        });

        // combineLatestDelayError和 concatDelayError() mergeDelayError()类似

        // reduce() 聚合的逻辑根据需求编写，本质前两个数据聚合，然后和后一个数据聚合，依次类推
        Observable.just(1, 2, 3, 4)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer i1, Integer i2) throws Exception {
                        Log.d(TAG, "reduce前的数据：" + i1 + " " + i2);
                        return i1 + i2;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "reduce后的数据：" + integer);
            }
        });

        // collect() 将Observable的数据发送到一个数据结构中
        Observable.just(1, 2, 3, 4, 5, 6)
                // 1.创建数据结构
                .collect(new Callable<ArrayList<Integer>>() {

                    @Override
                    public ArrayList<Integer> call() throws Exception {
                        return new ArrayList<>();
                    }
                    // 2.收集数据到数据结构中
                }, new BiConsumer<ArrayList<Integer>, Integer>() {
                    @Override
                    public void accept(ArrayList<Integer> list, Integer integer) throws Exception {
                        Log.d(TAG, "collect before ：" + integer);
                        list.add(integer);
                    }
                }).subscribe(new Consumer<ArrayList<Integer>>() {
            @Override
            public void accept(ArrayList<Integer> list) throws Exception {
                Log.d(TAG, "collect after ：" + list);
            }
        });

        // startWith() startWithArray() 发送数据前，追加事件
        Observable.just(4, 5, 6)
                .startWith(1) // 追加单个数据
                .startWithArray(2, 3, 4) // 追加多个数据
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始收到onSubscribe事件");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "收到onNext = " + integer);
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

        // count()统计发送数量
        Observable.just(1,3,3,4)
                .count()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "发送事件的数量count = " + aLong);
                    }
                });

    }
}