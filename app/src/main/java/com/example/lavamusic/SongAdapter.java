package com.example.lavamusic;
/*
@author 石同尘 中央民族大学 计算机科学与技术
适配器设置
 */
import android.content.Context;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {
        private List<Song> mSongList;
        private Context mContext;
        private OnSongItemClickListener mItemClickListener;

        public SongAdapter(List<Song> songList,OnSongItemClickListener itemClickListener){
            mSongList=songList;
            mItemClickListener=itemClickListener;
        }

        public void setSongList(List<Song> songList){
            notifyDataSetChanged();
        }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.song,viewGroup,false);
            mContext=viewGroup.getContext();
            return new SongHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder songHolder, int i) {
        songHolder.setData(i);
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }
    class SongHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.song_album)
            ImageView songAlbum;
            @BindView(R.id.song_title)
            TextView songTitle;
            @BindView(R.id.song_singer)
            TextView songSinger;
            @BindView(R.id.song_duration)
            TextView songDuration;

            public SongHolder(View itemView){
                super(itemView);
                ButterKnife.bind(this,itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            mItemClickListener.play(mSongList.get(getAdapterPosition()),getAdapterPosition());
                        }catch (IOException e){
                            e.printStackTrace();
                        }catch (RemoteException e){
                            e.printStackTrace();
                        }
                    }
                });
            }

            public void setData(int position){
                Song song=mSongList.get(position);
                songTitle.setText(song.songTitle);
                songSinger.setText(song.songSinger);
                SimpleDateFormat formatter=new SimpleDateFormat("HH:mm:ss");
                String time=formatter.format(new Date(song.songDuration));
                songDuration.setText(time.split(":")[1]+":"+time.split(":")[2]);
            }
    }
}
