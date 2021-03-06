package com.example.playmusic;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.MyHolder> {
    private Context nContext;
    private ArrayList<MusicFiles> albumFiles;
    View view;

    public AlbumDetailsAdapter(Context nContext, ArrayList<MusicFiles> albumFiles) {
        this.nContext = nContext;
        this.albumFiles = albumFiles;
    }

    public AlbumDetailsAdapter(Context context, ArrayList<MusicFiles> musicFiles, Object onDeleteOptionClick)
    {
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(nContext).inflate(R.layout.music_items, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.album_name.setText(albumFiles.get(position).getTitle());
        byte[] image = getAlbumArt(albumFiles.get(position).getPath());
        if (image !=null)
        {
            Glide.with(nContext).asBitmap()
                    .load(image)
                    .into(holder.album_image);
        }
        else {
            Glide.with(nContext)
                    .load(R.drawable.ic_launcher_background)
                    .into(holder.album_image);
        }

    }

    @Override
    public int getItemCount() {
        return albumFiles.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView album_image;
        TextView album_name;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            album_image = itemView.findViewById(R.id.music_img);
            album_name = itemView.findViewById(R.id.music_file_name);

        }
    }
    private byte[] getAlbumArt(String uri)  //All Album retrive//
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
