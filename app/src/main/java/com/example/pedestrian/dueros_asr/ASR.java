package com.example.pedestrian.dueros_asr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.speech.VoiceRecognitionService;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Pedestrian on 2017/7/24.
 */

public class ASR extends Activity {
    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.baidu.action.RECOGNIZE_SPEECH");
                intent.putExtra("grammar", "asset:///baidu_speech_grammar.bsg"); // 设置离线的授权文件(离线模块需要授权), 该语法可以用自定义语义工具生成, 链接http://yuyin.baidu.com/asr#m5
                //intent.putExtra("slot-data", your slots); // 设置grammar中需要覆盖的词条,如联系人名
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle results = data.getExtras();
            String origin_result = results.getString("origin_result");
            try {
                JSONObject jsonObject = new JSONObject(origin_result);
                JSONObject result = jsonObject.optJSONObject("result");
                JSONObject content = jsonObject.getJSONObject("content");
                String raw_text = result.optString("raw_text");
                if(raw_text != "")
                    textView.setText("识别结果(数组形式): " + raw_text);
                else
                    textView.setText("识别结果(数组形式): " + content.getJSONArray("item").get(0));
            } catch (JSONException e) {
                Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
