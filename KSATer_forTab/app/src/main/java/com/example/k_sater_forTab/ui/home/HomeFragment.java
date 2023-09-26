package com.example.k_sater_forTab.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.k_sater_forTab.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends Fragment {

    ProgressBar progress;
    TextView progressText, saying;
    ImageView firecracker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        progress = (ProgressBar) root.findViewById(R.id.progress);
        progressText = (TextView) root.findViewById(R.id.progressText);
        firecracker = (ImageView) root.findViewById(R.id.firecracker);
        saying = (TextView) root.findViewById(R.id.saying);
        progress.setProgress(0);
        setProgressBar(progress, progressText, firecracker);
        setPhrase(saying);

        Button btn_quick = (Button) root.findViewById(R.id.btn_quick);
        EditText quicknote = (EditText) root.findViewById(R.id.quicknote);
        try {
            FileInputStream inFs = getActivity().getApplicationContext().openFileInput("quickmemo.txt");
            byte[] txt = new byte[1000]; //조정 가능
            inFs.read(txt);
            String str = (new String(txt)).trim();
            quicknote.setText(str);
            inFs.close();
        } catch (IOException e) {
            try {
                FileOutputStream outFs = getActivity().getApplicationContext().openFileOutput("quickmemo.txt", Context.MODE_PRIVATE);
                outFs.close();
            } catch (IOException _e) {
            }
        }

        btn_quick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = quicknote.getText().toString().trim();
                try {
                    FileOutputStream outFs = getActivity().getApplicationContext().openFileOutput("quickmemo.txt", Context.MODE_PRIVATE);
                    outFs.write(str.getBytes());
                    outFs.close();
                    Toast.makeText(getActivity().getApplicationContext(), "저장되었습니다", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                }
            }
        });

        return root;
    }

    void setProgressBar(ProgressBar progress, TextView progressText, ImageView firecracker) {
        try {
            FileInputStream inFs1 = getActivity().getApplicationContext().openFileInput("todolist.txt");
            byte[] txt1 = new byte[1000]; //조정 가능
            inFs1.read(txt1);
            String[] str1 = (new String(txt1)).trim().split(",");
            int whole = str1[0].equals("") ? 0 : str1.length;
            inFs1.close();

            FileInputStream inFs2 = getActivity().getApplicationContext().openFileInput("todolist_selected.txt");
            byte[] txt2 = new byte[1000]; //조정 가능
            inFs2.read(txt2);
            String[] str2 = (new String(txt2)).trim().split(",");
            int count = str2[0].equals("") ? 0 : str2.length;
            inFs2.close();

            int p;
            if (whole != 0) {
                p = (int) 100 * count / whole;
            } else {
                p = 0;
            }

            progress.setProgress(p);
            progressText.setText(p + "%");

            if(p==100)
                firecracker.setVisibility(View.VISIBLE);
            else
                firecracker.setVisibility(View.INVISIBLE);
        } catch (IOException e) {
            Toast.makeText(getActivity().getApplicationContext(), "오류가 발생했습니다", Toast.LENGTH_SHORT).show();
        }
    }

    void setPhrase(TextView phrase) {
        String[] sayings = null;
        try {
            FileInputStream inFs = getActivity().getApplicationContext().openFileInput("sayings.txt");
            byte[] txt = new byte[2000]; //조정 가능
            inFs.read(txt);
            String str = (new String(txt)).trim();
            sayings = str.split("/");
            inFs.close();
        } catch (IOException e) {
            try {
                FileOutputStream outFs = getActivity().getApplicationContext().openFileOutput("sayings.txt", Context.MODE_PRIVATE);
                sayings = new String[]{"지금 니가 편한 이유는 내리막길을 가고 있기 때문이다",
                                        "후회하기 싫으면 그렇게 살지 말고, 그렇게 살거면 후회하지 마라",
                                        "인생이 끝날까 두려워하지 마라. 당신의 인생이 시작조차 하지 않을 수 있음을 두려워하라.",
                                        "멈추지 말고 한 가지 목표에 매진하라. 그것이 성공의 비결이다.",
                                        "기회는 일어나는 것이 아니라 만들어내는 것이다",
                                        "남의 성공은 반드시 이유가 있고, 내 실패 또한 반드시 이유가 있다",
                                        "불합격은 시험 당일 결정되는 것이 아니라 바로 이 순간 결정된다. 지금 당신은 합격인가?",
                                        "평범하게 살고 싶지 않은데 왜 평범하게 노력하는가?",
                                        "공부는 시간이 부족한 것이 아니라 노력이 부족한 것이다.",
                                        "가격은 당신이 지불하는 것이고, 가치는 당신이 얻는 것이다.",
                                        "공부가 뭐 대수냐. 후회가 무서운거지.",
                                        "오늘 걷지 않는다면 내일 뛰어야 한다",
                                        "공부할 시간이 부족하다는 사람은 핑계이다. 부족한 건 당신의 노력이다.",
                                        "가장 공부가 안되는 것 같고 남들에게 뒤쳐진다고 생각하는 그 순간이 당신이 진정으로 공부하는 순간이다.",
                                        "생각만 하지 말고 바로 실천에 옮겨라. 그럼 뭘 해야할지 알 수 있다.",
                                        "긍정적인 태도는 강력한 힘을 갖는다. 그 어느 것도 그것을 막을 수 없다.",
                                        "잘난 건 타고 난 거지만, 잘 사는 건 자기 하기 나름이다",
                                        "나는 폭풍이 두렵지 않다. 나의 배로 항해하는 법을 배우고 있으니까.",
                                        "군자는 말이 어눌해도 행동에는 민첩하다",
                                        "기초 없이 이룬 성취는 단계를 오르는게 아니라 성취 후 다시 바닥으로 오게 된다.",
                                        "고난이란 최선을 다 할 기회이다",
                                        "포기하면 그 순간이 시합종료다",
                                        "최선을 다하고 나머지는 잊어라",
                                        "진정한 노력은 결코 배신하지 않는다. 평범한 노력은 노력이 아니다.",
                                        "나만이 내 인생을 바꿀 수 있다. 아무도 날 대신해 줄 수 없다.",
                                        "성공의 비결은 좌절하지 않고 극복하는데 있다",
                                        "가장 유능한 사람은 가장 배움에 힘쓰는 사람이다",
                                        "바람이 불지 않을 때 바람개비를 돌리는 방법은 앞으로 달려 나가는 것이다",
                                        "실패란 넘어지는 것이 아니라 넘어진 자리에 머무는 것이다",
                                        "고뇌에 지는 것은 수치가 아니다. 쾌락에 지는 것이야말로 수치다.",
                                        "1분 전 만큼 먼 시간은 없다",
                                        "보리가 싹을 틔우기 위해서 씨는 죽어야 한다",
                                        "늦게 시작하는 것을 두려워 말고, 하다 중단하는 것을 두려워해라",
                                        "사람은 할 수 있다고 생각하기에 할 수 있는 것이다",
                                        "고통을 주지 않는 것은 쾌락도 주지 않는다",
                                        "시작할 때 위대할 필요는 없다. 그러나 시작하면 위대해진다.",
                                        "네가 아무리 먼 꿈을 꾸어도 너 자신이 가능성을 믿는 한 그것은 손이 닿는 곳에 있다",
                                        "꿈을 꾸기에 인생은 빛난다",
                                        "신은 우리에게 성공을 요구하지 않습니다. 오로지 도전하기를 바랄 뿐입니다.",
                                        "힘든 길을 피하지 마라. 목표에 도달하기 위해서는 나아가지 않으면 안된다.",
                                        "당신의 운명이 형성되는 것은 당신이 결단하는 순간이다",
                                        "우리가 감히 할 수 없는 것은 어렵기 때문이 아니다. 안하려고 하니까 어려운 것이다.",
                                        "오늘 할 수 있는 일에 전력을 다하라. 그렇다면 내일은 더 큰 진전이 있을 것이다.",
                                        "노력하면 보답을 받을 거라고 늘 믿어왔지",
                                        "목표를 이루기 위해서는 전력투구하는 것 외에는 방법이 없다. 거기에 지름길은 없다.",};
                String str = sayings[0];
                for(int i=1; i<sayings.length; i++) {
                    str += "/" + sayings[i];
                }
                outFs.write(str.getBytes());
                outFs.close();
            } catch (IOException _e) {
            }
        }

        Date date = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("MM");
        SimpleDateFormat sdf3 = new SimpleDateFormat("dd");
        String s = sdf1.format(date) + sdf2.format(date) + String.valueOf(Integer.parseInt(sdf3.format(date))^2);
        int key = Math.abs(s.hashCode()) % sayings.length;

        phrase.setText("\"" + sayings[key] + "\"");
    }
}