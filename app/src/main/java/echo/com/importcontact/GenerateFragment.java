package echo.com.importcontact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenerateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenerateFragment extends Fragment {

    @Bind(R.id.currentCityTextView)
    TextView currentCityTextView;
    @Bind(R.id.numberEditText)
    EditText numberEditText;

    String currentCity;

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
        return view;
    }

    @OnClick(R.id.cityListTextView)
    public void showCityList() {
        Intent intent = new Intent(getActivity(), CityListActivity.class);
        startActivityForResult(intent, MainActivity.REQUEST_CODE_CITY);
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
