package com.jakewharton.rxbinding.widget;

import android.annotation.TargetApi;
import android.widget.Toolbar;
import android.view.View;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;

import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

@TargetApi(LOLLIPOP)
final class ToolbarNavigationClickOnSubscribe implements Observable.OnSubscribe<Void> {
  private final Toolbar view;

  public ToolbarNavigationClickOnSubscribe(Toolbar view) {
    this.view = view;
  }

  @Override public void call(final Subscriber<? super Void> subscriber) {
    checkUiThread();

    View.OnClickListener listener = new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(null);
        }
      }
    };
    view.setNavigationOnClickListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setNavigationOnClickListener(null);
      }
    });
  }
}
