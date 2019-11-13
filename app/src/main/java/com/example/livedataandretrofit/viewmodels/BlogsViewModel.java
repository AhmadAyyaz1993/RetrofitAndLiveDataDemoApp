package com.example.livedataandretrofit.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.livedataandretrofit.models.Blog;
import com.example.livedataandretrofit.repos.BlogRepository;

import java.util.List;

public class BlogsViewModel extends AndroidViewModel {

    private BlogRepository blogRepository;
    public BlogsViewModel(@NonNull Application application) {
        super(application);
        blogRepository = new BlogRepository(application);
    }

    public LiveData<List<Blog>> getAllBlogs() {
        return blogRepository.getBlogsFromServer();
    }
}
