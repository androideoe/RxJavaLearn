package com.example.rxjavalearn;

import android.os.Bundle;
import android.util.Log;

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
 * 过滤性操作符
 */
public class FilterActivity extends AppCompatActivity {
    private static final String TAG = "RxJava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);


        // 1.根据指定条件过滤事件 Filter() ofType() skip() skipLast() distinct() distinctUntilChange()
        // 2.根据指定事件数量过滤事件 take() takeLast()
        // 3.根据指定时间过滤事件 throttleFirst() throttleLast() sample() throttleWithTimeOut() debounce()
        // 4.根据指定事件位置过滤事件 firstElement() lastElement() elementAt() elementAtOrError()


        // Filter() 过滤指定条件的事件
        Observable.just(1, 2, 3, 4, 5)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 3;
                    }

                    // a. 返回true，则继续发送
                    // b. 返回false，则不发送（即过滤）

                }).subscribe(new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer value) {
//                Log.d(TAG, "过滤后得到的事件是：" + value);
            }

            @Override
            public void onError(Throwable e) {
//                Log.d(TAG, "对Error事件作出响应");
            }

            @Override
            public void onComplete() {
//                Log.d(TAG, "对onComplete事件作出响应");
            }
        });

        // ofType() 过滤特定类型的数据
        Observable.just(1, "hello", 3, "world", 5)
                .ofType(String.class) // 筛选出 整型数据
                .subscribe(new Consumer<String>() {

                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "过滤后得到的事件是：" + s);
                    }
                });

        // skip()/skipLast() 跳过某个事件
        Observable.just(1, 2, 3, 4, 5)
                .skip(2) // 跳过正序前两项
                .skipLast(2) // 跳过倒序后两项
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "过滤后得到的事件是：" + integer);
                    }
                });

        // distinct() / distinctUntilChange() 过滤重复/连续重复事件
        Observable.just(1, 3, 4, 4, 3, 2)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "过滤重复的事件：" + integer);
                    }
                });
        // 执行结果: 1 3 4 2

        Observable.just(1, 3, 4, 4, 3, 3, 2, 5, 5)
                .distinctUntilChanged()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "过滤连续重复的事件：" + integer);
                    }
                });
        // 执行结果: 1 3 4 3 2 5


        // take()指定观察者最多能接收到的事件数量
        Observable.just(1, 2, 3, 4, 5)
                .take(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "过滤连续得到的事件：" + integer);
                    }
                });
        // 执行结果: 1 2

        // takeLast() 指定观察者接收最后几个事件
        Observable.just(1, 2, 3, 4, 5)
                .takeLast(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "过滤连续得到的事件：" + integer);
                    }
                });

        // throttleFirst()/throttleLast() 某段时间只接收观察者第一个/最后一个事件
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                Thread.sleep(500);
                emitter.onNext(4);
                Thread.sleep(1000);
                emitter.onNext(2);
                Thread.sleep(500);
                emitter.onNext(3);
                Thread.sleep(500);
            }
        }).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "throttleFirst过滤连续得到的事件：" + integer);
                    }
                });
        // 执行结果： 1 3

        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                Thread.sleep(500);
                emitter.onNext(4);
                Thread.sleep(1000);
                emitter.onNext(2);
                Thread.sleep(500);
                emitter.onNext(3);
                Thread.sleep(500);
            }
        }).throttleLast(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "throttleLast过滤连续得到的事件：" + integer);
                    }
                });
        // 执行结果： 2 3

        // sample() 发送该段事件最后一次事件 和throttleLast类似

        // throttleWithTimeOut() / debounce()
        // 发送数据事件时 若两次发送的事件时间间隔<小于指定时间 就会丢弃前一次的数据 直到指定时间间隔没有发送新数据时才发送最后一次数据
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1); // 1、4发送事件小于1s 1 抛弃
                Thread.sleep(500);
                emitter.onNext(4);//  发送事件间隔>1s 发送4 然后2 、3发送<1s 2抛弃
                Thread.sleep(1500);
                emitter.onNext(2);// 最后发送3
                Thread.sleep(500);
                emitter.onNext(3);
            }
        }).throttleWithTimeout(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "throttleWithTimeout过滤连续得到的事件：" + integer);
                    }
                });

        // 执行结果： 4 3

        // firstElement/lastElement 选取第一个元素/最后一个元素
        Observable.just(1, 2, 3, 5, 0)
//                .firstElement()
                .lastElement()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "firstElement过滤连续得到的事件：" + integer);
                    }
                });

        // 执行结果： firstElement() 1/lastElement() 0

        // elementAt() 获取索引位置的元素（允许越界）/elementAtOrError() 索引越界抛异常
        Observable.just(1, 3, 5, 6, 8)
//                .elementAt(2)
                .elementAt(6, 9) // 越界后默认9
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "elementAt过滤连续得到的事件：" + integer);
                    }
                });

        // 执行结果： elementAt() 5/越界 9

        Observable.just(1, 3, 5, 6, 8)
                .elementAtOrError(6)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "elementAt过滤连续得到的事件：" + integer);
                    }
                });

        // 执行结果： elementAtOrError() 抛异常


    }
}