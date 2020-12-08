package com.bdpqchen.spans;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.MaskFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuggestionSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.bdpqchen.spans.customspan.CustomClickableSpan;

public final class Spans {

  public static Spany of(@NonNull String text) {
    return new Spany(text);
  }

  public static final class Spany {
    static final int FLAGS = SPAN_EXCLUSIVE_EXCLUSIVE;

    private final String mText;
    private List<Pair<Integer, Integer>> mRanges = new ArrayList<>();

    Spany(@NonNull String text) {
      this.mText = text;
      resetRange();
      ss = new SpannableString(text);
    }

    private void reset() {
      resetRange();
    }

    public Spany resetRange(int start, int end) {
      resetRange();
      range(start, end);
      return this;
    }

    private void resetRange() {
      mRanges.clear();
    }

    public Spany atStart(int position) {
      mRanges.add(Pair.create(0, position));
      return this;
    }

    public Spany atEnd(String spanTxt) {
      return atEnd(mText.lastIndexOf(spanTxt));
    }

    public Spany atEnd(int position) {
      mRanges.add(Pair.create(position, mText.length()));
      return this;
    }

    public Spany all() {
      mRanges.add(Pair.create(0, mText.length()));
      return this;
    }

    public Spany range(Pair<Integer, Integer> range) {
      mRanges.add(range);
      return this;
    }

    public Spany range(int start, int end) {
      mRanges.add(Pair.create(start, end));
      return this;
    }

    public Spany range(@NonNull String spanStr) {
      int idx = mText.indexOf(spanStr);
      if (idx == -1) {
        // TODO: 2020/12/3 throw an exception or not
        return this;
      }
      return range(idx, idx + +spanStr.length());
    }

    public Spany ranges(List<Pair<Integer, Integer>> ranges) {
      mRanges.addAll(ranges);
      return this;
    }

    public Spany ranges(int... pairs) {
      for (int i = 0; i < pairs.length - 1; i += 2) {
        mRanges.add(Pair.create(pairs[i], pairs[i + 1]));
      }
      return this;
    }

    public Spany between(@NonNull String before, @NonNull String after) {
      int startIndex = mText.indexOf(before) + before.length() + 1;
      int endIndex = mText.lastIndexOf(after) - 1;
      mRanges.add(Pair.create(startIndex, endIndex));
      return this;
    }

    public Spany foreground(@ColorInt int color) {
      setSpan(() -> new ForegroundColorSpan(color));
      return this;
    }

    public Spany background(@ColorInt int color) {
      setSpan(() -> new BackgroundColorSpan(color));
      return this;
    }

    public Spany underline() {
      setSpan(UnderlineSpan::new);
      return this;
    }

    public Spany strikeThrough() {
      setSpan(StrikethroughSpan::new);
      return this;
    }

    /**
     * NOTE: your textview need to be set
     * `setMovementMethod(LinkMovementMethod.getInstance());`
     */
    public Spany url(@NonNull String url) {
      setSpan(() -> new URLSpan(url));
      return this;
    }

    public Spany suggestion(Context context, String[] string) {
      setSpan(() -> new SuggestionSpan(context, string, 0));
      return this;
    }

    public Spany normal() {
      setStyleSpan(Typeface.NORMAL);
      return this;
    }

    public Spany bold() {
      setStyleSpan(Typeface.BOLD);
      return this;
    }

    public Spany italic() {
      setStyleSpan(Typeface.ITALIC);
      return this;
    }

    public Spany subscript() {
      setSpan(SubscriptSpan::new);
      return this;
    }

    public Spany superscript() {
      new SuperscriptSpan();
      setSpan(SuperscriptSpan::new);
      return this;
    }

    public Spany font(String fontFamily) {
      setSpan(() -> new TypefaceSpan(fontFamily));
      return this;
    }

    public Spany mask(MaskFilter maskFilter) {
      setSpan(() -> new MaskFilterSpan(maskFilter));
      return this;
    }

    /*public*/ Spany image(Drawable drawable) {
      // TODO: 2020/12/3 start, end, resource
      setSpan(new ImageSpan(drawable));
      return this;
    }

    public Spany click(@NonNull final View.OnClickListener onClickListener) {
      setClickable(onClickListener, false);
      return this;
    }

    public Spany clickNoUnderline(@NonNull final View.OnClickListener onClickListener) {
      setClickable(onClickListener, true);
      return this;
    }

    public void setClickable(View.OnClickListener onClickListener, boolean withoutUnderline) {
      setSpan(() -> new CustomClickableSpan(withoutUnderline) {
        @Override
        public void onClick(@NonNull View widget) {
          onClickListener.onClick(widget);
        }
      });
    }

    public void setClickable(View.OnClickListener onClickListener, boolean withoutUnderline,
        TextView textViewSetMovementMethod) {
      textViewSetMovementMethod.setMovementMethod(LinkMovementMethod.getInstance());
      setClickable(onClickListener, withoutUnderline);
    }

    public Spany size(int sizeInDp) {
      setSpan(() -> new AbsoluteSizeSpan(sizeInDp, true));
      return this;
    }

    public Spany size(float relativeSize) {
      for (Pair<Integer, Integer> r : mRanges) {
        setSpan(new RelativeSizeSpan(relativeSize), r.first, r.second);
      }
      return this;
    }

    public Spany appearance(Context context, int appearance) {
      setSpan(() -> new TextAppearanceSpan(context, appearance));
      return this;
    }

    void setStyleSpan(int style) {
      setSpan(() -> new StyleSpan(style));
    }

    public void setSpan(Generator generator) {
      for (Pair<Integer, Integer> range : mRanges) {
        setSpan(generator.create(), range.first, range.second);
      }
    }

    private interface Generator {
      Object create();
    }

    public void setSpan(Object what) {
      for (Pair<Integer, Integer> range : mRanges) {
        setSpan(what, range.first, range.second);
      }
    }

    void setSpan(Object what, int start, int end) {
      if (rangeAvailable()) {
        ss.setSpan(what, start, end, FLAGS);
      }
    }

    boolean rangeAvailable() {
      return mRanges.size() != 0;
    }

    private SpannableString ss;

    public SpannableString build() {
      return ss;
    }
  }
}
