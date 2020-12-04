package com.bdpqchen.spans;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.MaskFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuggestionSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Pair;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

public final class Spans {

  public static Spany of(@NonNull String text) {
    return new Spany(text);
  }

  public static final class Spany {
    static final int FLAGS = SPAN_EXCLUSIVE_EXCLUSIVE;

    private final String mText;
    private List<Pair<Integer, Integer>> mRanges = new ArrayList<>();
    private SpannableStringBuilder ssb;

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

    /*public*/ Spany atStart() {
      mRanges.add(Pair.create(0, 0));
      return this;
    }

    /*public*/ Spany atEnd() {
      // todo range set
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

    public Spany between(@NonNull String before, @NonNull String after) {
      int startIndex = mText.indexOf(before) + before.length() + 1;
      int endIndex = mText.lastIndexOf(after) - 1;
      mRanges.add(Pair.create(startIndex, endIndex));
      return this;
    }

    public Spany foreground(@ColorInt int color) {
      setSpan(new ForegroundColorSpan(color));
      return this;
    }

    public Spany background(@ColorInt int color) {
      setSpan(new BackgroundColorSpan(color));
      return this;
    }

    public Spany underline() {
      setSpan(new UnderlineSpan());
      return this;
    }

    public Spany url(@NonNull String url) {
      setSpan(new URLSpan(url));
      return this;
    }

    public Spany suggestion(Context context, String[] string) {
      setSpan(new SuggestionSpan(context, string, 0));
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
      setSpan(new SubscriptSpan());
      return this;
    }

    public Spany superscript() {
      setSpan(new SuperscriptSpan());
      return this;
    }

    public Spany font(String fontFamily) {
      setSpan(new TypefaceSpan(fontFamily));
      return this;
    }

    public Spany mask(MaskFilter maskFilter) {
      setSpan(new MaskFilterSpan(maskFilter));
      return this;
    }

    /*public */Spany image(Drawable drawable) {
      // TODO: 2020/12/3 start, end, resource
      setSpan(new ImageSpan(drawable));
      return this;
    }

    public Spany click(final View.OnClickListener onClickListener) {
      setSpan(new ClickableSpan() {
        @Override
        public void onClick(@NonNull View widget) {
          onClickListener.onClick(widget);
        }
      });
      return this;
    }

    public Spany size(int sizeInDp) {
      setSpan(new AbsoluteSizeSpan(sizeInDp, true));
      return this;
    }

    public Spany size(float relativeSize) {
      setSpan(new RelativeSizeSpan(relativeSize));
      return this;
    }

    void setStyleSpan(int style) {
      setSpan(new StyleSpan(style));
    }

//  public void setSpan(Object whatSpan) { }

    void setSpan(Object what) {
      for (Pair<Integer, Integer> range : mRanges) {
        setSpan(what, range.first, range.second, FLAGS);
      }
    }

    void setSpan(Object what, int start, int end, int flags) {
      if (rangeAvailable()) {
        ss.setSpan(what, start, end, flags);
      }
    }

    boolean rangeAvailable() {
      return mRanges.size() != 0;
    }

    private SpannableString ss;

    public SpannableString build() {
      return ss;
    }

    /*public*/ SpannableStringBuilder builder() {
      return ssb;
    }

  }

}
