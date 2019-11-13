package com.example.livedataandretrofit.repos;


import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.livedataandretrofit.models.Blog;
import com.example.livedataandretrofit.models.BlogWrapper;
import com.example.livedataandretrofit.network.RestApiService;
import com.example.livedataandretrofit.network.RetrofitInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.livedataandretrofit.BuildConfig.BASE_URL;

public class BlogRepository {
    private ArrayList<Blog> movies = new ArrayList<>();
    private MutableLiveData<List<Blog>> mutableLiveData = new MutableLiveData<>();
    private Application application;

    public BlogRepository(Application application) {
        this.application = application;
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
                }
            }

            @Override
            public void onFailure(Call<BlogWrapper> call, Throwable t) {

            }
        });


        return mutableLiveData;
    }
}
