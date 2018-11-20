package com.example.lavamusic;
/*
@author 石同尘 中央民族大学 计算机科学与技术
 */
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;

import java.io.IOException;

public class SongPlayService extends Service implements MediaPlayer.OnErrorListener {
    private MediaPlayer mMediaPlayer;
    private Song mCurrSong;
    private int mPlayState=PlayState.IDLE;
    private ISongManager.Stub mBinder=new ISongManager.Stub() {
        @Override
        public int getPlayState() throws RemoteException {
            return mPlayState;
        }

        @Override
        public Song getCurrSong() throws RemoteException {
            return mCurrSong;
        }

        @Override
        public void play(Song song) throws RemoteException {
            if(mPlayState==PlayState.PAUSED&&song.songPath.equals(mCurrSong.songPath)){
                mMediaPlayer.start();
                mPlayState=PlayState.STARTED;
                return;
            }else if(mPlayState==PlayState.STARTED&&song.songPath.equals(mCurrSong.songPath)){
                return;
            }
            SongPlayService.this.play(song);
        }

        @Override
        public void pause() throws RemoteException {
            SongPlayService.this.pause();
        }
    };
    @Override
    public void onCreate() {
        mMediaPlayer=new MediaPlayer();
        initMediaPlayerListener();
    }

    public void initMediaPlayerListener(){
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayState=PlayState.PLAY_BACK_COMPLETED;
                reset();
                sendBroadcast(new Intent(SongCompletionReceiver.SONG_COMPLETION));
            }
        });
        mMediaPlayer.setOnErrorListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mPlayState=PlayState.ERROR;
        reset();
        return false;
    }

    private void prePlayer()throws IOException {
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setDataSource(getApplicationContext(), Uri.parse(mCurrSong.songPath));
        mMediaPlayer.prepare();
        mMediaPlayer.start();
        mPlayState=PlayState.STARTED;
    }

    private void pause(){
        mMediaPlayer.pause();
        mPlayState=PlayState.PAUSED;
    }

    private void stop(){
        mMediaPlayer.stop();
        mPlayState=PlayState.STOPPED;
    }

    private void reset(){
        mMediaPlayer.reset();
        mPlayState=PlayState.IDLE;
    }

    private void play(Song song){
        try {
            mCurrSong=song;
            reset();
            prePlayer();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mMediaPlayer!=null){
            mMediaPlayer.reset();
            mMediaPlayer=null;
            mPlayState=PlayState.END;
        }
    }
}
