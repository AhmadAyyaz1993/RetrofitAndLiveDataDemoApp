package com.example.livedataandretrofit;

import android.app.Application;

import androidx.multidex.MultiDexApplication;

import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApp extends MultiDexApplication {
    Stripe stripe;
    @Override
    public void onCreate() {
        super.onCreate();
        PaymentConfiguration.init(getApplicationContext(), "pk_test_TYooMQauvdEDq54NiTphI7jx");
        Realm.init(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("MyTest.realm")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
