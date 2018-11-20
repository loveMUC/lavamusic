package com.example.lavamusic;
/*
@author 石同尘 中央民族大学 计算机科学与技术
使用Intent Parcelable方式传递类实例对象
 */
import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {
    public long songId;
    public String songPath;
    public String songAlbum;
    public String songTitle;
    public String songSinger;
    public long songDuration;
    public long songSize;


    @Override
    public String toString() {
        return "Song{" +
                "songId=" + songId +
                ", songPath='" + songPath + '\'' +
                ", songAlbum='" + songAlbum + '\'' +
                ", songTitle=" + songTitle +
                ", songSinger=" + songSinger +
                ", songDuration='" + songDuration + '\'' +
                ", songSize='" + songSize + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.songId);
        dest.writeString(this.songPath);
        dest.writeString(this.songAlbum);
        dest.writeString(this.songTitle);
        dest.writeString(this.songSinger);
        dest.writeLong(this.songDuration);
        dest.writeLong(this.songSize);
    }
    public Song(){}

    protected Song(Parcel in){
        this.songId=in.readLong();
        this.songPath=in.readString();
        this.songAlbum=in.readString();
        this.songTitle=in.readString();
        this.songSinger=in.readString();
        this.songDuration=in.readLong();
        this.songSize=in.readLong();
    }

    public static final Creator<Song> CREATOR=new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel source) {
            return new Song(source);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}
