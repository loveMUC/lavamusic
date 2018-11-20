package com.example.lavamusic;
/*
@author 石同尘 中央民族大学 计算机科学与技术
MediaPlayer的播放状态
 */
public interface PlayState {
    int IDLE=0;
    int PREPARED=1;
    int STARTED=2;
    int PAUSED=3;
    int STOPPED=4;
    int PLAY_BACK_COMPLETED=5;
    int ERROR=6;
    int END=7;
}
