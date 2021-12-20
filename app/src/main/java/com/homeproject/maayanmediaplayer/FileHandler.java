package com.homeproject.maayanmediaplayer;

import android.content.Context;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class FileHandler {
    public static void writeToFile(final Context context, final ArrayList<Song> arrayList) {
        new Thread(new Runnable() { // from class: com.homeproject.maayanmediaplayer;.FileHandler.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(context.openFileOutput("arrayList", 0));
                    objectOutputStream.writeObject(arrayList);
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static ArrayList<Song> readFromFile(Context context) {
        Exception e;
        ArrayList<Song> arrayList = null;
        try {
            FileInputStream openFileInput = context.openFileInput("arrayList");
            ArrayList<Song> arrayList2 = (ArrayList) new ObjectInputStream(openFileInput).readObject();
            try {
                openFileInput.close();
                return arrayList2;
            } catch (IOException e2) {
                e = e2;
                arrayList = arrayList2;
                e.printStackTrace();
                return arrayList;
            }
        } catch (IOException e3) {
            e = e3;
        } catch (ClassNotFoundException e4) {
            e = e4;
        }
        return arrayList;
    }
}