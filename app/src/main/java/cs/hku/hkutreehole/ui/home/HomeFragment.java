package cs.hku.hkutreehole.ui.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cs.hku.hkutreehole.EmailService;
import cs.hku.hkutreehole.R;
import cs.hku.hkutreehole.databinding.FragmentHomeBinding;
import cs.hku.hkutreehole.ui.login.LoginActivity;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private final String userInfoUrl = "http://175.178.42.68:12345/appProject/userInfo.php";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView2 = root.findViewById(R.id.emailAdressTextView);
        final Button buttonSave = root.findViewById(R.id.buttonGetVerificationCode);
        final Button buttonLogOut = root.findViewById(R.id.buttonLogOut);
        final Button buttonAnalyze = root.findViewById(R.id.buttonEmotionAnalysis);
        Intent intent = getActivity().getIntent();
        String emailAddres = intent.getStringExtra("EmailAddress");
        textView2.setText(emailAddres);
        connect("");


        //Set Listener
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText emailAddress = root.findViewById(R.id.editTextTextEmailAddress);
                //emailAddress.setText("hamster");
                //textView2.setText("hamster")

                try {
                    okhttpconnet();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    okhttpconnet();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public String ReadBufferedHTML(BufferedReader reader, char [] htmlBuffer, int bufSz) throws java.io.IOException
    {
        htmlBuffer[0] = '\0';
        int offset = 0;
        do {
            int cnt = reader.read(htmlBuffer, offset, bufSz - offset);
            if (cnt > 0) {
                offset += cnt;
            } else {
                break;
            }
        } while (true);
        return new String(htmlBuffer);
    }

    public String getJsonPage(String url) {
        HttpURLConnection conn_object = null;
        final int HTML_BUFFER_SIZE = 2*1024*1024;
        char htmlBuffer[] = new char[HTML_BUFFER_SIZE];

        try {
            URL url_object = new URL(url);
            conn_object = (HttpURLConnection) url_object.openConnection();
            conn_object.setInstanceFollowRedirects(true);
            BufferedReader reader_list = new BufferedReader(new InputStreamReader(conn_object.getInputStream()));
            String HTMLSource = ReadBufferedHTML(reader_list, htmlBuffer, HTML_BUFFER_SIZE);
            reader_list.close();
            return HTMLSource;
        } catch (Exception e) {
            System.out.println("Exception caught!");
            return "Fail to login";
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            if (conn_object != null) {
                conn_object.disconnect();
            }
        }
    }

    protected void alert(String title, String mymessage){
        new AlertDialog.Builder(getContext())
                .setMessage(mymessage)
                .setTitle(title)
                .setCancelable(true)
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton){}
                        }
                )
                .show();
    }

    public void parse_JSON_String_and_Switch_Activity(String JSONString) {
        String userName = "test";
        String postCount = "user";
        try {
            JSONObject rootJSONObj = new JSONObject(JSONString);
            userName = rootJSONObj.getString("userName");
            postCount = rootJSONObj.getString("postCount");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        EditText emailAddress = getActivity().findViewById(R.id.editTextTextEmailAddress);
        emailAddress.setText(userName+"connet");
    }

    public void connect(final String name){
        final ProgressDialog pdialog = new ProgressDialog(getContext());

        pdialog.setCancelable(false);
        pdialog.setMessage("Connecting ...");
        pdialog.show();

        final String url = userInfoUrl + (name.isEmpty() ? "" : "?action=insert&name=" + android.net.Uri.encode(name, "UTF-8"));

        ExecutorService executor = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                boolean success = true;
                pdialog.setMessage("Before ...");
                pdialog.show();
                String jsonString = getJsonPage(url);
                if (jsonString.equals("Fail to login"))
                    success = false;
                boolean finalSuccess = success;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (finalSuccess) {
                            parse_JSON_String_and_Switch_Activity(jsonString);
                            System.out.println(jsonString);
                        } else {
                            alert( "Error", "Fail to connect" );
                        }
                        pdialog.hide();
                    }
                });
            }
        });
    }

    public void okhttpconnet() throws IOException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable(){
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                String token = "24.cf88be4f1ba5a3c565aef936513ed7f0.2592000.1652869102.282335-25999734";
                MediaType mediaType = MediaType.parse("application/json");
                String textAnalysis = "I'm very happy today";
                TextView textViewToAnalyze = getActivity().findViewById(R.id.multiAutoCompleteTextView);
                textAnalysis = textViewToAnalyze.getText().toString().trim();
                RequestBody body = RequestBody.create(mediaType, "{\r\n    \"text\": \""+textAnalysis+"\"\r\n}");
                Request request = new Request.Builder()
                        .url("https://aip.baidubce.com/rpc/2.0/nlp/v1/sentiment_classify?access_token="+token+"&charset=UTF-8")
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String jsonData = Objects.requireNonNull(response.body()).string();
                    JSONObject emoAnalysisResult = new JSONObject(jsonData).getJSONArray("items").getJSONObject(0);
                    System.out.println(emoAnalysisResult.getString("positive_prob"));
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            TextView textViewPositiveValue = getActivity().findViewById(R.id.PositiveValue);
                            TextView textViewNegativeValue = getActivity().findViewById(R.id.NegativeValue);
                            try {
                                Double posProb = Double.parseDouble(emoAnalysisResult.getString("positive_prob"));
                                Double negProb = Double.parseDouble(emoAnalysisResult.getString("negative_prob"));
                                DecimalFormat df = new DecimalFormat("00.00%");
                                String posProbStr = df.format(posProb);
                                String negProbStr = df.format(negProb);
                                textViewPositiveValue.setText(posProbStr);
                                textViewNegativeValue.setText(negProbStr);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}