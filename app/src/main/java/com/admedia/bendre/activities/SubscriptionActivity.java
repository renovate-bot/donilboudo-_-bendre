package com.admedia.bendre.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.admedia.bendre.R;
import com.admedia.bendre.util.MenuUtil;

import java.util.Objects;

import static com.admedia.bendre.activities.PostDetailsActivity.POST_TYPE;

public class SubscriptionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView mTypeAbonnement;
    private TextInputEditText mNomUtilisateur;
    private TextInputEditText mMotDePasse;
    private TextInputEditText mConfirmerMotDePasse;
    private TextInputEditText mEmail;
    private TextInputEditText mConfirmerEmail;

    private TextInputEditText mPrenom;
    private TextInputEditText mNom;
    private TextInputEditText mAdresse1;
    private TextInputEditText mAdresse2;
    private TextInputEditText mVille;
    private TextInputEditText mPays;
    private TextInputEditText mTelephone;

    private LinearLayout mInfoCompte;
    private LinearLayout mInfoFacturation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_subscription);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MenuUtil.getInstance().setNavigationView(this, navigationView, drawer);

        mNomUtilisateur = findViewById(R.id.subscription_nom_utilisateur);
        mMotDePasse = findViewById(R.id.subscription_mot_passe);
        mConfirmerMotDePasse = findViewById(R.id.subscription_confirmer_mot_passe);
        mEmail = findViewById(R.id.subscription_email);
        mConfirmerEmail = findViewById(R.id.subscription_confirmer_email);

        mPrenom = findViewById(R.id.subscription_prenom);
        mNom = findViewById(R.id.subscription_nom);
        mAdresse1 = findViewById(R.id.subscription_adresse1);
        mAdresse2 = findViewById(R.id.subscription_adresse2);
        mVille = findViewById(R.id.subscription_ville);
        mPays = findViewById(R.id.subscription_pays);
        mTelephone = findViewById(R.id.subscription_telephone);

        mInfoCompte = findViewById(R.id.info_compte);
        mInfoFacturation = findViewById(R.id.info_facturation);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(), PostsActivity.class);
            intent.putExtra(POST_TYPE, getString(R.string.menu_a_la_une));
            startActivity(intent);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuUtil.getInstance().openPage(getApplicationContext(), item.getItemId());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    public void cancel(View view) {
//        startActivity(new Intent(getApplicationContext(), MyMenuActivity.class));
//    }

    public void submit(View view) {
        boolean cancel = false;
        View focusView = null;

        String nomUtilisateur = Objects.requireNonNull(mNomUtilisateur.getText()).toString();
        String motDePasse = Objects.requireNonNull(mMotDePasse.getText()).toString();
        String confirmerMotDePasse = Objects.requireNonNull(mConfirmerMotDePasse.getText()).toString();
        String email = Objects.requireNonNull(mEmail.getText()).toString();
        String confirmerEmail = Objects.requireNonNull(mConfirmerEmail.getText()).toString();

        if (TextUtils.isEmpty(nomUtilisateur))
        {
            mNomUtilisateur.setError("Le nom d'utilisateur est obligatoire");
            focusView = mNomUtilisateur;
            cancel = true;
        }
        if (TextUtils.isEmpty(motDePasse))
        {
            mMotDePasse.setError("Le mot de passe est obligatoire");
            focusView = mMotDePasse;
            cancel = true;
        }
        if (!TextUtils.isEmpty(motDePasse))
        {
            if (TextUtils.isEmpty(confirmerMotDePasse))
            {
                mConfirmerMotDePasse.setError("Veuillez confirmer le mot de passe");
                focusView = mConfirmerMotDePasse;
                cancel = true;
            }
            else if (!motDePasse.equals(confirmerMotDePasse))
            {
                mConfirmerMotDePasse.setError("Veuiller rentrer les meme mots de passe");
                focusView = mConfirmerMotDePasse;
                cancel = true;
            }
        }
        if (TextUtils.isEmpty(email))
        {
            mEmail.setError("Le email est obligatoire");
            focusView = mEmail;
            cancel = true;
        }
        if (!TextUtils.isEmpty(email))
        {
            if (TextUtils.isEmpty(confirmerEmail))
            {
                mConfirmerEmail.setError("Veuillez confirmer le email");
                focusView = mConfirmerEmail;
                cancel = true;
            }
            else if (!motDePasse.equals(confirmerEmail))
            {
                mConfirmerEmail.setError("Veuiller rentrer les meme email");
                focusView = mConfirmerEmail;
                cancel = true;
            }
        }

        if (cancel)
        {
            focusView.requestFocus();
        }
        else
        {
            //submit
        }
    }

    public void chooseSubscription(View view) {
    }

    public void goBack() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK)
//        {
//            onBackPressed();
//            //kill activity
//            finish();
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }
}
