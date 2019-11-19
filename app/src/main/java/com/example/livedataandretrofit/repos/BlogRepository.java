package com.example.livedataandretrofit.repos;


import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.livedataandretrofit.models.Blog;
import com.example.livedataandretrofit.models.BlogWrapper;
import com.example.livedataandretrofit.network.RestApiService;
import com.example.livedataandretrofit.network.RetrofitInstance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.livedataandretrofit.BuildConfig.BASE_URL;

public class BlogRepository {
    private ArrayList<Blog> movies = new ArrayList<>();
    private MutableLiveData<List<Blog>> mutableLiveData = new MutableLiveData<>();
    private Application application;
    private Realm mRealm;
    public BlogRepository(Application application) {
        this.application = application;
        this.mRealm = Realm.getDefaultInstance();
    }

    public MutableLiveData<List<Blog>> getBlogsFromServer() {

        RestApiService apiService = RetrofitInstance.getApiService(BASE_URL);

        Call<BlogWrapper> call = apiService.getPopularBlog();

        call.enqueue(new Callback<BlogWrapper>() {
            @Override
            public void onResponse(Call<BlogWrapper> call, Response<BlogWrapper> response) {
                BlogWrapper mBlogWrapper = response.body();
                if (mBlogWrapper != null && mBlogWrapper.getBlog() != null) {
                    movies = (ArrayList<Blog>) mBlogWrapper.getBlog();
                    mutableLiveData.setValue(movies);
                    for (Blog blog : movies){
                        mRealm.beginTransaction();
                        Blog blog1 = mRealm.createObject(Blog.class);
                        blog1.setAuthor(blog.getAuthor());
                        blog1.setDescription(blog.getDescription());
                        blog1.setLink(blog.getLink());
                        blog1.setmId(System.currentTimeMillis());
                        blog1.setPubDate(new Date().toString());
                        blog1.setTitle(blog.getTitle());
                        mRealm.commitTransaction();
                    }
                }
            }

            @Override
            public void onFailure(Call<BlogWrapper> call, Throwable t) {

            }
        });


        return mutableLiveData;
    }
}
