package com.mfr.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.mfr.fragments.TransactionsFragment;
import com.mfr.model.Response.Parsed.Budget;
import com.mfr.model.Response.Parsed.BudgetGraph;
import com.mfr.model.Response.Parsed.Transaction;
import com.mfr.model.Utils.Utils;
import com.mfr.ourwallet.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {
	private List<BudgetGraph> budgets;
	private Context mContext;


	public BudgetAdapter(Context c) {
		this.budgets = new ArrayList<>();
		mContext = c;
	}

	@Override
	public BudgetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.budget_list_item, parent, false);
		BudgetViewHolder viewHolder = new BudgetViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(BudgetViewHolder holder, int position) {
		holder.bindTransaction(budgets.get(position));
	}

	@Override
	public int getItemCount() {
		return budgets.size();
	}



	public void setBudgets( List<BudgetGraph> bud){
		this.budgets.clear();
		this.budgets.addAll(bud);
		notifyDataSetChanged();
	}




	public class BudgetViewHolder extends RecyclerView.ViewHolder{

		private TextView name;
		private TextView advice;
		private NumberProgressBar progressBar;
		private TextView value;

		public BudgetViewHolder(View itemView) {
			super(itemView);
			name = itemView.findViewById(R.id.nameBudget);
			advice = itemView.findViewById(R.id.adviceBudget);
			progressBar = itemView.findViewById(R.id.progressBudget);
			value = itemView.findViewById(R.id.budgetValue);
		}

		public void bindTransaction(BudgetGraph tr) {
			name.setText(tr.getBudget().getName());
			advice.setText("Oggi puoi spendere "+tr.getAmount()+" €");
			progressBar.setMax(100);
			float ris = (float) tr.getSpent()/ (float)tr.getBudget().getAmount();
			ris = ris *100;
			progressBar.setProgress((int)(ris));
			value.setText(tr.getBudget().getAmount()+" €");

			if(ris>=40){
				progressBar.setReachedBarColor(Color.parseColor("#FFA500"));
				progressBar.setProgressTextColor(Color.parseColor("#FFA500"));

			}
			if (ris>=80){
				progressBar.setUnreachedBarColor(Color.parseColor("#DC143C"));
				progressBar.setReachedBarColor(Color.parseColor("#DC143C"));
				progressBar.setProgressTextColor(Color.parseColor("#DC143C"));

			}
		}

	}
}