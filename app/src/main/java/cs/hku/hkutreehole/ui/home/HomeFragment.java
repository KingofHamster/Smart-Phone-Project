package cs.hku.hkutreehole.ui.home;

import android.os.Bundle;
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

import cs.hku.hkutreehole.EmailService;
import cs.hku.hkutreehole.R;
import cs.hku.hkutreehole.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView2 = root.findViewById(R.id.emailAdressTextView);
        final Button button = root.findViewById(R.id.buttonGetVerificationCode);
        textView2.setText("这是首页页面");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText emailAddress = root.findViewById(R.id.emailAdressTextView);
                EmailService.startActionSendEmail(getContext(), "hamster1", "hamster2");
                //注册
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}