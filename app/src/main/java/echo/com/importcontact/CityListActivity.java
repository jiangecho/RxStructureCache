package echo.com.importcontact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Arrays;
import java.util.Comparator;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class CityListActivity extends AppCompatActivity {

    private StickyListHeadersListView stickyListHeadersListView;

    String[] cities;

    HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cities = getResources().getStringArray(R.array.cities);

        // UPPERCASE：大写  (ZHONG)
        // LOWERCASE：小写  (zhong)
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);

        Arrays.sort(cities, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                char l = firstLetter(lhs);
                char r = firstLetter(rhs);
                if (l > r) {
                    return 1;
                } else if (l == r) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        stickyListHeadersListView = (StickyListHeadersListView) findViewById(R.id.list);
        stickyListHeadersListView.setAdapter(new CityAdapter(this));
        stickyListHeadersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("city", cities[position]);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    private char firstLetter(String city) {
        String[] pinyin = null;
        try {
            pinyin = PinyinHelper.toHanyuPinyinStringArray(city.charAt(0), format);
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }

        if (pinyin == null) {
            return '*';
        } else {
            return pinyin[0].charAt(0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    class CityAdapter extends BaseAdapter implements StickyListHeadersAdapter {

        private LayoutInflater layoutInflater;

        public CityAdapter(Context context) {
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            HeaderViewHolder viewHolder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_city_header, parent, false);
                viewHolder = new HeaderViewHolder();
                viewHolder.text = (TextView) convertView.findViewById(R.id.headerTextView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (HeaderViewHolder) convertView.getTag();
            }
            viewHolder.text.setText(firstLetter(cities[position]) + "");

            return convertView;
        }

        @Override
        public long getHeaderId(int position) {
            return firstLetter(cities[position]);
        }

        @Override
        public int getCount() {
            return cities.length;
        }

        @Override
        public Object getItem(int position) {
            return cities[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_city, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.text = (TextView) convertView.findViewById(R.id.cityTextView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.text.setText(cities[position]);

            return convertView;
        }

        class HeaderViewHolder {
            TextView text;
        }

        class ViewHolder {
            TextView text;
        }
    }

}
