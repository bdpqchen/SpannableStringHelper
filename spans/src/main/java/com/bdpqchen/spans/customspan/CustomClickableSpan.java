package com.bdpqchen.spans.customspan;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import androidx.annotation.NonNull;

public abstract class CustomClickableSpan extends ClickableSpan {

  private final boolean mUnderline;

  public CustomClickableSpan() {
    this.mUnderline = true;
  }

  public CustomClickableSpan(boolean withoutUnderline) {
    this.mUnderline = !withoutUnderline;
  }

  @Override
  public void updateDrawState(@NonNull TextPaint ds) {
    super.updateDrawState(ds);
    ds.setUnderlineText(mUnderline);
  }
}
