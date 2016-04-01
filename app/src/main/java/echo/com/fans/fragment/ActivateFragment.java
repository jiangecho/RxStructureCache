package echo.com.fans.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.echo.Token;
import com.echo.data.DataManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import echo.com.fans.App;
import echo.com.fans.R;
import echo.com.fans.utils.ClipUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActivateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivateFragment extends Fragment {

    @Bind(R.id.keyEditText)
    EditText keyEditText;
    @Bind(R.id.deviceIdEditText)
    EditText deviceIdEditText;

    SharedPreferences sharedPreferences;

    public ActivateFragment() {
        // Required empty public constructor
    }

    public static ActivateFragment newInstance() {
        ActivateFragment fragment = new ActivateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activate, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void init() {
        sharedPreferences = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        String key = sharedPreferences.getString("key", "");
        if (!TextUtils.isEmpty(key)) {
            keyEditText.setText(key);
        }
        String deviceId = sharedPreferences.getString("deviceId", "");
        deviceIdEditText.setText(deviceId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @OnClick(R.id.activateButton)
    public void activate() {
        final String key = keyEditText.getText().toString();
        if (TextUtils.isEmpty(key)) {
            Toast.makeText(getActivity(), "激活码错误", Toast.LENGTH_LONG).show();
            return;
        }

        DataManager.getInstance().activate(key, new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.body().getCode() == 0) {
                    App.getInstance().isActivated = true;
                    sharedPreferences.edit().putString("key", key).commit();
                    Toast.makeText(getActivity(), "激活成功 ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "激活失败: " + response.body().getMsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(getActivity(), "激活失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.copyButton)
    public void copy() {
        ClipUtil.clip(getActivity(), "deviceId", deviceIdEditText.getText().toString());
        Toast.makeText(getActivity(), "已复制到剪切板", Toast.LENGTH_SHORT).show();
    }
}
