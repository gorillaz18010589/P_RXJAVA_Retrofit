package com.example.p_rxjava;
//目的RxJava結合Retrofit,抓取資料呈現在item上,但觀念薄弱另外會做一篇
//1.加入api
/* //Retrofit
    implementation 'com.squareup.retrofit2:converter-gson:2.6.2'
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.5.0'
    //RxJava
    implementation 'io.reactivex.rxjava2:rxjava:2.1.9'
    implementation 'io.reactivex.rxjava2:rxandroid:2.0.1'*/

//2.API網址https://jsonplaceholder.typicode.com => posts	100 posts
//3.建立post的Model
//4.建立Retrofit的介面,getPosts回傳的資料用io.reactivex的Observable<T> 包起來
//5.建立一個Retrofit物件,用RxJava2CallAdapterFactory的工廠
//6.建立一個CardView當資料呈現的ui
//7.RecycleView設定
//8.activity取得myApi

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MyApi myApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //8.activity取得myApi
        Retrofit retrofit = RetrofitClient.getInstance();
         myApi = retrofit.create(MyApi.class);

        //9.init RecycleView
        recView = findViewById(R.id.recView);
        recView.setLayoutManager(new LinearLayoutManager(this));

        //10.抓取資料呼叫
        fecthData();
        

    }
//    CompositeDisposable.boolean add(@NonNull Disposable d)
//    Observable.Observable<T> subscribeOn(Scheduler scheduler)
//    Observable.Disposable subscribe(Consumer<? super T> onExceptionResumeNext
//    Observable.Disposable subscribe(Consumer<? super T> onNext)
//   io.reactivex.functions.interface Consumer<T>
    //10.抓取資料用CompositeDisposable
    private void fecthData() {
        compositeDisposable
                .add((Disposable) myApi.getPosts()
        .subscribeOn(Schedulers.io()) //決定送出資料的thread
        .observeOn(AndroidSchedulers.mainThread()) //決定operator執行時的thread
                .subscribe(new Consumer<List<Post>>() { //訂閱Observable
                    @Override
                    public void accept(List<Post> posts) throws Exception {
                        Log.v("hank","observeOn =>  +posts:" + posts);
                        setData(posts);
                    }
                })
        );

    }

    private void setData(List<Post> posts) {
        recView.setAdapter(new PostRecyclerViewAdapter(posts,this));
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }
}
