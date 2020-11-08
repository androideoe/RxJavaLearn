package com.example.rxjavalearn;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 变换操作符
 */
public class ChangeActivity extends AppCompatActivity {
    private static final String TAG = "RxJava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);


        // 变换操作符 Map FlatMap ConcatMap Buffer

        // Map 被观察者发送的每一个事件根据指定的函数处理，转换成另一种事件
//        Observable.create(new ObservableOnSubscribe<Integer>() {
//
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                emitter.onNext(1);
//                emitter.onNext(2);
//                emitter.onNext(3);
//
//            }
//        }).map(new Function<Integer, String>() {
//
//            @Override
//            public String apply(Integer integer) throws Exception {
//                return "map变换后的事件：" + integer;
//            }
//        }).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                Log.d(TAG, s);
//            }
//        });
//
        // FlatMap 将被观察者发送的事件，经过合并、拆分或者单独转换生成一个新的事件序列再发送
        // 新合成的事件和原事件发送顺序无关
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);

            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("修改前的事件：" + integer + "变换后的事件: " + i);
                }
                return Observable.fromIterable(list);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d(TAG, "error = "+throwable.toString());
            }
        });
//
//
//        // ConcatMap 与FlatMap类似 新合并生成的事件与原事件发送顺序有关
//        Observable.create(new ObservableOnSubscribe<Integer>() {
//
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//                emitter.onNext(1);
//                emitter.onNext(2);
//                emitter.onNext(3);
//            }
//        }).concatMap(new Function<Integer, ObservableSource<String>>() {
//            @Override
//            public ObservableSource<String> apply(Integer integer) throws Exception {
//                List<String> list = new ArrayList<>();
//                for (int i = 0; i < 3; i++) {
//                    list.add("修改前的事件：" + integer + "变换后的事件: " + i);
//                }
//                return Observable.fromIterable(list);
//            }
//        }).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                Log.d(TAG, s);
//            }
//        });


        // Buffer 定期从被观察者(Observable)获取一定事件放在缓存区，并最终发送
        // 1.发送1、2、3、4、5
        Observable.just(1, 2, 3, 4, 5)
                // 缓存数量3 步长1
                // 缓存区大小 = 每次从被观察者获取的数量大小
                // 步长 = 每次获取新事件的数量
                .buffer(3, 1)
                .subscribe(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(List<Integer> integers) {
                        Log.d(TAG, "缓存区的数量：= " + integers.size());
                        for (Integer i : integers) {
                            Log.d(TAG, "事件：= " + i);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "收到onError事件");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "收到onComplete事件...");
                    }
                });
    }
}