package com.example.p_rxjava;
//建立Retrofit的介面,getPosts用io.reactivex的Observable<T> 包起來
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface MyApi {
    @GET("posts")
    Observable<List<Post>> getPosts();
}
