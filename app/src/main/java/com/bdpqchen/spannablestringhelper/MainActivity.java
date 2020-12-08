package com.bdpqchen.spannablestringhelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.graphics.BlurMaskFilter;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bdpqchen.spans.Spans;

public class MainActivity extends AppCompatActivity {

  private List<SpannableString> items = new ArrayList<>();

  private int color1, color2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    color1 = ContextCompat.getColor(this, R.color.colorAccent);
    color2 = ContextCompat.getColor(this, R.color.colorPrimaryDark);
    setContentView(R.layout.activity_main);
    RecyclerView rv = findViewById(R.id.rv);
    RecyclerAdapter adapter = new RecyclerAdapter();
    items.add(Spans.of("AbsoluteSizeSpan 18.00dp")
        .range("18")
        .size(17)
        .build());
    items.add(Spans.of("RelativeSizeSpan more than 1.5 times")
        .range("1.5")
        .size(1.5f)
        .build());
    items.add(Spans.of("StyleSpan with Bold, Italic and Normal")
        .range("Bold").bold()
        .range("Italic").italic()
        .range("Normal").normal()
        .build());
    items.add(Spans.of("ForegroundSpan")
        .range(0, 4)
        .foreground(color1)
        .build());
    items.add(Spans.of("BackgroundSpan").range(0, 4)
        .background(color1)
        .build());
    items.add(Spans.of("UnderlineSpan").all().underline().build());
    items.add(Spans.of("StrikethroughSpan").all().strikeThrough().build());
    items.add(Spans.of("URLSpan: jump to android.com")
        .atEnd(9)
        .appearance(this, R.style.URLTextAppearance) /* change the clickable text color */
        .url("http://www.android.com")
        .build());
    items.add(Spans.of("ClickableSpan: FINISH this Activity")
        .range("FINISH")
        .click(v -> finish())
        .build());
    items.add(Spans.of("☕- C8H10N4O2\n")
        .ranges(Arrays
            .asList(Pair.create(4, 5), Pair.create(6, 8), Pair.create(9, 10), Pair.create(11, 12)))
        .size(0.7f)
        .subscript()
        .build());
    items.add(Spans.of("1st example")
        .range(1, 3)
        .size(0.8f)
        .superscript()
        .build());
    items.add(Spans.of("TypefaceSpan, TypefaceSpan")
        .range("TypefaceSpan")
        .font("serif")
        .build());
    items.add(Spans.of("MaskFilterSpan BlurMaskFilter")
        .atEnd("BlurMaskFilter")
        .mask(new BlurMaskFilter(2, BlurMaskFilter.Blur.NORMAL))
        .build());
    items.add(Spans.of("TextAppearance")
        .all()
        .appearance(this, R.style.MyTextAppearance)
        .build());
    rv.setAdapter(adapter);
    rv.setLayoutManager(new LinearLayoutManager(this));
    rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
  }

  private class RecyclerAdapter extends RecyclerView.Adapter<VH> {

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = getLayoutInflater().inflate(R.layout.view_item, parent, false);
      return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
      SpannableString ss = items.get(position);
      holder.tv.setText(ss, TextView.BufferType.SPANNABLE);
      holder.tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public int getItemCount() {
      return items.size();
    }
  }

  private static class VH extends RecyclerView.ViewHolder {
    TextView tv;

    public VH(@NonNull View itemView) {
      super(itemView);
      tv = itemView.findViewById(R.id.tv);
      tv.setSpannableFactory(mSpannableFactory);
    }
  }

  private static final Spannable.Factory mSpannableFactory = new Spannable.Factory() {
    @Override
    public Spannable newSpannable(CharSequence source) {
      return (Spannable) source;
    }
  };
}