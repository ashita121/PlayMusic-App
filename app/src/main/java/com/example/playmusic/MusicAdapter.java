package com.example.playmusic;


import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyVieHolder> //RecyclerVew for display all songs in the list view//
{
    private final Context nContext;
    private final ArrayList<MusicFiles> nFiles;
    OnOptionClick optionClick;

    MusicAdapter(Context nContext, ArrayList<MusicFiles> nFiles,OnOptionClick onOptionClick)
    {
       this.nFiles = nFiles;
       this.nContext = nContext;
       this.optionClick = onOptionClick;
    }


    @NonNull
    @Override
    public MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(nContext).inflate(R.layout.music_items, parent, false);
        return new MyVieHolder(view);
    }

    @Override
    public void onBindViewHolder( MyVieHolder holder, int position) {
        int currentPosition = position;
        holder.file_name.setText(nFiles.get(position).getTitle());
        byte[] image = getAlbumArt(nFiles.get(position).getPath());
        if (image !=null)
        {
            Glide.with(nContext).asBitmap()
                    .load(image)
                    .into(holder.album_art);
        }
        else {
            Glide.with(nContext)
                    .load(R.drawable.ic_launcher_background)
                    .into(holder.album_art);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(nContext, PlayerActivity.class);
                intent.putExtra("position",position);
                nContext.startActivity(intent);
            }
        });
        holder.More.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(nContext, view);
                popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener((item) -> {
                    if (item.getItemId() == R.id.delete) {
                        Toast.makeText(nContext, "Delete file!", Toast.LENGTH_SHORT).show();
                        Log.e("Song Position", String.valueOf(position));
                        optionClick.onDeleteOptionClick(currentPosition);
                        deleteFile(position, view);

                    }
                    return true;
                });
            }
        });
    }

    private void deleteFile(int position, View view)
    {
        Log.e("Position", String.valueOf(position));
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(nFiles.get(position).getId()));//Content//
        nFiles.remove(position);
        File file = new File(nFiles.get(position).getPath());
         boolean deleted = file.delete();// Deleted your file //
        if (file.exists())
        {
            Log.e("Process","File Deleting");
            nContext.getContentResolver().delete(contentUri, null, null);
//            nFiles.remove(position);
            //notifyItemRemoved(position);//
            //notifyItemRangeChanged(position, nFiles.size());//
            if(!file.exists())
                Log.e("Process","Delete file success..");
            Snackbar.make(view, "File Deleted", Snackbar.LENGTH_LONG)
                .show();

        }else{
            Log.e("File","Deleted..");
        }
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return nFiles.size();
    }


    public static class MyVieHolder extends RecyclerView.ViewHolder
    {
        TextView file_name;
        ImageView album_art, More;
        public MyVieHolder(@NonNull View itemView)
        {
            super(itemView);
            file_name = itemView.findViewById(R.id.music_file_name);
            album_art = itemView.findViewById(R.id.music_img);
            More = itemView.findViewById(R.id.More);
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

    public interface OnOptionClick{
        void onDeleteOptionClick(int position);
    }
}
