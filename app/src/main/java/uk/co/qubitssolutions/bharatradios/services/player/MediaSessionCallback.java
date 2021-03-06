package uk.co.qubitssolutions.bharatradios.services.player;

import android.os.SystemClock;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import uk.co.qubitssolutions.bharatradios.app.services.BackgroundAudioPlayerService;
import uk.co.qubitssolutions.bharatradios.model.Constants;

public class MediaSessionCallback extends MediaSessionCompat.Callback {
    @Override
    public void onPlay() {
        super.onPlay();


        if (BackgroundAudioPlayerService.mediaSession != null) {
            PlaybackStateCompat state = new PlaybackStateCompat.Builder()
                    .setActions(
                            PlaybackStateCompat.ACTION_PLAY |
                                    PlaybackStateCompat.ACTION_PAUSE |
                                    PlaybackStateCompat.ACTION_STOP)
                    .setState(PlaybackStateCompat.STATE_PLAYING, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, SystemClock.elapsedRealtime())
                    .build();

            BackgroundAudioPlayerService.mediaSession.setPlaybackState(state);
        }

        Log.i(Constants.LOG_TAG, "Media Session Callback - Play");
    }

    @Override
    public void onPause() {
        super.onPause();

        if (BackgroundAudioPlayerService.mediaSession != null) {
            PlaybackStateCompat state = new PlaybackStateCompat.Builder()
                    .setActions(
                            PlaybackStateCompat.ACTION_PLAY |
                                    PlaybackStateCompat.ACTION_PAUSE |
                                    PlaybackStateCompat.ACTION_STOP)
                    .setState(PlaybackStateCompat.STATE_PAUSED, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, SystemClock.elapsedRealtime())
                    .build();

            BackgroundAudioPlayerService.mediaSession.setPlaybackState(state);
        }


        Log.i(Constants.LOG_TAG, "Media Session Callback - Pause");
    }


    @Override
    public void onStop() {
        super.onStop();

        if (BackgroundAudioPlayerService.mediaSession != null) {
            PlaybackStateCompat state = new PlaybackStateCompat.Builder()
                    .setActions(
                            PlaybackStateCompat.ACTION_PLAY |
                                    PlaybackStateCompat.ACTION_PAUSE |
                                    PlaybackStateCompat.ACTION_STOP)
                    .setState(PlaybackStateCompat.STATE_STOPPED, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN, SystemClock.elapsedRealtime())
                    .build();

            BackgroundAudioPlayerService.mediaSession.setPlaybackState(state);
        }

        Log.i(Constants.LOG_TAG, "Media Session Callback - Stop");
    }
}
