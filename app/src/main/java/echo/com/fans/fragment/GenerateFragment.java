package echo.com.fans.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.echo.fans.Fans;
import com.echo.fans.FansDao;

import java.io.File;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import echo.com.fans.App;
import echo.com.fans.R;
import echo.com.fans.activity.CityListActivity;
import echo.com.fans.activity.MainActivity;
import echo.com.fans.utils.AES256Utils;
import echo.com.fans.utils.ContactUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenerateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenerateFragment extends Fragment {

    @Bind(R.id.currentCityTextView)
    TextView currentCityTextView;
    @Bind(R.id.hotCityRadioGroup)
    RadioGroup hotCityRadioGroup;
    @Bind(R.id.numberEditText)
    EditText numberEditText;

    String currentCity = "北京";
    private int count = 0;

    public GenerateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BlankFragment.
     */
    public static GenerateFragment newInstance() {
        GenerateFragment fragment = new GenerateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        hotCityRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio0:
                        currentCity = "北京";
                        break;
                    case R.id.radio1:
                        currentCity = "上海";
                        break;
                    case R.id.radio2:
                        currentCity = "广州";
                        break;
                    default:
                        currentCity = "杭州";
                        break;
                }

                currentCityTextView.setText(getString(R.string.current_city, currentCity));
            }
        });

        return view;
    }

    @OnClick(R.id.cityListTextView)
    public void showCityList() {
        Intent intent = new Intent(getActivity(), CityListActivity.class);
        startActivityForResult(intent, MainActivity.REQUEST_CODE_CITY);
    }

    @OnClick(R.id.generateButton)
    public void generate() {

        if (!App.getInstance().isActivated) {
            Toast.makeText(getActivity(), "请先激活", Toast.LENGTH_LONG).show();
            return;
        }

        String tmp = numberEditText.getText().toString();
        try {
            if (!TextUtils.isEmpty(tmp)) {
                count = Integer.parseInt(tmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            count = 0;
        }

        if (count < 1 || count > 1000) {
            Toast.makeText(getActivity(), "输入错误", Toast.LENGTH_LONG).show();
            return;
        }

        generateNumberAsync();

    }

    private boolean generateNumberSync() {
        String cityInfo = null;
        File file = new File(getActivity().getFilesDir().getAbsolutePath() + File.separator + "city" + File.separator + currentCity + ".txte");
        try {
            cityInfo = AES256Utils.decrpt(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cityInfo == null) {
            Toast.makeText(getActivity(), "加载城市错误", Toast.LENGTH_LONG).show();
            return false;
        }
        String[] baseNumbers = cityInfo.split("\\s+");
        if (baseNumbers == null) {
            Toast.makeText(getActivity(), "内部错误", Toast.LENGTH_LONG).show();
            return false;
        }

        Random random = new Random();
        String number;
        String name;
        for (int i = 0; i < count; i++) {
            number = baseNumbers[random.nextInt(baseNumbers.length)];
            number = number + String.format("%04d", random.nextInt(9999));
            name = String.format(currentCity + "_%04d", i);
            ContactUtil.insertPhoneContact(getActivity(), name, number);

            App.getInstance().getFansDao().insert(new Fans(null, name, number));
        }
        return true;
    }

    private void generateNumberAsync() {
        new AsyncTask<Void, Integer, Boolean>() {
            private MaterialDialog dialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new MaterialDialog.Builder(getActivity())
                        .title("正在生成")
                        .content("请稍后")
                        .progress(true, count)
                        .cancelable(false)
                        .build();
                dialog.show();
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                return generateNumberSync();
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                dialog.dismiss();
                if (aBoolean) {
                    Toast.makeText(getActivity(), "生成成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "生成失败", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    @OnClick(R.id.clearButton)
    public void clear() {
        if (!App.getInstance().isActivated) {
            Toast.makeText(getActivity(), "请先激活", Toast.LENGTH_LONG).show();
            return;
        }
        deleteAsync();
    }

    private void deleteAsync() {
        new AsyncTask<Void, Void, Boolean>() {
            private MaterialDialog dialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new MaterialDialog.Builder(getActivity())
                        .title("正在删除")
                        .content("请稍后")
                        .progress(true, 0)
                        .cancelable(false)
                        .build();
                dialog.show();
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                FansDao fansDao = App.getInstance().getFansDao();
                List<Fans> fanses = fansDao.loadAll();
                if (fanses == null) {
                    return false;
                }
                boolean result;
                for (Fans fans : fanses) {
                    result = ContactUtil.deleteContact(getActivity(), fans.getNumber());
                    if (result) {
                        fansDao.delete(fans);
                    }
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                dialog.dismiss();
            }

        }.execute();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.REQUEST_CODE_CITY && resultCode == Activity.RESULT_OK) {
            currentCity = data.getStringExtra("city");
            currentCityTextView.setText(getString(R.string.current_city, currentCity));

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
