package com.admedia.bendre.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.admedia.bendre.R;
import com.admedia.bendre.WordPressService;
import com.admedia.bendre.model.woocommerce.Billing;
import com.admedia.bendre.model.woocommerce.Order;
import com.admedia.bendre.model.woocommerce.OrderItem;
import com.admedia.bendre.model.woocommerce.Product;
import com.admedia.bendre.model.woocommerce.Shipping;
import com.admedia.bendre.util.EndpointConstants;
import com.admedia.bendre.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.admedia.bendre.util.Constants.USE_CACHE_DATA;

public class PaymentActivity extends AppCompatActivity {
    private TextInputLayout mLayoutFirstName;
    private TextInputLayout mLayoutLastName;
    private TextInputLayout mLayoutCountry;
    private TextInputLayout mLayoutAddress;
    private TextInputLayout mLayoutCity;
    private TextInputLayout mLayoutState;
    private TextInputLayout mLayoutPostalCode;
    private TextInputLayout mLayoutPhone;
    private TextInputLayout mLayoutMail;

    private TextInputEditText mFirstName;
    private TextInputEditText mLastName;
    private TextInputEditText mCountry;
    private TextInputEditText mAddress;
    private TextInputEditText mCity;
    private TextInputEditText mState;
    private TextInputEditText mPostalCode;
    private TextInputEditText mPhone;
    private TextInputEditText mEmail;

    private ProgressBar mProgressBar;

    private Product selectedProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        selectedProduct = (Product) getIntent().getSerializableExtra("SELECTED_PRODUCT");

        TextView mProduct = findViewById(R.id.payment_product);
        String productString = mProduct.getText().toString();
        productString = productString + " " + selectedProduct.getName();
        mProduct.setText(productString);

        TextView mAmount = findViewById(R.id.payment_amount);
        String amountString = mAmount.getText().toString();
        amountString = amountString + " " + selectedProduct.getRegularPrice() + " FCFA";
        mAmount.setText(amountString);

        FloatingActionButton fab = findViewById(R.id.fab_close);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), KiosqueActivity.class);
            intent.putExtra(USE_CACHE_DATA, true);
            startActivity(intent);
        });

        mFirstName = findViewById(R.id.payment_firstname);
        mLastName = findViewById(R.id.payment_lastname);
        mCountry = findViewById(R.id.payment_country);
        mAddress = findViewById(R.id.payment_address);
        mCity = findViewById(R.id.payment_city);
        mState = findViewById(R.id.payment_province);
        mPostalCode = findViewById(R.id.payment_postal_code);
        mPhone = findViewById(R.id.payment_phone);
        mEmail = findViewById(R.id.payment_mail);

        mLayoutFirstName = findViewById(R.id.layout_firstname);
        mLayoutLastName = findViewById(R.id.layout_lastname);
        mLayoutCountry = findViewById(R.id.layout_country);
        mLayoutAddress = findViewById(R.id.layout_address);
        mLayoutCity = findViewById(R.id.layout_city);
        mLayoutState = findViewById(R.id.layout_state);
        mLayoutPostalCode = findViewById(R.id.layout_postal_code);
        mLayoutPhone = findViewById(R.id.layout_phone);
        mLayoutMail = findViewById(R.id.layout_mail);

        mProgressBar = findViewById(R.id.payment_progressbar);

        showProgress(false);
    }

    public void order(View view) {
        if (isValid())
        {
            showProgress(true);

            Order order = new Order();
            Billing billing = new Billing();
            billing.setFirstName(Objects.requireNonNull(mFirstName.getText()).toString());
            billing.setLastName(Objects.requireNonNull(mLastName.getText()).toString());
            billing.setCountry(Objects.requireNonNull(mCountry.getText()).toString());
            billing.setAddress1(Objects.requireNonNull(mAddress.getText()).toString());
            billing.setAddress2("");
            billing.setCity(Objects.requireNonNull(mCity.getText()).toString());
            billing.setState(Objects.requireNonNull(mState.getText()).toString());
            billing.setPostCode(Objects.requireNonNull(mPostalCode.getText()).toString());
            billing.setPhone(Objects.requireNonNull(mPhone.getText()).toString());
            billing.setEmail(Objects.requireNonNull(mEmail.getText()).toString());
            order.setBilling(billing);

            Shipping shipping = new Shipping();
            order.setShipping(shipping);

            List<OrderItem> items = new ArrayList<>();
            items.add(new OrderItem(selectedProduct.getId(), 1));
            order.setOrderItems(items);

            order.setPaymentMethod("admedia_gateway");
            order.setPaymentMethodTitle("Admedia OrangeMoney");
            order.setStatus("pending");

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(EndpointConstants.wcBaseUrl)
                    .client(new OkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            WordPressService apiService = retrofit.create(WordPressService.class);
            Call call = apiService.saveOrder(order);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                    if (response.body() != null)
                    {
                        showProgress(false);
                        MessageUtil.getInstance().ToastMessage(getApplicationContext(), "Votre commande est en cours de traitement");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                    showProgress(false);
                    if (!call.isCanceled())
                    {
                        MessageUtil.getInstance().ToastMessage(getApplicationContext(), getString(R.string.cannot_fetch_data));
                    }
                }
            });
        }
    }

    private void showProgress(boolean show) {
        if (!show)
        {
            if (mProgressBar != null)
            {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    private boolean isValid() {
        boolean isValid = true;

        if (mFirstName.getText() == null || mFirstName.getText().toString().isEmpty())
        {
            mLayoutFirstName.setError("Ce champs est obligatoire");
            isValid = false;
        }
        if (mLastName.getText() == null || mLastName.getText().toString().isEmpty())
        {
            mLayoutLastName.setError("Ce champs est obligatoire");
            isValid = false;
        }
        if (mCountry.getText() == null || mCountry.getText().toString().isEmpty())
        {
            mLayoutCountry.setError("Ce champs est obligatoire");
            isValid = false;
        }
        if (mAddress.getText() == null || mAddress.getText().toString().isEmpty())
        {
            mLayoutAddress.setError("Ce champs est obligatoire");
            isValid = false;
        }
        if (mCity.getText() == null || mCity.getText().toString().isEmpty())
        {
            mLayoutCity.setError("Ce champs est obligatoire");
            isValid = false;
        }
        if (mState.getText() == null || mState.getText().toString().isEmpty())
        {
            mLayoutState.setError("Ce champs est obligatoire");
            isValid = false;
        }
        if (mPostalCode.getText() == null || mPostalCode.getText().toString().isEmpty())
        {
            mLayoutPostalCode.setError("Ce champs est obligatoire");
            isValid = false;
        }
        if (mPhone.getText() == null || mPhone.getText().toString().isEmpty())
        {
            mLayoutPhone.setError("Ce champs est obligatoire");
            isValid = false;
        }
        if (mEmail.getText() == null || mEmail.getText().toString().isEmpty())
        {
            mLayoutMail.setError("Ce champs est obligatoire");
            isValid = false;
        }

        return isValid;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            finish();

            Intent intent = new Intent(getApplicationContext(), KiosqueActivity.class);
            intent.putExtra(USE_CACHE_DATA, false);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
