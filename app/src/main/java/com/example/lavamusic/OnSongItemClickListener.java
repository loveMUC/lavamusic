package com.example.lavamusic;

import android.os.RemoteException;

import java.io.IOException;

/*
@author 石同尘 中央民族大学 计算机科学与技术
 */
public interface OnSongItemClickListener {
    void play(Song song,int position)throws IOException,RemoteException;
}
