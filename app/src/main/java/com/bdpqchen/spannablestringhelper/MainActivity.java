package com.bdpqchen.spannablestringhelper;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bdpqchen.spans.Spans;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    TextView tv = findViewById(R.id.tv);
    tv.setText(Spans.of("Hello World!")
        .range("llo")
        .foreground(ContextCompat.getColor(this, R.color.colorAccent))
        .range(1, 2)
        .foreground(ContextCompat.getColor(this, R.color.colorAccent))
        .bold()
        .build());
  }
}