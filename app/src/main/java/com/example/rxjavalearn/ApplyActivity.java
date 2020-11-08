package com.example.rxjavalearn;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;

/**
 * RxJava应用场景
 */
public class ApplyActivity extends AppCompatActivity {
    private static final String TAG = "RxJava";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        final EditText name = findViewById(R.id.et1);
        final EditText age = findViewById(R.id.et2);
        final EditText id = findViewById(R.id.et3);

        Button btn = findViewById(R.id.btn);
        // 跳过第一次输入
        Observable<CharSequence> nameObservable = RxTextView.textChanges(name).skip(1);
        Observable<CharSequence> ageObservable = RxTextView.textChanges(age).skip(1);
        Observable<CharSequence> idObservable = RxTextView.textChanges(id).skip(1);

        Observable.combineLatest(nameObservable, ageObservable, idObservable, new Function3<CharSequence, CharSequence, CharSequence, Boolean>() {
            @NonNull
            @Override
            public Boolean apply(@NonNull CharSequence charSequence, @NonNull CharSequence charSequence2, @NonNull CharSequence charSequence3) throws Exception {
                // 1. 姓名信息
                boolean isUserNameValid = !TextUtils.isEmpty(name.getText());
                // 2. 年龄信息
                boolean isUserAgeValid = !TextUtils.isEmpty(age.getText());
                // 3. 身份证信息
                boolean isUserIdValid = !TextUtils.isEmpty(id.getText());

                return isUserNameValid && isUserAgeValid && isUserIdValid;
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                // 最后判断是否可以提交
            }
        });


        RxView.clicks(btn)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                       // 处理点击事件
                    }
                });


        RxTextView
                .textChanges(name)
                // skip(1)跳过第一次数据为空事件，debounce 1秒中只执行一次
                .debounce(1,TimeUnit.SECONDS).skip(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CharSequence>() {
                    @Override
                    public void accept(CharSequence charSequence) throws Exception {
                        // 服务器请求的数据
                    }
                });

        // button长按事件
        RxView.longClicks(btn)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        // 响应长按事件
                    }
                });



    }

}