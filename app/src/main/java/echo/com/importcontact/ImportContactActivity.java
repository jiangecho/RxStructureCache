package echo.com.importcontact;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ImportContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_contact);

    }

    public void insert(View view) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        final File file = new File(path + "/tel.txt");

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                insertConftactFromFile(ImportContactActivity.this, file);
                return null;
            }
        }.execute();
    }


    public void insertConftactFromFile(Context context, File file) {
        if (file == null || !file.exists()) {
            return;
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = bufferedReader.readLine();
            int count = 200;
            while (line != null) {
                ContactUtil.insertPhoneContact(String.format("%04d", count), line, context);
                Log.e("jyj", count + "  " + line);
                count++;
                line = bufferedReader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
