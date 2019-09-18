package com.versatile.fastclas.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.versatile.fastclas.BaseActivity;
import com.versatilemobitech.fastclas.R;

public class OfferPriceAcivity extends BaseActivity {

    RecyclerView recyclerViewOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_price_acivity);
    }
}
