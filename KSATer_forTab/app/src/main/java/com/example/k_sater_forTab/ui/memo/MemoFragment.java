package com.example.k_sater_forTab.ui.memo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.k_sater_forTab.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MemoFragment extends Fragment {
    TextView titleText, memoText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_memo, container, false);
        ArrayList<String> midList = new ArrayList<String>();
        try {
            FileInputStream inFs = getActivity().getApplicationContext().openFileInput("memoNames.txt");
            byte[] txt = new byte[1000]; //조정 가능
            inFs.read(txt);
            String str = (new String(txt)).trim();
            if (!str.equals("")) {
                String[] memoNames = str.split(",");
                for (String memoName : memoNames) {
                    midList.add(memoName);
                }
            }
            inFs.close();
        } catch (IOException e) {
            try {
                FileOutputStream outFs = getActivity().getApplicationContext().openFileOutput("memoNames.txt", Context.MODE_PRIVATE);
                outFs.close();
            } catch (IOException _e) {
            }
        }
        titleText = (TextView) root.findViewById(R.id.titleText);
        memoText = (TextView) root.findViewById(R.id.memoText);
        ListView list = (ListView) root.findViewById(R.id.listView_memo);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, midList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView)view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.BLACK);
                return view;
            }
        };
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = (View) View.inflate(getActivity(), R.layout.memo_dialog, null);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                dlg.setTitle("새 메모");
                dlg.setIcon(R.drawable.ic_menu_memo);
                dlg.setView(dialogView);
                dlg.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText dlgEdt1 = (EditText) dialogView.findViewById(R.id.dlgEdt1);
                        EditText dlgEdt2 = (EditText) dialogView.findViewById(R.id.dlgEdt2);
                        String title = dlgEdt1.getText().toString().trim().length() == 0 ? "새 메모" : dlgEdt1.getText().toString().trim();
                        String content = dlgEdt2.getText().toString().trim().length() == 0 ? "새 메모입니다" : dlgEdt2.getText().toString().trim();

                        makeMemo(midList, title, content);

                        adapter.notifyDataSetChanged();
                    }
                });

                dlg.show();
                ViewGroup.LayoutParams params = dialogView.getLayoutParams();
                params.width = 1050;
                params.height = 800;
                dialogView.setLayoutParams(params);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                try {
                    FileInputStream inFs = getActivity().getApplicationContext().openFileInput(midList.get(arg2) + ".txt");
                    byte[] txt = new byte[3000]; //조정 가능
                    inFs.read(txt);
                    String str = (new String(txt)).trim();
                    memoText.setText(str);
                    inFs.close();
                } catch (IOException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                }
                titleText.setText(midList.get(arg2));
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                View dialogView = (View) View.inflate(getActivity(), R.layout.memo_dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                dlg.setTitle("메모 수정하기");
                dlg.setIcon(R.drawable.ic_menu_memo);
                dlg.setView(dialogView);

                EditText dlgEdt1 = (EditText) dialogView.findViewById(R.id.dlgEdt1);
                EditText dlgEdt2 = (EditText) dialogView.findViewById(R.id.dlgEdt2);
                dlgEdt1.setText(midList.get(arg2));
                try {
                    FileInputStream inFs = getActivity().getApplicationContext().openFileInput(midList.get(arg2) + ".txt");
                    byte[] txt = new byte[3000]; //조정 가능
                    inFs.read(txt);
                    String str = (new String(txt)).trim();
                    dlgEdt2.setText(str);
                    inFs.close();
                } catch (IOException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                }

                dlg.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String title = dlgEdt1.getText().toString().trim().length() == 0 ? "새 메모" : dlgEdt1.getText().toString().trim();
                        String content = dlgEdt2.getText().toString().trim().length() == 0 ? "새 메모입니다" : dlgEdt2.getText().toString().trim();

                        removeMemo(midList, arg2);
                        makeMemo(midList, title, content);
                        adapter.notifyDataSetChanged();
                    }
                });

                dlg.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeMemo(midList, arg2);
                        adapter.notifyDataSetChanged();
                    }
                });

                dlg.show();
                ViewGroup.LayoutParams params = dialogView.getLayoutParams();
                params.width = 1050;
                params.height = 800;
                dialogView.setLayoutParams(params);
                return false;
            }
        });

        return root;
    }

    void makeMemo(ArrayList<String> midList, String title, String content) {
        if (midList.contains(title)) {
            int i = 1;
            while (true) {
                String title_changed = title + "(" + i + ")";
                if (midList.contains(title_changed)) {
                    i++;
                } else {
                    title = title_changed;
                    break;
                }
            }
        }

        try {
            FileOutputStream outFs = getActivity().getApplicationContext().openFileOutput(title + ".txt", Context.MODE_PRIVATE);
            outFs.write(content.getBytes());
            outFs.close();
        } catch (IOException e) {
        }

        String str = "";
        try {
            FileInputStream inFs = getActivity().getApplicationContext().openFileInput("memoNames.txt");
            byte[] txt = new byte[1000]; //조정 가능
            inFs.read(txt);
            String raw_str = (new String(txt)).trim();
            if (raw_str.equals("")) {
                str = title;
            } else {
                str = raw_str + "," + title;
            }
            inFs.close();
        } catch (IOException e) {
            Toast.makeText(getActivity().getApplicationContext(), "오류가 발생했습니다", Toast.LENGTH_SHORT).show();
        }

        try {
            FileOutputStream outFs = getActivity().getApplicationContext().openFileOutput("memoNames.txt", Context.MODE_PRIVATE);
            outFs.write(str.getBytes());
            outFs.close();
        } catch (IOException e) {
        }

        midList.add(title);
        titleText.setText(title);
        memoText.setText(content);
    }

    void removeMemo(ArrayList<String> midList, int i) {
        String title = midList.get(i);
        midList.remove(i);
        try {
            FileOutputStream outFs = getActivity().getApplicationContext().openFileOutput("memoNames.txt", Context.MODE_PRIVATE);
            if (midList.size() == 0) {
                outFs.write("".getBytes());
            }
            if (midList.size() == 1) {
                outFs.write((midList.get(0)).getBytes());
            }
            if (midList.size() > 1) {
                String str;
                str = midList.get(0);
                for (int index = 1; index < midList.size(); index++) {
                    str = str + "," + midList.get(index);
                }
                outFs.write(str.getBytes());
            }
            File f = new File("/data/data/com.example.k_sater_forTab/files/"+title+".txt");
            f.delete();
            outFs.close();
        } catch (IOException e) {
        }
    }
}