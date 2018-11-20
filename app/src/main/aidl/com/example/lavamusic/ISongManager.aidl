// ISongManager.aidl
package com.example.lavamusic;
import com.example.lavamusic.Song;
// Declare any non-default types here with import statements

interface ISongManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int getPlayState();
    Song getCurrSong();
    void play(in Song song);
    void pause();
}
