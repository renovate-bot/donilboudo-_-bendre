package com.admedia.bendre.util;

import android.content.Context;

import com.admedia.bendre.model.Post;
import com.admedia.bendre.model.woocommerce.Product;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class CachesUtil {
    private static CachesUtil instance = null;
    private long cacheSize = (5 * 1024 * 1024);

    public static CachesUtil getInstance() {
        if (instance == null)
        {
            instance = new CachesUtil();
        }

        return instance;
    }

    public void createCachedFileForPosts(Context context, String key, List<Post> posts) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(posts);
        oos.close();
        fos.close();
    }

    public void createCachedFileForProducts(Context context, String key, List<Product> products) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(products);
        oos.close();
        fos.close();
    }

    public Object readCachedFile(Context context, String key) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        return ois.readObject();
    }
}
