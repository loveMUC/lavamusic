package com.example.lavamusic;
/*
author 石同尘 中央民族大学 计算机科学与技术
 */
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SongListActivity extends BaseActivity implements OnNextSongListener {
        @BindView(R.id.mini_bar)
        RelativeLayout mMiniBar;
        @BindView(R.id.recycler_view)
        RecyclerView mShowSong;
        @BindView(R.id.bar_song_album)
        ImageView mSongAlbum;
        @BindView(R.id.bar_song_title)
        TextView mSongTitle;
        @BindView(R.id.bar_song_singer)
        TextView mSongSinger;
        @BindView(R.id.next)
        ImageView mNext;
        @BindView(R.id.play)
        ImageView mPlay;
        @BindView(R.id.pre)
        ImageView mPre;
        private List<Song> mSongList=new ArrayList<>();
        private static final int SONG_LOADER_ID=1;
        private SongAdapter mAdapter;
        private ISongManager mSongManager;
        private int mCurrIndex;
        private ServiceConnection mServiceConnection=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mSongManager=ISongManager.Stub.asInterface(service);
                showOrNotMiniBar();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mSongManager=null;
            }
        };
        private Song mCurrSong;
        private SongCompletionReceiver mReceiver;

    @Override
    protected int getLayoutResId() {
        return R.layout.song_content;
    }

    @Override
    protected void initData() {
        super.initData();
        LoaderCallback loaderCallback=new LoaderCallback();
        getLoaderManager().initLoader(SONG_LOADER_ID,null,loaderCallback);
        startService(new Intent(this,SongPlayService.class));
        bindService(new Intent(this,SongPlayService.class),mServiceConnection,BIND_AUTO_CREATE);
        mReceiver=new SongCompletionReceiver();
        mReceiver.setNextSongListener(this);
        IntentFilter filter=new IntentFilter();
        filter.addAction(SongCompletionReceiver.SONG_COMPLETION);
        registerReceiver(mReceiver,filter);
    }

    @Override
    protected void initView() {
        super.initView();
        mShowSong.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new SongAdapter(mSongList, new OnSongItemClickListener() {
            @Override
            public void play(Song song, int position) throws IOException, RemoteException {
                clickSongItem(mCurrSong,mCurrIndex);
            }
        });//!!!!!!!!!!!!!!!!!!!注意！
        mShowSong.setAdapter(mAdapter);
        hideMiniBar();
    }

    private void showOrNotMiniBar(){
            try{
                mCurrSong=mSongManager.getCurrSong();
                if(mCurrSong==null)
                    hideMiniBar();
                else
                    changeMiniBar(mCurrSong);
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }

    private void clickSongItem(Song song,int position)throws RemoteException{
        mCurrSong=song;
        mCurrIndex=position;
        play();
        changeMiniBar(song);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        unregisterReceiver(mReceiver);
    }
    private void changeMiniBar(Song song)throws RemoteException{
        showMiniBar();
        changeDisplaySongInfo(song);
        changePlayState();
    }
    private void changeDisplaySongInfo(Song song){
        mSongSinger.setText(song.songSinger);
        mSongTitle.setText(song.songTitle);
    }
    private void changePlayState()throws RemoteException{
        switch (mSongManager.getPlayState()){
            case PlayState.STARTED:
                mPlay.setImageResource(R.drawable.playing_song);
                break;
            case PlayState.PAUSED:
                mPlay.setImageResource(R.drawable.play_song);
                break;
        }
    }
    private void showMiniBar(){
        mMiniBar.setVisibility(View.VISIBLE);
    }
    private void hideMiniBar(){
        mMiniBar.setVisibility(View.GONE);
    }
    @OnClick({R.id.next,R.id.play,R.id.pre})
    public void onViewClicked(View view){
        try{
            switch (view.getId()){
                case R.id.next:
                    nextSong();
                    break;
                case R.id.play:
                    playOrPause();
                    changePlayState();
                    break;
                case R.id.pre:
                    preSong();
                    break;
            }
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }
    private void preSong()throws RemoteException{
        mCurrSong=getPreSong();
        changeDisplaySongInfo(mCurrSong);
        if(!(mSongManager.getPlayState()==PlayState.PAUSED)){
            play();
        }
    }
    private Song getPreSong(){
        Song song;
        if(--mCurrIndex<0){
            mCurrIndex=mSongList.size()-1;
            song=mSongList.get(mCurrIndex);
        }else{
            song=mSongList.get(mCurrIndex);
        }
        return song;
    }
    private void playOrPause()throws RemoteException{
        switch (mSongManager.getPlayState()){
            case PlayState.STARTED:
                mSongManager.pause();
                break;
            case PlayState.PAUSED:
                play();
                break;
        }
    }
    private void play()throws RemoteException{
        mSongManager.play(mCurrSong);
    }
    private void nextSong()throws RemoteException{
        mCurrSong=getNextSong();
        changeDisplaySongInfo(mCurrSong);
        if(!(mSongManager.getPlayState()==PlayState.PAUSED))
            play();
    }
    private Song getNextSong(){
        Song song;
        if(++mCurrIndex>(mSongList.size()-1)){
            mCurrIndex=0;
            song=mSongList.get(mCurrIndex);
        }else{
            song=mSongList.get(mCurrIndex);
        }
        return song;
    }

    @Override
    public Song playNextSong() {
        try{
            nextSong();
        }catch (RemoteException e){
            e.printStackTrace();
        }
        return null;
    }
    class LoaderCallback implements android.app.LoaderManager.LoaderCallbacks<Cursor>{
        private final String[] SONG_PREJECTION=new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.IS_MUSIC
        };

        @Override
        public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if(id==SONG_LOADER_ID){
                return new android.content.CursorLoader(SongListActivity.this,
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        SONG_PREJECTION,
                        null,null,SONG_PREJECTION[3]+" ASC");
            }
            return null;
        }

        @Override
        public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
            if(data!=null&&data.getCount()>0){
                while(data.moveToNext()){
                    long id=data.getLong(0);
                    String title=data.getString(1);
                    String artist=data.getString(2);
                    long duration=data.getLong(3);
                    long size=data.getLong(4);
                    String url=data.getString(5);
                    String album=data.getString(6);
                    int isSong=data.getInt(7);
                    if(isSong!=0&&duration/(1000*60)>=1){
                        Song song=new Song();
                        song.songId=id;
                        song.songTitle=title;
                        song.songSinger=artist;
                        song.songDuration=duration;
                        song.songSize=size;
                        song.songPath=url;
                        song.songAlbum=album;
                        mSongList.add(song);
                    }
                }
                mAdapter.setSongList(mSongList);
            }
        }

        @Override
        public void onLoaderReset(android.content.Loader<Cursor> loader) {

        }
    }
}
