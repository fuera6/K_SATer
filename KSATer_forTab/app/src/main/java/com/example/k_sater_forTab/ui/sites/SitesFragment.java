package com.example.k_sater_forTab.ui.sites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.k_sater_forTab.R;

public class SitesFragment extends Fragment {

    ImageButton suneung, ebsi, megastudy, mimac, etoos, jongro;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sites, container, false);
        suneung = (ImageButton) root.findViewById(R.id.suneung);
        ebsi = (ImageButton) root.findViewById(R.id.ebsi);
        megastudy = (ImageButton) root.findViewById(R.id.megastudy);
        mimac = (ImageButton) root.findViewById(R.id.mimac);
        etoos = (ImageButton) root.findViewById(R.id.etoos);
        jongro = (ImageButton) root.findViewById(R.id.jongro);

        suneung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.suneung.re.kr/"));
                startActivity(mIntent);
            }
        });

        ebsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ebsi.co.kr/"));
                startActivity(mIntent);
            }
        });

        megastudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.megastudy.net/"));
                startActivity(mIntent);
            }
        });

        mimac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mimacstudy.com/"));
                startActivity(mIntent);
            }
        });

        etoos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://go3.etoos.com/"));
                startActivity(mIntent);
            }
        });

        jongro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://b.jongro.co.kr/"));
                startActivity(mIntent);
            }
        });

        return root;
    }
}