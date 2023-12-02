package com.example.du_an_1_nhom_12.FRAGMENT;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.du_an_1_nhom_12.Activity.FeedbackActivity;
import com.example.du_an_1_nhom_12.Activity.RateAppActivity;
import com.example.du_an_1_nhom_12.Activity.ScanPdfActivity;
import com.example.du_an_1_nhom_12.Activity.WritePdfActivity;
import com.example.du_an_1_nhom_12.BuildConfig;
import com.example.du_an_1_nhom_12.R;
import com.example.du_an_1_nhom_12.SUPPORT.OnSingleClickListener;
public class FragSetting extends Fragment {
    public boolean isActivityOpen = false;
    LinearLayout layout_feedback, layout_lang, layout_share,layout_rate_app,layout_scan,layout_write;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_setting,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layout_feedback = view.findViewById(R.id.layout_feedback);
        layout_feedback.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
            }
        });

        layout_lang = view.findViewById(R.id.layout_lang);
        layout_lang.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragLanguage fragLanguage = new FragLanguage();
                fm.beginTransaction().replace(R.id.frag_container_file,fragLanguage).commit();
            }
        });

        layout_share = view.findViewById(R.id.layout_share);
        layout_share.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "File Manager");
                    String shareMessage = "\n\n" + "File management application\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.microsoft.office.excel&pcampaignid=web_share" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Choose one"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        layout_rate_app = view.findViewById(R.id.layout_rate);
        layout_rate_app.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                startActivity(new Intent(getActivity(), RateAppActivity.class));
            }
        });

        layout_scan = view.findViewById(R.id.layout_scan);
        layout_scan.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                startActivity(new Intent(getActivity(), ScanPdfActivity.class));
            }
        });

        layout_write = view.findViewById(R.id.layout_write);
        layout_write.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                startActivity(new Intent(getActivity(), WritePdfActivity.class));
            }
        });
    }

//SHARE FILE
//    Intent intent = new Intent(Intent.ACTION_SEND);
//    intent.setType("application/pdf");
//    File pdfFile = new File("/path/to/your/pdf/file.pdf");
//    Uri pdfUri = Uri.fromFile(pdfFile);
//    intent.putExtra(Intent.EXTRA_STREAM, pdfUri);
//    startActivity(Intent.createChooser(intent, "Chia sẻ tài liệu PDF"));

}
