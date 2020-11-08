package com.example.rxjavalearn.rxbus;

/**
 * Created by ddup on 2020/11/8.
 */
public class RxTestBean {

    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public RxTestBean(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
