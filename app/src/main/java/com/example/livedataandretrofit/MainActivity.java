package com.example.livedataandretrofit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.res.Configuration;
import android.os.Bundle;

import com.example.livedataandretrofit.adapters.BlogAdapter;
import com.example.livedataandretrofit.models.Blog;
import com.example.livedataandretrofit.viewmodels.BlogsViewModel;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentMethodCreateParams;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    SwipeRefreshLayout swipeRefresh;
    private BlogsViewModel mainViewModel;
    Stripe stripe;
    BlogAdapter mBlogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializationViews();
        mainViewModel = ViewModelProviders.of(this).get(BlogsViewModel.class);
        getPopularBlog();
        // lambda expression
        swipeRefresh.setOnRefreshListener(() -> {
            getPopularBlog();
        });

        PaymentMethodCreateParams.Card card = PaymentMethodCreateParams.Card.create("");
        PaymentMethodCreateParams params = PaymentMethodCreateParams.create(card);
        if (params != null) {
            ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                    .createWithPaymentMethodCreateParams(params, "");
            stripe = new Stripe(getApplicationContext(), PaymentConfiguration.getInstance(getApplicationContext()).getPublishableKey());
            stripe.confirmPayment(this, confirmParams);
        }
    }

    private void initializationViews() {
        swipeRefresh = findViewById(R.id.swiperefresh);
        mRecyclerView = findViewById(R.id.blogRecyclerView);
    }

    public void getPopularBlog() {
        swipeRefresh.setRefreshing(true);
        mainViewModel.getAllBlogs().observe(this, new Observer<List<Blog>>() {
            @Override
            public void onChanged(@Nullable List<Blog> blogList) {
                swipeRefresh.setRefreshing(false);
                prepareRecyclerView(blogList);
            }
        });
        /**
         * Replace this statement with lambda expression
         * For using set you have to set following lines in app/build.gradle
         // add below line
         compileOptions {
         sourceCompatibility JavaVersion.VERSION_1_8
         targetCompatibility JavaVersion.VERSION_1_8
         }
         // reduce line of code
         mainViewModel.getAllBlog().observe(this, blogList -> prepareRecyclerView(blogList));

         */

    }

    private void prepareRecyclerView(List<Blog> blogList) {

        mBlogAdapter = new BlogAdapter(blogList);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        }
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mBlogAdapter);
        mBlogAdapter.notifyDataSetChanged();

    }
}
