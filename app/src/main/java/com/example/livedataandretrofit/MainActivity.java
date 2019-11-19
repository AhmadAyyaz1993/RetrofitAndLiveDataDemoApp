package com.example.livedataandretrofit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.livedataandretrofit.adapters.BlogAdapter;
import com.example.livedataandretrofit.models.Blog;
import com.example.livedataandretrofit.viewmodels.BlogsViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
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

        PaymentMethodCreateParams.Card card = PaymentMethodCreateParams.Card.create("tok_mastercard_debit_transferSuccess");
        PaymentMethodCreateParams params = PaymentMethodCreateParams.create(card);
        if (params != null) {
            ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                    .createWithPaymentMethodCreateParams(params, "pi_1Db4Eu2eZvKYlo2Cet17VHlp_secret_IvrnS53GJker7rUaDH2sM8CRw");
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback());
    }

    private static final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {


        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {


            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
        }
    }
}
