package variable.com.eclectic.Chats;

import androidx.appcompat.app.AppCompatActivity;
import variable.com.eclectic.R;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Random;

public class Chat_WriteStatus extends AppCompatActivity implements View.OnClickListener {

    private EditText edt_write_status;
    private ImageView write_status_font;
    private ImageView write_status_bg_color;
    private FloatingActionButton write_status_add;
    private RelativeLayout write_status_RL;
    static int font_click = 1;
    Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_write_status);

        init();
        write_status_bg_color.setOnClickListener(this);
        write_status_font.setOnClickListener(this);

    }

    private void init() {

        write_status_RL = (RelativeLayout)findViewById(R.id.write_status_RL);
        edt_write_status = (EditText)findViewById(R.id.edt_write_status);
        write_status_font = (ImageView)findViewById(R.id.write_status_font);
        write_status_bg_color = (ImageView)findViewById(R.id.write_status_bg_color);
        write_status_add = (FloatingActionButton)findViewById(R.id.write_status_add);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.write_status_bg_color:
            {
                Random random = new Random();
                int color = Color.argb(255,random.nextInt(256),random.nextInt(256),random.nextInt(256));
                write_status_RL.setBackgroundColor(color);
                edt_write_status.setBackgroundColor(color);

                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
            }
            break;

            case R.id.write_status_font:
            {
                switch (font_click)
                {
                    case 1:
                    {
                        typeface = Typeface.createFromAsset(getAssets(),"fonts/DancingScript-Regular.otf");
                        edt_write_status.setTypeface(typeface);
                        font_click = font_click + 1;
                    }
                    break;

                    case 2:
                    {
                        typeface = Typeface.createFromAsset(getAssets(),"fonts/gobold-blocky.regular.otf");
                        edt_write_status.setTypeface(typeface);
                        font_click = font_click + 1;
                    }
                    break;

                    case 3:
                    {
                        typeface = Typeface.createFromAsset(getAssets(),"fonts/Simply Font.ttf");
                        edt_write_status.setTypeface(typeface);
                        font_click = font_click + 1;
                    }
                    break;

                    case 4:
                    {
                        typeface = Typeface.createFromAsset(getAssets(),"fonts/OpenSans-Regular.ttf");
                        edt_write_status.setTypeface(typeface);
                        font_click = font_click + 1;
                    }
                    break;

                    case 5:
                    {
                        typeface = Typeface.createFromAsset(getAssets(),"fonts/naughty_scratch_free.ttf");
                        edt_write_status.setTypeface(typeface);
                        font_click = font_click + 1;
                    }
                    break;

                    case 6:
                    {
                        typeface = Typeface.createFromAsset(getAssets(),"fonts/silent_broadcast.ttf");
                        edt_write_status.setTypeface(typeface);
                        font_click = font_click + 1;
                    }
                    break;

                    case 7:
                    {
                        typeface = Typeface.createFromAsset(getAssets(),"fonts/Got_Heroin.ttf");
                        edt_write_status.setTypeface(typeface);
                        font_click = font_click + 1;
                    }
                    break;

                    case 8:
                    {
                        typeface = Typeface.createFromAsset(getAssets(),"fonts/Ghost Of The Wild West.ttf");
                        edt_write_status.setTypeface(typeface);
                        font_click = 1;
                    }
                    break;

                }
            }
        }
    }
}
