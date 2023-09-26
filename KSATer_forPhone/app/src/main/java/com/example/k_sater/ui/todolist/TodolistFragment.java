package com.example.k_sater.ui.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.k_sater.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class TodolistFragment extends Fragment {
    Button btnSave;
    EditText edtSave;
    ListView list;
    ProgressBar progressbar;
    TextView progress_text;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_todolist, container, false);

        ArrayList<String> midList = new ArrayList<String>();
        try {
            FileInputStream inFs = getActivity().getApplicationContext().openFileInput("todolist.txt");
            byte[] txt = new byte[1000]; //조정 가능
            inFs.read(txt);
            String str = (new String(txt)).trim();
            if (!str.equals("")) {
                String[] todos = str.split(",");
                for (String todo : todos) {
                    midList.add(todo);
                }
            }
            inFs.close();
        } catch (IOException e) {
            try {
                FileOutputStream outFs = getActivity().getApplicationContext().openFileOutput("todolist.txt", Context.MODE_PRIVATE);
                outFs.close();
            } catch (IOException _e) {
            }
        }

        list = (ListView) root.findViewById(R.id.listView1);
        btnSave = (Button) root.findViewById(R.id.btnSave);
        edtSave = (EditText) root.findViewById(R.id.edtSave);
        progressbar = (ProgressBar) root.findViewById(R.id.progressbar);
        progress_text = (TextView) root.findViewById(R.id.progress_text);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, midList);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list.setAdapter(adapter);
        try {
            FileInputStream inFs = getActivity().getApplicationContext().openFileInput("todolist_selected.txt");
            byte[] txt = new byte[100]; //조정 가능
            inFs.read(txt);
            String raw_str = (new String(txt)).trim();
            if (!raw_str.equals("")) {
                String[] checkedTodos = raw_str.split(",");
                for(String checkedTodo : checkedTodos) {
                    int index = midList.indexOf(checkedTodo);
                    list.setItemChecked(index, true);
                }
            }
            inFs.close();
        } catch (IOException e) {
            try {
                FileOutputStream outFs = getActivity().getApplicationContext().openFileOutput("todolist_selected.txt", Context.MODE_PRIVATE);
                outFs.close();
            } catch (IOException _e) {
            }
        }
        updateProgress(list, progressbar, progress_text);
        checkedSave(midList, list);
        adapter.notifyDataSetChanged();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todo = edtSave.getText().toString().trim().length() == 0 ? "새 계획" : edtSave.getText().toString().trim();
                makeTodo(midList, todo);
                adapter.notifyDataSetChanged();

                updateProgress(list, progressbar, progress_text);
                checkedSave(midList, list);
                adapter.notifyDataSetChanged();
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                View dialogView = (View) View.inflate(getActivity(), R.layout.todolist_dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                dlg.setTitle("계획 수정하기");
                dlg.setIcon(R.drawable.ic_menu_todolist);
                dlg.setView(dialogView);
                EditText dlgEdt = (EditText) dialogView.findViewById(R.id.dlgEdt);
                dlgEdt.setText(midList.get(arg2));
                dlg.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String todo = dlgEdt.getText().toString().trim().length() == 0 ? "새 계획" : dlgEdt.getText().toString().trim();
                        removeTodo(midList, arg2);
                        makeTodo(midList, todo);
                        adapter.notifyDataSetChanged();

                        updateProgress(list, progressbar, progress_text);
                        checkedSave(midList, list);
                        adapter.notifyDataSetChanged();
                    }
                });

                dlg.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeTodo(midList, arg2);
                        adapter.notifyDataSetChanged();

                        updateProgress(list, progressbar, progress_text);
                        checkedSave(midList, list);
                        adapter.notifyDataSetChanged();
                    }
                });

                dlg.show();
                return true;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                updateProgress(list, progressbar, progress_text);
                checkedSave(midList, list);
                adapter.notifyDataSetChanged();
            }
        });

        updateProgress(list, progressbar, progress_text);
        checkedSave(midList, list);
        adapter.notifyDataSetChanged();
        return root;
    }

    void makeTodo(ArrayList<String> midList, String todo) {

        if (midList.contains(todo)) {
            int i = 1;
            while (true) {
                String todo_changed = todo + "(" + i + ")";
                if (midList.contains(todo_changed)) {
                    i++;
                } else {
                    todo = todo_changed;
                    break;
                }
            }
        }

        String str = "";
        try {
            FileInputStream inFs = getActivity().getApplicationContext().openFileInput("todolist.txt");
            byte[] txt = new byte[1000]; //조정 가능
            inFs.read(txt);
            String raw_str = (new String(txt)).trim();
            if (raw_str.equals("")) {
                str = todo;
            } else {
                str = raw_str + "," + todo;
            }
            inFs.close();
        } catch (IOException e) {
            Toast.makeText(getActivity().getApplicationContext(), "오류가 발생했습니다", Toast.LENGTH_SHORT).show();
        }

        try {
            FileOutputStream outFs = getActivity().getApplicationContext().openFileOutput("todolist.txt", Context.MODE_PRIVATE);
            outFs.write(str.getBytes());
            outFs.close();
        } catch (IOException e) {
        }

        midList.add(todo);
        list.setItemChecked(list.getCount(), false);
    }

    void removeTodo(ArrayList<String> midList, int i) {
         midList.remove(i);
        try {
            FileOutputStream outFs = getActivity().getApplicationContext().openFileOutput("todolist.txt", Context.MODE_PRIVATE);
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
        } catch (IOException e) {
        }

        for(int j = i; j < list.getCount() - 1; j++) {
            list.setItemChecked(j, list.isItemChecked(j+1));
        }
        list.setItemChecked(list.getCount() - 1, false);
    }

    void updateProgress(ListView list, ProgressBar progressbar, TextView progress_text) {
        int count = 0;
        for(int i=0; i<list.getCount(); i++) {
            if(list.isItemChecked(i))
                count++;
        }
        int whole = list.getCount();
        if (whole == 0) {
            progressbar.setProgress(0);
            progress_text.setText("0%");
            return;
        } else {
            int progress = (int) 100 * count / whole;
            progressbar.setProgress(progress);
            progress_text.setText(String.valueOf(progress) + "%");
        }
    }

    void checkedSave(ArrayList<String> midList, ListView list) {
        String str = "";
        try {
            FileOutputStream outFs = getActivity().getApplicationContext().openFileOutput("todolist_selected.txt", Context.MODE_PRIVATE);
            for(int i=0; i<list.getCount(); i++) {
                if(list.isItemChecked(i)) {
                    if(str.equals(""))
                        str = midList.get(i);
                    else
                        str += "," + midList.get(i);
                }
            }
            outFs.write(str.getBytes());
            outFs.close();
        } catch (IOException e) {
        }
    }
}