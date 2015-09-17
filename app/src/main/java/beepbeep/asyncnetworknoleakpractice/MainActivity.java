package beepbeep.asyncnetworknoleakpractice;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.button)
    Button mButton;


    @Bind(R.id.text)
    TextView mTextView;
    WeakReference<TextView> mWRTextView;

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    WeakReference<ProgressBar> mWRProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mWRTextView = new WeakReference<TextView>(mTextView);
        mWRProgressBar = new WeakReference<ProgressBar>(mProgressBar);

    }

    @OnClick(R.id.button)
    public void buttonOnClick(){
        mTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        new NetworkTask().execute("http://reddit.com/r/androiddev.json");
    }

    private class NetworkTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            String resultString = null;
            try {
                resultString = run(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return resultString;
        }

        String run(String url) throws IOException {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                                    .url(url)
                                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        }

        protected void onPostExecute(String result) {
            TextView textView = mWRTextView.get();
            ProgressBar progressBar = mWRProgressBar.get();

            if (textView != null && progressBar != null) {
                mWRTextView.get().setVisibility(View.VISIBLE);
                mWRProgressBar.get().setVisibility(View.GONE);

                if (result != null) {
                    mWRTextView.get().setText(result);
                } else {
                    mWRTextView.get().setText("An error has occurred.");
                }
            }
        }
    }
}
