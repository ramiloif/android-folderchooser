package com.io.tools.android.ramiloif.folderchooser;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rami Loiferman on 11/03/2017.
 */


public class ChooseDirectoryDialog extends AlertDialog{

    private TextView mTitleTV;
    private boolean mChoosed = false;

    public interface DirectoryChooseListener {

        void onDirectoryPicked(String path);
        void onCancel();

    }
    private RecyclerView.Adapter mAdapter;
    private String mOkText = "Choose Folder";
    private String mCancelText = "Cancel";
    private String mTitleText = "Pick";
    private File startDir = Environment.getExternalStorageDirectory();
    private File mSelectedDir;
    private DirectoryChooseListener mListener;
    private final List<File> mFilesList = new ArrayList<>();

    public ChooseDirectoryDialog(@NonNull Context context) {
        super(context);
    }

    public ChooseDirectoryDialog setOKText(String text) {
        mOkText = text;
        return this;
    }

    public ChooseDirectoryDialog setmCancelText(String text) {
        mCancelText = text;
        return this;
    }

    public ChooseDirectoryDialog setmTitleText(String text) {
        mTitleText = text;
        return this;
    }

    public ChooseDirectoryDialog setStartDir(File dir) {
        startDir = dir;
        return this;
    }

    public ChooseDirectoryDialog setOnPickListener(DirectoryChooseListener listener){
        mListener = listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_directory);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.file_folder_recycler);
        mTitleTV =  (TextView)findViewById(R.id.choose_title);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = createAdapter();
        Button mPositiveButton = (Button) findViewById(R.id.positive_button);
        Button mNegativeButton = (Button) findViewById(R.id.negative_button);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(!mChoosed){
                    mListener.onCancel();
                }
            }
        });
        mNegativeButton.setText(mCancelText);
        mNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
        mPositiveButton.setText(this.mOkText);
        mPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChoosed = true;
                dismiss();
                mListener.onDirectoryPicked(mSelectedDir.getAbsolutePath());
            }
        });
        recyclerView.setAdapter(mAdapter);
        walkToDir(startDir);
    }

    @Override
    public void onBackPressed() {
        if(mSelectedDir.equals(startDir)){
            super.onBackPressed();
        }else {
            mSelectedDir = mSelectedDir.getParentFile();
            walkToDir(mSelectedDir);
        }
    }



    private RecyclerView.Adapter createAdapter(){

        return new RecyclerView.Adapter<FileVH>() {

            @Override
            public FileVH onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_file,
                        parent,
                        false);
                return new FileVH(v);
            }

            @Override
            public void onBindViewHolder(FileVH holder, final int position) {
                final File file = mFilesList.get(position);
                if(file.isDirectory()){
                    holder.mFileImage.setImageResource(R.drawable.ic_folder_black_48dp);
                }else {
                    holder.mFileImage.setImageResource(R.drawable.ic_insert_drive_file_black_36dp);
                }
                holder.mFileText.setText(file.getName());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(file.isDirectory())
                            walkToDir(file);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return mFilesList.size();
            }
        };


    }

    private void walkToDir(File file) {
        mFilesList.clear();
        mFilesList.addAll(Arrays.asList(file.listFiles()));
        mSelectedDir = file;
        mAdapter.notifyDataSetChanged();
        mTitleTV.setText(buildTitleText());
    }

    private CharSequence buildTitleText() {
        return mSelectedDir.getName();
    }

    private class FileVH extends RecyclerView.ViewHolder {
        ImageView mFileImage;
        TextView mFileText;

         FileVH(View itemView) {
            super(itemView);
            mFileImage = (ImageView) itemView.findViewById(R.id.file_icon);
            mFileText = (TextView) itemView.findViewById(R.id.file_text);
        }
    }

}