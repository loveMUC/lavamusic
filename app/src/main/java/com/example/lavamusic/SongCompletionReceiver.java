package com.example.lavamusic;
/*
@author 石同尘 中央民族大学 计算机科学与技术
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SongCompletionReceiver extends BroadcastReceiver {
    public static final String SONG_COMPLETION="com.example.lavamusic.song_completion";
    private OnNextSongListener mNextSongListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if(mNextSongListener!=null)
            mNextSongListener.playNextSong();
    }

    public void setNextSongListener(OnNextSongListener nextSongListener){
        mNextSongListener=nextSongListener;
    }
}
