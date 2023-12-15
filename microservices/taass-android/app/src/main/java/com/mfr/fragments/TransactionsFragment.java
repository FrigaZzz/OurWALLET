package com.mfr.fragments;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mfr.adapter.TransactionAdapter;
import com.mfr.model.Request.InputTransaction;
import com.mfr.model.Response.BaseResponse;
import com.mfr.model.Response.Parsed.Account;
import com.mfr.model.Response.Parsed.Categories;
import com.mfr.model.Response.Parsed.Category;
import com.mfr.model.Response.Parsed.Group;
import com.mfr.model.Response.Parsed.Transaction;
import com.mfr.model.Utils.Utils;
import com.mfr.ourwallet.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.mfr.model.Utils.Utils.getDateFromDatePicker;

public class TransactionsFragment extends Fragment {


	private ConstraintLayout layout;
	private LineChart chart;
	private LineData lineData;

	private RecyclerView recycle;
	private TransactionAdapter adapter;
	private ProgressBar progressBar;

	private FloatingActionButton fab;

	private List<Transaction> transactions;
	private Group gFamily;
	private HashMap<String, Integer> colors;
	private List<Category> categories;
	private Integer taskFinished;

	private Context mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.transactions_fragment, container, false);
		layout = view.findViewById(R.id.viewTransaction);

		transactions = Collections.synchronizedList(new ArrayList<Transaction>());
		categories = new ArrayList<>();
		colors = new HashMap<>();

		adapter = new TransactionAdapter(mContext, transactions, this, colors);
		recycle = view.findViewById(R.id.recycle);
		RecyclerView.LayoutManager layoutManager =
				new LinearLayoutManager(mContext);
		recycle.setLayoutManager(layoutManager);
		recycle.setAdapter(adapter);

		chart = view.findViewById(R.id.chart1);
		chart.setTouchEnabled(true);
		chart.setPinchZoom(true);

		fab = view.findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showInputDialog();
			}
		});

		progressBar = view.findViewById(R.id.transactionProgress);
		progressBar.setVisibility(View.VISIBLE);
		layout.setVisibility(View.GONE);

		getAllGroups();
		getAllCategories();
		return view;
	}

	private void showInputDialog() {
		MaterialDialog dialog  = new MaterialDialog.Builder(mContext)
				.title("Nuova Transazione")
				.customView(R.layout.transactions_options, true)
				.positiveText("Conferma")
				.negativeText("Annulla")
				.onPositive(new MaterialDialog.SingleButtonCallback() {
					@Override
					public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
						View view = dialog.getView();
						Spinner spinner = (Spinner)view.findViewById(R.id.spinner);
						Spinner spinnerCat = (Spinner)view.findViewById(R.id.spinnerCat);
						EditText descriptionValue = view.findViewById(R.id.value);
						EditText descriptionEdit = view.findViewById(R.id.description);
						DatePicker picker = view.findViewById(R.id.datePicker);

						String value = descriptionValue.getText().toString();
						String desc = descriptionEdit.getText().toString();
						Long date = getDateFromDatePicker(picker);
						if(!value.equals("") && isNumeric(value)){
							Long val = Long.valueOf(value.trim());
							sendNewTransaction(val, spinner.getSelectedItemPosition(), spinnerCat.getSelectedItemPosition(),desc, date);
						}


					}
				})
				.build();

		View view =  dialog.getCustomView();

		Spinner spinner = (Spinner)view.findViewById(R.id.spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


		for(Account acc: gFamily.getAccounts()){
			if(acc.getName()!=null){
				adapter.add(acc.getName());
			}else{
				adapter.add(acc.getId()+"");
			}
		}
		spinner.setAdapter(adapter);


		Spinner spinnerCat = (Spinner)view.findViewById(R.id.spinnerCat);
		ArrayAdapter<String> adapterCat = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_spinner_item);
		adapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		for(Category c: categories){
			if(c.getName()!=null){
				adapterCat.add(c.getName());
			}else{
				adapterCat.add(c.getId()+"");
			}
		}

		spinnerCat.setAdapter(adapterCat);
		dialog.show();

	}

	/*Checks*/
	public static boolean isNumeric(String strNum) {
		if (strNum == null) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public int getRandomColor(){
		Random rnd = new Random();
		return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
	}


	/*UI*/
	public void populateTransactions(List<Transaction> trNet){
		taskFinished();
		transactions.addAll(trNet);

		if(isDone() && (isAdded() && isVisible() && getUserVisibleHint())) {
			resetTask();
			progressBar.setVisibility(View.INVISIBLE);
			layout.setVisibility(View.VISIBLE);
			Collections.sort(transactions, (t1, t2) -> {
				return (int)(t1.getDate()-t2.getDate());
			});

			List<Entry> lineEntries = new ArrayList<>();
			Long sum = 0l;
			int i = 0;

			colors.put("null", getRandomColor());
			i = 0;
			for (Transaction t : transactions) {
				sum = sum + t.getAmount();
				lineEntries.add(new Entry(i, sum));
				if (t.getCategory() != null && !colors.containsKey(t.getCategory().getName())) {
					colors.put(t.getCategory().getName(), getRandomColor());
				}
				i++;
			}

			chart.setNoDataText("Nessuna transazione questo mese.");
			chart.setNoDataTextColor(ContextCompat.getColor(mContext, R.color.colorEnabled));
			chart.getDescription().setEnabled(false);
			chart.getXAxis().setEnabled(false);
			chart.getAxisLeft().setDrawGridLines(false);
			chart.getAxisRight().setDrawGridLines(false);


			if(i>0) {
				LineDataSet dataSet = new LineDataSet(lineEntries, "Andamento Transazioni"); // add entries to dataset

				// fill drawable only supported on api level 18 and above
				Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.fade_blue);
				dataSet.setDrawFilled(true);
				dataSet.setFillDrawable(drawable);

				lineData = new LineData(dataSet);
				chart.setData(lineData);
				chart.invalidate();

			}
			adapter.setTransaction(transactions);
			adapter.notifyDataSetChanged();


		}
	}

	public void updateGraph(final int pos){

		final LineData data = chart.getData();

		if (data != null) {

			final ILineDataSet set = data.getDataSetByIndex(0);

			if (set != null) {


				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						data.removeEntry(pos, 0);
						data.notifyDataChanged();
						chart.notifyDataSetChanged();
						chart.invalidate();
					}
				});

			}
		}
	}

	/*Sync*/
	public synchronized void taskFinished(){
		taskFinished = taskFinished+1;
	}

	public synchronized void resetTask(){
		taskFinished = 0;
	}

	public synchronized boolean isDone(){
		return taskFinished==gFamily.getAccounts().size();
	}

	/*Network*/
	public void getAllGroups(){
		transactions.clear();
		progressBar.setVisibility(View.VISIBLE);
		layout.setVisibility(View.GONE);
		ArrayList<Account> accounts = new ArrayList<>();
		AndroidNetworking.get(Utils.IP+"/api/groups")
				.addHeaders("Authorization", "Bearer " + Utils.getToken(mContext).trim())
				.build()
				.getAsString(new StringRequestListener() {
					@Override
					public void onResponse(String response) {
						response = response.replace(" ","");
						Gson gson = new Gson();
						Log.d("Accounts",response);

						BaseResponse res = gson.fromJson(response, BaseResponse.class);
						Log.d("Accounts",res.toString());

						Log.d("Accounts",gson.toJson(res.getBody()));
						Group[] groups= gson.fromJson(gson.toJson(res.getBody()),Group[].class);
						ArrayList<Account> accounts = new ArrayList<>();
						for (Group g:
								groups) {
							accounts.addAll(g.getAccounts());
							if(g.getFamilyGroup()) {
								gFamily = g;
								getAllFamilyTransaction();
							}
						}




					}

					@Override
					public void onError(ANError anError) {
						Log.e("Accounts",anError.getErrorBody());
					}
				});

	}

	private void getAllFamilyTransaction() {
		resetTask();
		transactions.clear();
		Date fromDate = Utils.getFromDate();
		Date toDate = Utils.getToDate();

		String filterDate="( date>"+fromDate.getTime()+" AND date<"+toDate.getTime()+" )";

		for (Account a : gFamily.getAccounts()) {

			AndroidNetworking.get(Utils.IP+"/api/accounts/" + a.getId() + "/android_transactions")
					.addHeaders("Authorization", "Bearer " + Utils.getToken(mContext).trim())
					.addQueryParameter("filters",filterDate)
					.build()
					.getAsString(new StringRequestListener() {
						@Override
						public void onResponse(String response) {

							response = response.replace(" ", "");
							Gson gson = new Gson();
							Log.d("Transactions", response);

							BaseResponse res = gson.fromJson(response, BaseResponse.class);
							Log.d("Transactions", res.toString());
							Type t = new TypeToken<HashMap<String, Object>>() {
							}.getType();

							HashMap<String, String> hash = ((HashMap<String, String>) (gson.fromJson(gson.toJson(res.getBody()), t)));
							Transaction[] transactions = gson.fromJson(gson.toJson(hash.get("transactions")), Transaction[].class);
							populateTransactions(Arrays.asList(transactions));
						}

						@Override
						public void onError(ANError anError) {
							Log.e("Accounts", anError.getErrorBody());
						}
					});
		}
	}

	private void sendNewTransaction(Long val, int selectedItemPosition,int selectedCatPosition, String descr, Long date) {
		InputTransaction newTransaction = new InputTransaction();
		newTransaction.setAccountSenderID(gFamily.getAccounts().get(selectedItemPosition).getId());
		newTransaction.setGroupSenderID(gFamily.getId());
		newTransaction.setAmount(val);
		newTransaction.setDescription(descr);
		newTransaction.setDate(date);
		newTransaction.setCategoryID(categories.get(selectedCatPosition).getId());

		Log.d("NewTransaction", newTransaction.getAmount()+"");

		AndroidNetworking.post(Utils.IP+"/api/transactions")
				.addApplicationJsonBody(newTransaction)
				.addHeaders("Authorization", "Bearer " + Utils.getToken(mContext).trim())
				.build()
				.getAsString(new StringRequestListener() {
					@Override
					public void onResponse(String response) {
						getAllFamilyTransaction();
					}

					@Override
					public void onError(ANError anError) {
						Toast.makeText(mContext,"Stai sforando il budget!",Toast.LENGTH_LONG).show();
					}
				});
	}

	private void getAllCategories() {
		AndroidNetworking.get(Utils.IP+"/api/categories")
				.addHeaders("Authorization", "Bearer " + Utils.getToken(mContext).trim())
				.build()
				.getAsString(new StringRequestListener() {
					@Override
					public void onResponse(String response) {
						response = response.replace(" ", "");
						Gson gson = new Gson();
						Log.e("Category", response);

						BaseResponse res = gson.fromJson(response, BaseResponse.class);
						Log.e("Category", res.toString());
						Log.e("a",gson.toJson(res.getBody()));
						Categories cats = gson.fromJson(gson.toJson(res.getBody()), Categories.class);
						categories.addAll(cats.getCategories());
					}

					@Override
					public void onError(ANError anError) {
						Log.e("Category", anError.getErrorBody());
					}
				});
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = context;
	}


}

