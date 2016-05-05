package activity.meijialove.com.emojitest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vk.emoji.EmojiParser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(this);
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                EmojiParser.getInstance().initConfig(MainActivity.this, EmojiParser.REGEX_STR_DEFAULT2, EmojiParser.CHINESE_EMOJI_DEFAULT);
                textView.setText(EmojiParser.getInstance().parseEmojiToChineseText(editText.getText().toString()));
                break;
        }
    }
}
