// Generated by view binder compiler. Do not edit!
package com.example.glbd22021.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.glbd22021.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemContainerSentMessageBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final TextView textDataTime;

  @NonNull
  public final TextView textMessage;

  private ItemContainerSentMessageBinding(@NonNull RelativeLayout rootView,
      @NonNull TextView textDataTime, @NonNull TextView textMessage) {
    this.rootView = rootView;
    this.textDataTime = textDataTime;
    this.textMessage = textMessage;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemContainerSentMessageBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemContainerSentMessageBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_container_sent_message, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemContainerSentMessageBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.textDataTime;
      TextView textDataTime = ViewBindings.findChildViewById(rootView, id);
      if (textDataTime == null) {
        break missingId;
      }

      id = R.id.textMessage;
      TextView textMessage = ViewBindings.findChildViewById(rootView, id);
      if (textMessage == null) {
        break missingId;
      }

      return new ItemContainerSentMessageBinding((RelativeLayout) rootView, textDataTime,
          textMessage);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
