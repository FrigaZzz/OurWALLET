package com.mfr.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.mfr.fragments.TransactionsFragment;
import com.mfr.model.Response.Parsed.Transaction;
import com.mfr.model.Utils.Utils;
import com.mfr.ourwallet.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
	private List<Transaction> transactions;
	private Context mContext;
	private TransactionsFragment frag;
	private HashMap<String, Integer> colors;


	public TransactionAdapter(Context context, List<Transaction> transactions, TransactionsFragment transactionsFragment, HashMap<String, Integer> colors) {
		mContext = context;
		this.transactions = new ArrayList<>();
		frag = transactionsFragment;
		this.colors = colors;
	}

	@Override
	public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transactions_list_item, parent, false);
		TransactionViewHolder viewHolder = new TransactionViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(TransactionViewHolder holder, int position) {
		holder.bindTransaction(transactions.get(position));
	}

	@Override
	public int getItemCount() {
		return transactions.size();
	}

	public synchronized void setTransaction(List<Transaction> tr){
		transactions = new ArrayList<>();
		for(Transaction t:tr){
			if(!transactions.contains(t)){
				transactions.add(t);
			}
		}

		notifyDataSetChanged();
	}

	public void deleteTransaction(Transaction tr){
		AndroidNetworking.delete(Utils.IP+"/api/transactions/"+tr.getId())
				.addHeaders("Authorization", "Bearer " + Utils.getToken(mContext).trim())
				.build().getAsString(new StringRequestListener() {
			@Override
			public void onResponse(String response) {
				Log.e("Delete",response);
			}

			@Override
			public void onError(ANError anError) {
				Log.e("Delete",anError.getErrorBody());

			}
		});

		frag.updateGraph(transactions.indexOf(tr));
		transactions.remove(tr);
		notifyDataSetChanged();
	}



	public void printToast(String text) {
		text = text.trim();
		Toast t = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
		t.getYOffset();
		t.show();
	}



	public class TransactionViewHolder extends RecyclerView.ViewHolder{



		private Context mContext;
		private TextView value;
		private TextView date;
		private TextView description;
		private Button category;
		private ImageButton delete;
		private Transaction trans;

		public TransactionViewHolder(View itemView) {
			super(itemView);
			mContext = itemView.getContext();
			value = itemView.findViewById(R.id.textValue);
			date = itemView.findViewById(R.id.textDate);
			description = itemView.findViewById(R.id.textDescription);
			category = itemView.findViewById(R.id.btnCat);
			delete =itemView.findViewById(R.id.btnDelete);
		}

		public void bindTransaction(Transaction tr) {
			this.trans = tr;
			value.setText(tr.getAmount()+"");
			date.setText(Utils.getDate(tr.getDate()));
			if(tr.getDescription()!=null) {
				description.setText(tr.getDescription());
			}
			if(tr.getCategory()!=null) {
				Integer color = colors.get(tr.getCategory().getName());
				category.setBackgroundColor(color);
				category.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(mContext,"Categoria:"+trans.getCategory().getName(), Toast.LENGTH_SHORT).show();
					}
				});
			}else{
				Integer color = colors.get("null");
				category.setBackgroundColor(color);
				category.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(mContext,"Nessuna categoria", Toast.LENGTH_SHORT).show();
					}
				});
			}


			delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					new AlertDialog.Builder(mContext)
							.setTitle("Elimina")
							.setMessage("Elimina la transazione?")
							.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									deleteTransaction(trans);

								}
							})
							.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {

								}
							})
							.show();
				}
			});
		}

	}
}