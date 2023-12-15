package com.mfr.ourwallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.mfr.fragments.BudgetFragment;
import com.mfr.fragments.TransactionsFragment;
import com.mfr.model.Response.BaseResponse;
import com.mfr.model.Response.Parsed.BodyJWT;
import com.mfr.model.Response.Parsed.BudgetGraph;
import com.mfr.model.Utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AppActivity extends AppCompatActivity {


	private ArrayList<Fragment> fragments;
	private Button goals;
	private Button transactions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_app);
		fragments = new ArrayList<>();
		fragments.add(new TransactionsFragment());
		fragments.add(new BudgetFragment());
		transactions = findViewById(R.id.buttonTransaction);
		goals =  findViewById(R.id.buttonGoals);
		transactions.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeViev(0);
			}
		});
		goals.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				changeViev(1);
			}
		});
		changeViev(0);

		ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);		scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
			public void run() {
				refreshToken();
			}
		}, 0, 5, TimeUnit.MINUTES);

	}

	public void changeViev(int pos){
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment, fragments.get(pos)).commit();
		color(pos);
	}

	private void color(int pos) {
		goals.setTextColor(getResources().getColor(R.color.colorDisabled));
		transactions.setTextColor(getResources().getColor(R.color.colorDisabled));

		switch (pos) {
			case 0:
				transactions.setTextColor(getResources().getColor(R.color.colorEnabled));
				break;
			case 1:
				goals.setTextColor(getResources().getColor(R.color.colorEnabled));
				break;
		}

		colorButton(pos);
	}

	private void colorButton(int index){
		switch(index){
			case 0:
				transactions.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorEnabled)));
				goals.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorDisabled)));
				break;
			case 1:
				goals.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorEnabled)));
				transactions.setCompoundDrawableTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.colorDisabled)));
				break;

		}
	}

	private void refreshToken(){
		AndroidNetworking.get(Utils.IP+"/api/refresh")
				.addHeaders("Authorization", "Bearer " + Utils.getToken(this).trim())
				.build()
				.getAsString(new StringRequestListener() {
					@Override
					public void onResponse(String response) {
						Gson gson = new Gson();
						BaseResponse res = gson.fromJson(response, BaseResponse.class);
						BodyJWT jwt = gson.fromJson(res.getBody().toString(), BodyJWT.class);
						Log.d("Refresh", jwt.getJwt());
						Utils.setToken(jwt.getJwt(), getApplicationContext());

					}

					@Override
					public void onError(ANError anError) {
						Log.e("Budget",anError.getErrorBody());
					}
				});
	}

}
