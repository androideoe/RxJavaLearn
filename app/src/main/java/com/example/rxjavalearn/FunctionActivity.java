package com.example.rxjavalearn;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * 功能操作符
 */
public class FunctionActivity extends AppCompatActivity {
    private static final String TAG = "RxJava";

    // 设置变量 = 模拟轮询服务器次数
    private int i = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuction);


        // 订阅 subscribe()
        // 线程调度 subscribeOn() ObserveOn()
        // 延迟操作 delay()
        // 在事件生命周期中的操作 do()
        // 错误处理 onErrorRuturn() onErrorRusumeNext() onExceptionResumeNext() retry() retryUntil() retryWhen()
        // 重复发送操作 repeat() repeatWhen()

        // 被观察者Observable 订阅 subcribe() Observer观察者
        // subscribeOn() 被观察者执行的线程 第一次有效
        // ObserveOn() 观察者执行的线程 每一次线程切换都有效
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("111");
                emitter.onNext("222");
                emitter.onNext("333");
                emitter.onComplete();
                Log.d(TAG, "Observable thread = " + Thread.currentThread().getName());
            }
        }).subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "current thread = " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


//         delay()
//         delay(long delay,TimeUnit unit) // 延迟时间 时间单位
//         delay(long delay,TimeUnit unit,Scheduler mScheduler) 延迟时间 时间单位 调度器
//         delay(long delay,TimeUnit unit,boolean delayError) delayError 有error事件是否执行完再抛出异常
        Observable.just(1,2,3,4)
                .delay(3, TimeUnit.SECONDS)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "收到onSubscribe事件...");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "收到onNext事件 = "+integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "收到onError事件...");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "收到onComplete事件...");
                    }
                });

//         do操作符
//         doOnEach Observable 每发送事件就执行一次包括 onNext() onError() onCompleted()
//         doOnNextBefore doOnNextAfter onNext执行前后
//         doOnError 发送onError时调用 doOnCompleted 正常发送事件调用
//         doOnTerminate 正常发送完毕/异常终止时调用
//         doFinally 最后一定会执行
//         doOnSubscribe(）订阅时调用
//         doOnUnsubscribe() 解绑时调用

        // 错误处理
        // onErrorReturn 发送错误时，发送一个事件正常终止
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onError(new Throwable("发生了error"));
            }
        })
                .onErrorReturn(new Function<Throwable, Integer>() {
                    @Override
                    public Integer apply(Throwable throwable) throws Exception {
                        Log.e(TAG, "onErrorReturn处理了异常：" + throwable.toString());
                        return 888;
                    }
                }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "收到了onSubscribe事件");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "收到了onNext事件 = " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "收到了onError事件");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "收到了onComplete事件");
            }
        });

        // onErrorResumeNext onExceptionResumeNext发送错误时，发送一个新的Observable
        // 区别一个捕获error  一个exception
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onError(new Throwable("error111"));
            }
        }).onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> apply(Throwable throwable) throws Exception {
                Log.e(TAG, "onErrorResumeNext 捕获了：" + throwable.toString());
                return Observable.just(1,3,4);
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "收到了onNext事件 = " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "收到了onError事件");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "收到了onComplete事件");
            }
        });

        // 重试 retry 收到错误时，让被观察者重新发送事件
        // 5种重载方法
        // retry() 出现错误时，重新发送数据，一直错误一直发送
        // retry(long times) times 重试次数
        // retry(Predicate predicate) 出现错误是否需要重新发送数据
        // retry(long times,Predicate predicate) 重试次数&判断逻辑

        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onError(new Exception("出错了"));
                emitter.onNext(3);
            }
        }).retry(3, new Predicate<Throwable>() {
            @Override
            public boolean test(Throwable throwable) throws Exception {
                // 捕获异常
                Log.e(TAG, "retry错误: " + throwable.toString());
                return true;
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "收到onNext事件: " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "收到onError事件: " + e.toString());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "收到onComplete事件");
            }
        });

        // retryUntil() 类似retry(Predicate predicate) 返回true 不重新发送数据


        // retryWhen() 出现错误时，将发生的错误发送一个新的被观察者，并决定是否需要重新订阅原始的被观察者并发送事件
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onError(new Exception("出错了！！！"));
                emitter.onNext(3);
            }
        }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
            // 出现error事件回调
            @Override
            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {


                // 若新的被观察者发送error事件，原始的被观察者不会重新发送事件
                // 若新的被观察者发送next事件，原始的被观察者会重新发送事件
                return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
                        return Observable.error(new Throwable("retryWhen end"));
//                        return Observable.just(1);
                    }
                });
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "收到onNext事件: " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "收到onError事件: " + e.toString());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "收到onComplete事件");
            }
        });


        // repeat() repeatWhen() 重复发送 区别后者有条件重复发送
        Observable.just(1,2,3)
                .repeat(3)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "收到onNext事件: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "收到onError事件: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "收到onComplete事件");
                    }
                });


        Observable.just(1, 3, 5,9,10,12)
                .repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
                        return objectObservable.flatMap(new Function<Object, ObservableSource<?>>() {

                            @Override
                            public ObservableSource<?> apply(Object o) throws Exception {
//                                return Observable.empty(); // 发送complete事件 但观察者onComplete不会回调
//                                return Observable.error(new Throwable("出错了！！！"));
//                                return Observable.just(1);

                                // 加入判断条件：当轮询次数 = 5次后，就停止轮询
                                if (i > 3) {
                                    // 此处选择发送onError事件以结束轮询，因为可触发下游观察者的onError（）方法回调
                                    return Observable.error(new Throwable("轮询结束"));
                                }

                                // 注：此处加入了delay操作符，作用 = 延迟一段时间发送（此处设置 = 2s），以实现轮询间间隔设置
                                return Observable.just(1).delay(2000, TimeUnit.MILLISECONDS);
                            }
                        });
                    }
                }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "收到onNext事件: " + integer);
                i++;
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "收到onError事件: " + e.toString());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "收到onComplete事件");
            }
        });


    }
}