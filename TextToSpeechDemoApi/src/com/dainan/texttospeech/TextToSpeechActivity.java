package com.dainan.texttospeech;

import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * <p>Demonstrates text-to-speech (TTS). Please note the following steps:</p>
 *
 * <ol>
 * <li>Construct the TextToSpeech object.</li>
 * <li>Handle initialization callback in the onInit method.
 * The activity implements TextToSpeech.OnInitListener for this purpose.</li>
 * <li>Call TextToSpeech.speak to synthesize speech.</li>
 * <li>Shutdown TextToSpeech in onDestroy.</li>
 * </ol>
 *
 * <p>Documentation:
 * http://developer.android.com/reference/android/speech/tts/package-summary.html
 * </p>
 * <ul>
 */
public class TextToSpeechActivity extends Activity implements TextToSpeech.OnInitListener {

    private static final String TAG = "TextToSpeechDemo";

    private TextToSpeech mTts;
    private Button mAgainButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_to_speech);

        // text-to-speechの初期化. 初期化は非同期なオペレーション
        // 初期化完了後 OnInitListener (第二引数) が呼ばれる
        mTts = new TextToSpeech(this,
            this  // TextToSpeech.OnInitListener
            );

        // レイアウトファイル上ではボタンはdisabled状態
        // TTSエンジン初期化後にenable化
        mAgainButton = (Button) findViewById(R.id.again_button);

        mAgainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sayHello();
            }
        });
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown!
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }

        super.onDestroy();
    }

    // TextToSpeech.OnInitListenerのインプリメント
    public void onInit(int status) {
        // status はTextToSpeech.SUCCESS or TextToSpeech.ERROR.
        if (status == TextToSpeech.SUCCESS) {
            // 優先される言語としてUS englishをセット.
            // 言語は使用可能ではないかもしれない　resultで確認
            int result = mTts.setLanguage(Locale.US);
            //Lacle.FRANCEと指定したらどうなるか？？試してみてください！
            // Try this someday for some interesting results.
            // int result mTts.setLanguage(Locale.FRANCE);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {
               // 言語データがない、あるいは言語がサポートされていない。
                Log.e(TAG, "Language is not available.");
            } else {
                // Check the documentation for other possible result codes.
                // For example, the language may be available for the locale,
                // but not for the specified country and variant.

                // TTSが成功裏に初期化されたThe TTS
                // ボタンをenable化して押せるようにする。
                mAgainButton.setEnabled(true);
                // ユーザにあいさつ
                sayHello();
            }
        } else {
            // 初期化に失敗
            Log.e(TAG, "Could not initialize TextToSpeech.");
        }
    }

    private static final Random RANDOM = new Random();
    private static final String[] HELLOS = {
      "hellow",
      "how are you",
      "good bye",
      "java is easy but groovy is difficult",
      "What's crack-a-lackin?",
      "That explains the stench!"
    };

    private void sayHello() {
        // Select a random hello.
        int helloLength = HELLOS.length;
        String hello = HELLOS[RANDOM.nextInt(helloLength)];
        mTts.speak(hello,
            TextToSpeech.QUEUE_FLUSH,  // プレイバックキューをフラッシュ(他に追加モードもある）
            null); //エンジンに渡すパラメタ。エンジン対応にキーパラで指定。（通常nullでよい？）
    }

}

