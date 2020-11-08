package com.example.rxjavalearn;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.rxjavalearn.rxbus.RxBus;
import com.example.rxjavalearn.rxbus.RxTestBean;

public class RxBusActivity extends AppCompatActivity {
    private RxBus rxBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_bus);

        TextView textView = findViewById(R.id.tv_rxbus);
        rxBus = RxBus.getIntance();
        registerRxBus(RxTestBean.class, bean -> {
            if (bean != null) {
                textView.setText(bean.getName() + "-" + bean.getAge());
            }
        });

        textView.setOnClickListener(v -> RxBus.getIntance().post(new RxTestBean("张三", 28)));



    }

    // 注册
    public <T> void registerRxBus(Class<T> eventType, Consumer<T> action) {
        Disposable disposable = rxBus.doSubscribe(eventType, action, throwable -> Log.e("rxbus", throwable.toString()));
        rxBus.addSubscription(this, disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解绑
        RxBus.getIntance().unSubscribe(this);
    }
}