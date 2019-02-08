package com.admedia.bendre.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebView;

import com.admedia.bendre.R;
import com.admedia.bendre.util.MenuUtil;

public class AboutActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
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

        WebView webView = findViewById(R.id.wv_about);
        String text = "<p style=\"text-align: center;\"><strong>TEXTE DE PRESENTATION DE LA PLATE-FORME MOBILE</strong></p>\n" +
                "<p style=\"text-align: justify;\">Avec vous et pour vous, l&rsquo;hebdomadaire burkinab&egrave; d&rsquo;information et de r&eacute;flexion se lance dans une nouvelle aventure m&eacute;diatique, celle d&rsquo;une plate-forme num&eacute;rique d&rsquo;information num&eacute;rique participative. Plus de 25 ans apr&egrave;s avoir contribu&eacute; &agrave; la marche du Burkina Faso vers une soci&eacute;t&eacute; de plus de libert&eacute;, de justice sociale et de d&eacute;mocratie, le Tambour (Bendr&eacute; en langue nationale moor&eacute;) se met au rythme du monde contemporain. Il veut d&eacute;sormais adresser ses messages aux jeunes, aux femmes et aux Anciens dont les habitudes ont &eacute;t&eacute; profond&eacute;ment modifi&eacute;es par l&rsquo;av&egrave;nement d&rsquo;Internet et du Smartphone.</p>\n" +
                "<p style=\"text-align: justify;\">En choisissant le mod&egrave;le participatif, la plate-forme de Bendr&eacute; s&rsquo;inscrit dans le nouveau mod&egrave;le qui int&egrave;gre d&eacute;sormais la contribution des lecteurs &agrave; une information plus plurielle, plus mobile et plus partag&eacute;e. C&rsquo;est aussi et surtout un engagement renouvel&eacute; pour une culture journalistique plus proche des citoyens tout en restant soucieuse et attach&eacute;e aux valeurs cardinales d&rsquo;int&eacute;grit&eacute;, de qualit&eacute;, de pertinence et d&rsquo;exclusivit&eacute; de l&rsquo;information.</p>\n" +
                "<p style=\"text-align: justify;\">En plus des informations classiques, Bendr&eacute; num&eacute;rique, met &eacute;galement &agrave; la disposition de ses lecteurs une fen&ecirc;tre d&rsquo;informations pratiques sur les horaires des compagnies de transport, la m&eacute;t&eacute;o, les opportunit&eacute;s d&rsquo;affaires et d&rsquo;emplois, etc.</p>\n" +
                "<p style=\"text-align: justify;\">Le Bendr&eacute; num&eacute;rique veut faire participer &agrave; la recherche de l&rsquo;information sans la d&eacute;former, au d&eacute;cryptage des faits sans les galvauder, &agrave; faire r&eacute;sonner d&rsquo;avantage le &laquo;Tambour sacr&eacute;&raquo; sans d&eacute;raisonner. Bref, il s&rsquo;agit de susciter la cr&eacute;ation d&rsquo;une communaut&eacute; de lecteurs qui se d&eacute;cident &agrave; se faire entendre, &agrave; faire voir des choses dont ils sont acteurs ou t&eacute;moin, &agrave; discuter de leurs points de vue. Tout ceci selon des r&egrave;gles et des proc&eacute;dures de mod&eacute;ration accept&eacute;es et partag&eacute;es.</p>\n" +
                "<p style=\"text-align: justify;\">De ses lecteurs contributeurs, Bendr&eacute; num&eacute;rique attend de vivre une nouvelle aventure m&eacute;diatique faite d&rsquo;&eacute;changes, de propositions et d&eacute;bats instructifs et constructifs. Son ambition demeure celle de son fondateur, M. Ch&eacute;riff Sy, de galvaniser, par le son du Bendr&eacute;, tous les guerriers qui se battent pour un Burkina et un monde plus juste, plus tol&eacute;rant o&ugrave; il fait de plus en plus mieux vivre pour tous et pas seulement pour quelques-uns. Sa devise reste et demeure, &laquo;<strong>Au-del&agrave; des honneurs, il y a l&rsquo;honneur</strong>&raquo;.</p>\n" +
                "<p style=\"text-align: center;\"><strong>Copyright &copy;Bendr&eacute; |Design by <a href=\"http://www.admedia-technologies.com\">AdMedia Technologies</a></strong></p>";

        webView.loadData(text, "text/html; charset=utf-8", "utf-8");
        webView.setWebViewClient(new MyWebViewClient(this));
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
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            finishAffinity();
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MenuUtil.getInstance().openPage(getApplicationContext(), item.getItemId());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
