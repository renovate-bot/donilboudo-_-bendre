package com.admedia.bendre.util;

import android.content.Context;

import com.admedia.bendre.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriesUtil {
    public Map<Integer, String> categories;
    public static CategoriesUtil instance = null;
    public static final String POST_TYPE = "postType";

    public CategoriesUtil() {
        categories = new HashMap<>();
        loadData();
    }

    public static CategoriesUtil getInstance() {
        if (instance == null)
        {
            instance = new CategoriesUtil();
        }

        return instance;
    }

    private void loadData() {
        categories.put(6, "Edito");
        categories.put(14, "Actu Express");
        categories.put(29, "168h au Faso");
        categories.put(30, "Confidentiel");
        categories.put(15, "Politique");
        categories.put(26, "Focale");
        categories.put(27, "Reflex");
        categories.put(28, "Géopolitique");
        categories.put(18, "Société");
        categories.put(23, "Lettre à HS");
        categories.put(24, "Développement");
        categories.put(25, "Coin des femmes");
        categories.put(17, "Point barre");
        categories.put(21, "Profil haut");
        categories.put(22, "Profil bas");
        categories.put(16, "Bendremetrie");
        categories.put(12, "Faits-divers");
        categories.put(13, "Bon à savoir");
        categories.put(20, "Sagesse africain");
        categories.put(7, "Bendrescopie");
        categories.put(8, "Arrêt sur l’Histoire");
        categories.put(9, "Ils ont dit");
        categories.put(10, "Feu vert");
        categories.put(11, "A bout portant");
    }

    public String getCategoriesString(List<Long> postCategories) {
        StringBuilder categoriesString = new StringBuilder();
        for (Long item : postCategories)
        {
            String category = categories.get(item.intValue());
            if (category != null)
            {
                if (categoriesString.length() > 0)
                {
                    categoriesString.append(", ");
                }
                categoriesString.append(category);
            }

        }
        return categoriesString.toString();
    }

    public String getCategoriesString(Context context, String postType) {
        String categoriesString;
        if (postType.equals(context.getString(R.string.menu_burkina)))
        {
            categoriesString = "15+26+27+28+18+23+24+25";
        }
        else if (postType.equals(context.getString(R.string.menu_international)))
        {
            categoriesString = "229";
        }
        else if (postType.equals(context.getString(R.string.menu_labo)))
        {
            categoriesString = "112";
        }
        else if (postType.equals(context.getString(R.string.menu_opportunite)))
        {
            categoriesString = "13";
        }
        else
        {
            categoriesString = "14+29+30";
        }

        return categoriesString;
    }

}
