package com.jakewharton.rxbinding.view;

import android.view.View;
import com.jakewharton.rxbinding.internal.MainThreadSubscription;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class ViewLongClickOnSubscribe implements Observable.OnSubscribe<Void> {
  private final View view;
  private final Func0<Boolean> handled;

  ViewLongClickOnSubscribe(View view, Func0<Boolean> handled) {
    this.view = view;
    this.handled = handled;
  }

  @Override public void call(final Subscriber<? super Void> subscriber) {
    checkUiThread();

    View.OnLongClickListener listener = new View.OnLongClickListener() {
      @Override public boolean onLongClick(View v) {
        if (handled.call()) {
          if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(null);
          }
          return true;
        }
        return false;
      }
    };
    view.setOnLongClickListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        view.setOnLongClickListener(null);
      }
    });
  }
}
