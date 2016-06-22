package com.aphoh.muser.music;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;
import com.aphoh.muser.R;
import com.aphoh.muser.ui.activitiy.MainActivity;
import com.aphoh.muser.util.LogUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Keeps track of a notification and updates it automatically for a given MediaSession. Maintaining
 * a visible notification (usually) guarantees that the music service won't be killed during
 * playback.
 */
public class MediaNotificationManager extends BroadcastReceiver {
  public static final String ACTION_PAUSE = "com.aphoh.muser.pause";
  public static final String ACTION_PLAY = "com.aphoh.muser.play";
  public static final String ACTION_PREV = "com.aphoh.muser.prev";
  public static final String ACTION_NEXT = "com.aphoh.muser.next";
  private static final int NOTIFICATION_ID = "com.aphoh.muser".hashCode();
  private static final int REQUEST_CODE = 100;
  private final LogUtil log = new LogUtil(MediaNotificationManager.class.getSimpleName());
  private final MusicService mService;
  private final NotificationManager mNotificationManager;
  private final PendingIntent mPauseIntent;
  private final PendingIntent mPlayIntent;
  private final PendingIntent mPreviousIntent;
  private final PendingIntent mNextIntent;
  private final int mNotificationColor;
  private MediaSessionCompat.Token mSessionToken;
  private MediaControllerCompat mController;
  private MediaControllerCompat.TransportControls mTransportControls;
  private PlaybackStateCompat mPlaybackState;
  private MediaMetadataCompat mMetadata;
  private boolean mStarted = false;
  private final MediaControllerCompat.Callback mCb = new MediaControllerCompat.Callback() {
    @Override
    public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
      mPlaybackState = state;
      log.d("Received new playback state" + state);
      if (state.getState() == PlaybackStateCompat.STATE_STOPPED
          || state.getState() == PlaybackStateCompat.STATE_NONE) {
        stopNotification();
      } else {
        Notification notification = createNotification();
        if (notification != null) {
          mNotificationManager.notify(NOTIFICATION_ID, notification);
          if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
            mService.startForeground(NOTIFICATION_ID, notification);
          } else {
            mService.stopForeground(false);
          }
        }
      }
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
      mMetadata = metadata;
      log.d("Received new metadata " + metadata);
      Notification notification = createNotification();
      if (notification != null) {
        mNotificationManager.notify(NOTIFICATION_ID, notification);
      }
    }

    @Override
    public void onSessionDestroyed() {
      super.onSessionDestroyed();
      log.d("Session was destroyed, resetting to the new session token");
      updateSessionToken();
    }
  };

  public MediaNotificationManager(MusicService service) {
    mService = service;
    updateSessionToken();

    mNotificationColor = ContextCompat.getColor(service, R.color.primary_dark);

    mNotificationManager =
        (NotificationManager) mService.getSystemService(Context.NOTIFICATION_SERVICE);

    String pkg = mService.getPackageName();
    mPauseIntent =
        PendingIntent.getBroadcast(mService, REQUEST_CODE, new Intent(ACTION_PAUSE).setPackage(pkg),
            PendingIntent.FLAG_CANCEL_CURRENT);
    mPlayIntent =
        PendingIntent.getBroadcast(mService, REQUEST_CODE, new Intent(ACTION_PLAY).setPackage(pkg),
            PendingIntent.FLAG_CANCEL_CURRENT);
    mPreviousIntent =
        PendingIntent.getBroadcast(mService, REQUEST_CODE, new Intent(ACTION_PREV).setPackage(pkg),
            PendingIntent.FLAG_CANCEL_CURRENT);
    mNextIntent =
        PendingIntent.getBroadcast(mService, REQUEST_CODE, new Intent(ACTION_NEXT).setPackage(pkg),
            PendingIntent.FLAG_CANCEL_CURRENT);



    // Cancel all notifications to handle the case where the Service was killed and
    // restarted by the system.
    mNotificationManager.cancelAll();
  }

  /**
   * Posts the notification and starts tracking the session to keep it updated. The notification
   * will automatically be removed if the session is destroyed before {@link #stopNotification} is
   * called.
   */
  public void startNotification() {
    if (!mStarted) {
      mMetadata = mController.getMetadata();
      mPlaybackState = mController.getPlaybackState();

      // The notification must be updated after setting started to true
      Notification notification = createNotification();
      if (notification != null) {
        mController.registerCallback(mCb);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NEXT);
        filter.addAction(ACTION_PAUSE);
        filter.addAction(ACTION_PLAY);
        filter.addAction(ACTION_PREV);
        mService.registerReceiver(this, filter);

        mService.startForeground(NOTIFICATION_ID, notification);
        mStarted = true;
      }
    }
  }

  /**
   * Removes the notification and stops tracking the session. If the session was destroyed this has
   * no effect.
   */
  public void stopNotification() {
    if (mStarted) {
      mStarted = false;
      mController.unregisterCallback(mCb);
      try {
        mNotificationManager.cancel(NOTIFICATION_ID);
        mService.unregisterReceiver(this);
      } catch (IllegalArgumentException ex) {
        // ignore if the receiver is not registered.
      }
      mService.stopForeground(true);
    }
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    final String action = intent.getAction();
    log.d("Received intent with action " + action);
    switch (action) {
      case ACTION_PAUSE:
        mTransportControls.pause();
        mService.pause(); //TODO: For whatever reason, it won't pause using the transport controls.
        break;
      case ACTION_PLAY:
        mTransportControls.play();
        break;
      case ACTION_NEXT:
        mTransportControls.skipToNext();
        break;
      case ACTION_PREV:
        mTransportControls.skipToPrevious();
        break;
      default:
        log.e("Unknown intent ignored. Action=" + action);
    }
  }

  /**
   * Update the state based on a change on the session token. Called either when we are running for
   * the first time or when the media session owner has destroyed the session (see {@link
   * android.media.session.MediaController.Callback#onSessionDestroyed()})
   */
  private void updateSessionToken() {
    MediaSessionCompat.Token freshToken = mService.getCompatSessionToken();
    if (mSessionToken == null || !mSessionToken.equals(freshToken)) {
      if (mController != null) {
        mController.unregisterCallback(mCb);
      }
      mSessionToken = freshToken;
      try {
        mController = new MediaControllerCompat(mService, mSessionToken);
      } catch (RemoteException e) {
        log.e("FAILED TO CREATE MEDIACONTROLLER: ", e);
      }
      mTransportControls = mController.getTransportControls();
      if (mStarted) {
        mController.registerCallback(mCb);
      }
    }
  }

  private PendingIntent createContentIntent() {
    Intent openUI = new Intent(mService, MainActivity.class);
    openUI.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    return PendingIntent.getActivity(mService, REQUEST_CODE, openUI,
        PendingIntent.FLAG_CANCEL_CURRENT);
  }

  private Notification createNotification() {
    log.d("updateNotificationMetadata. mMetadata=" + mMetadata);
    if (mMetadata == null || mPlaybackState == null) {
      return null;
    }

    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mService);
    int playPauseButtonPosition = 0;

    // If skip to previous action is enabled
    if ((mPlaybackState.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS) != 0) {
      notificationBuilder.addAction(R.drawable.ic_fast_rewind,
          mService.getString(R.string.previous), mPreviousIntent);

      // If there is a "skip to previous" button, the play/pause button will
      // be the second one. We need to keep track of it, because the MediaStyle notification
      // requires to specify the index of the buttons (actions) that should be visible
      // when in compact view.
      playPauseButtonPosition = 1;
    }

    addPlayPauseAction(notificationBuilder);

    // If skip to next action is enabled
    if ((mPlaybackState.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_NEXT) != 0) {
      notificationBuilder.addAction(R.drawable.ic_fast_forward, mService.getString(R.string.next),
          mNextIntent);
    }

    MediaDescriptionCompat description = mMetadata.getDescription();

    notificationBuilder.setStyle(new NotificationCompat.MediaStyle().setShowActionsInCompactView(
        new int[]{playPauseButtonPosition})  // show only play/pause in compact view
        .setMediaSession(mSessionToken))
        .setColor(mNotificationColor)
        .setSmallIcon(R.drawable.play)
        .setVisibility(Notification.VISIBILITY_PUBLIC)
        .setUsesChronometer(true)
        .setContentIntent(createContentIntent())
        .setContentTitle(description.getTitle())
        .setContentText(description.getSubtitle());

    setNotificationPlaybackState(notificationBuilder);

    if (description.getIconUri() != null) {
      // This sample assumes the iconUri will be a valid URL formatted String, but
      // it can actually be any valid Android Uri formatted String.
      // async fetch the album art icon
      String artUrl = description.getIconUri().toString();
      fetchBitmapFromURLAsync(artUrl, notificationBuilder);
    }

    return notificationBuilder.build();
  }

  private void addPlayPauseAction(NotificationCompat.Builder builder) {
    log.d("updatePlayPauseAction");
    String label;
    int icon;
    PendingIntent intent;
    if (mPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
      label = mService.getString(R.string.pause);
      icon = R.drawable.pause;
      intent = mPauseIntent;
    } else {
      label = mService.getString(R.string.play);
      icon = R.drawable.play;
      intent = mPlayIntent;
    }
    builder.addAction(new NotificationCompat.Action(icon, label, intent));
  }

  private void setNotificationPlaybackState(NotificationCompat.Builder builder) {
    log.d("updateNotificationPlaybackState. mPlaybackState=" + mPlaybackState);
    if (mPlaybackState == null || !mStarted) {
      log.d("updateNotificationPlaybackState. cancelling notification!");
      mService.stopForeground(true);
      return;
    }
    if (mPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING
        && mPlaybackState.getPosition() >= 0) {
      log.d("updateNotificationPlaybackState. updating playback position to " +
          (System.currentTimeMillis() - mPlaybackState.getPosition()) / 1000 + " seconds");
      builder.setWhen(System.currentTimeMillis() - mPlaybackState.getPosition())
          .setShowWhen(true)
          .setUsesChronometer(true);
    } else {
      log.d("updateNotificationPlaybackState. hiding playback position");
      builder.setWhen(0).setShowWhen(false).setUsesChronometer(false);
    }

    // Make sure that the notification can be dismissed by the user when we are not playing:
    builder.setOngoing(mPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING);
  }

  private void fetchBitmapFromURLAsync(final String bitmapUrl,
                                       final NotificationCompat.Builder builder) {
    Picasso.with(mService).load(bitmapUrl).into(new Target() {
      @Override
      public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        if (mMetadata != null) {
          builder.setLargeIcon(bitmap);
          mNotificationManager.notify(NOTIFICATION_ID, builder.build());
        }
      }

      @Override
      public void onBitmapFailed(Drawable errorDrawable) {

      }

      @Override
      public void onPrepareLoad(Drawable placeHolderDrawable) {

      }
    });
  }
}
