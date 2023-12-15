package com.mfr.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.mfr.adapter.BudgetAdapter;
import com.mfr.model.Response.BaseResponse;
import com.mfr.model.Response.Parsed.BudgetGraph;
import com.mfr.model.Utils.Utils;
import com.mfr.ourwallet.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class BudgetFragment extends Fragment {

	private RecyclerView recycle;
	private ArrayList<BudgetGraph> budgets;
	private BudgetAdapter adapter;
	private ProgressBar progressBar;
	private NestedScrollView nested;
	private TextView txt;
	private Context mContext;

	public BudgetFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.budget_transactions, container, false);
		recycle = view.findViewById(R.id.recycleGoal);
		budgets = new ArrayList<>();
		adapter = new BudgetAdapter(mContext);
		RecyclerView.LayoutManager layoutManager =
				new LinearLayoutManager(mContext);
		recycle.setLayoutManager(layoutManager);
		recycle.setAdapter(adapter);

		progressBar = view.findViewById(R.id.transactionBudget);
		nested = view.findViewById(R.id.nested);

		txt = view.findViewById(R.id.txtBud);
		progressBar.setVisibility(View.VISIBLE);
		nested.setVisibility(View.GONE);
		loadBudgets();
		return view;
	}

	private void loadBudgets() {

		AndroidNetworking.get(Utils.IP+"/api/stats/familyBudgetSuggestions")
				.addHeaders("Authorization", "Bearer " + Utils.getToken(mContext).trim())
				.build()
				.getAsString(new StringRequestListener() {
					@Override
					public void onResponse(String response) {
						budgets = new ArrayList<>();
						response = response.replace(" ", "");
						Gson gson = new Gson();
						Log.d("Budgets", response);

						BaseResponse res = gson.fromJson(response, BaseResponse.class);
						Log.d("Budgets", res.toString());

						BudgetGraph[] budg = gson.fromJson(gson.toJson(res.getBody()), BudgetGraph[].class);
						budgets.addAll(Arrays.asList(budg));
						populateUi();
					}

					@Override
					public void onError(ANError anError) {
						Log.e("Budget",anError.getErrorBody());
					}
				});
		populateUi();
	}

	private void populateUi(){
		if(isAdded() && isVisible() && getUserVisibleHint()) {
			progressBar.setVisibility(View.INVISIBLE);
			nested.setVisibility(View.VISIBLE);
			adapter.setBudgets(budgets);
			adapter.notifyDataSetChanged();
			if(budgets.size()>0){
				txt.setVisibility(View.INVISIBLE);
			}else {
				txt.setVisibility(View.VISIBLE);
			}

		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = context;
	}
}
