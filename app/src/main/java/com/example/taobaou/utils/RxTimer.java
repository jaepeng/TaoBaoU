package com.example.taobaou.utils;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 定时器功能
 */
public class RxTimer {

    private long time;
    private TimeUnit timeUnit;
    private OnTimeCome mOnTimeCome = null;
    private int tag;
    private Disposable mDisposable;
    private long timePeriod;
    private long count=0;

    public RxTimer(Builder builder) {
        this.time = builder.time;
        this.timeUnit = builder.timeUnit;
        this.tag= builder.tag;
        this.timePeriod=builder.timeperiod;
    }

    public static class Builder {
        long time = 0;
        long timeperiod=0;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        int tag=-1;

        public Builder setTime(long time) {
            this.time = time;
            return this;
        }

        public Builder setTimeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
            return this;
        }
        public Builder setTag(int tag){
            this.tag=tag;
            return this;
        }
        public Builder setTimePeriod(long timeperiod){
            this.timeperiod=timeperiod;
            return this;
        }

        public RxTimer build() {
            return new RxTimer(this);
        }

    }

    public void startTimer() {
        Observable mObservable = Observable.interval(this.time, this.timeUnit);
        mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable=d;
                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        count++;
                        Log.d("jae", "onNext: !"+count);

                        if (mOnTimeCome != null) {
                            mOnTimeCome.onTimeCome();
                            closeTimer();
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public void setOnTimeCome(OnTimeCome onTimeCome) {
        this.mOnTimeCome = onTimeCome;
    }

    public interface OnTimeCome {
        //时间到了
        void onTimeCome();
    }
    public void closeTimer(){
        if (mDisposable != null) {
            mDisposable.dispose();
        }

    }

}
