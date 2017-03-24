package com.io.tools.android.ramiloif.folderchooser;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

    public static class ChooseDirectoryDialogBuilder{
        ChooseDirectoryDialog mDialog;

        public ChooseDirectoryDialogBuilder(Context context){
            mDialog = new ChooseDirectoryDialog(context);
        }

        public ChooseDirectoryDialogBuilder okText(String text) {
            mDialog.mOkText = text;
            return this;
        }

        public ChooseDirectoryDialogBuilder cancelText(String text) {
            mDialog.mCancelText = text;
            return this;
        }

        public ChooseDirectoryDialogBuilder titleText(String text) {
            mDialog.mTitleText = text;
            return this;
        }

        public ChooseDirectoryDialogBuilder startDir(File dir) {
            mDialog.startDir = dir;
            return this;
        }

        public ChooseDirectoryDialogBuilder onPickListener(DirectoryChooseListener listener){
            mDialog.mListener = listener;
            return this;
        }

        public ChooseDirectoryDialogBuilder neverAskAgainText(String mNeverAskAgainText) {
            mDialog.mNeverAskAgainText = mNeverAskAgainText;
            return this;
        }

        public ChooseDirectoryDialogBuilder showNeverAskAgain(boolean isShow) {
            mDialog.mShowNeverAskAgain = isShow;
            return this;
        }

        public ChooseDirectoryDialog build(){
            return mDialog;
        }
    }

    public class DialogResult{
        private String mPath;
        private boolean mAskAgain;

        public String getPath() {
            return mPath;
        }

        public boolean isAskAgain() {
            return mAskAgain;
        }
    }

    public interface DirectoryChooseListener {

        void onDirectoryPicked(DialogResult result);
        void onCancel();

    }

    public static ChooseDirectoryDialogBuilder builder(Context context){
        return new ChooseDirectoryDialogBuilder(context);
    }

    private TextView mFolderNameTV;
    private boolean mChoosed = false;
    private CheckBox mShowAgainCHB;

    private RecyclerView.Adapter mAdapter;
    private String mOkText = "Choose Folder";
    private String mCancelText = "Cancel";
    private String mTitleText = "Pick Folder";
    private File startDir = Environment.getExternalStorageDirectory();
    private File mSelectedDir;
    private DirectoryChooseListener mListener;
    private final List<File> mFilesList = new ArrayList<>();
    private String mNeverAskAgainText = "Never ask again";
    private boolean mShowNeverAskAgain = false;

    public ChooseDirectoryDialog(Context context) {
        super(context);
    }

    public ChooseDirectoryDialog setOKText(String text) {
        mOkText = text;
        return this;
    }

    public ChooseDirectoryDialog setCancelText(String text) {
        mCancelText = text;
        return this;
    }

    public ChooseDirectoryDialog setTitleText(String text) {
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

    public ChooseDirectoryDialog setNeverAskAgainText(String mNeverAskAgainText) {
        this.mNeverAskAgainText = mNeverAskAgainText;
        return this;
    }

    public ChooseDirectoryDialog showNeverAskAgain(boolean isShow) {
        this.mShowNeverAskAgain = isShow;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_directory);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.file_folder_recycler);
        ((TextView)findViewById(R.id.title)).setText(mTitleText);
        mFolderNameTV =  (TextView)findViewById(R.id.folder_name);
        if(mShowNeverAskAgain){
           mShowAgainCHB =  (CheckBox)findViewById(R.id.checkbox);
            mShowAgainCHB.setText(mNeverAskAgainText);
            mShowAgainCHB.setVisibility(View.VISIBLE);
        }
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
                DialogResult result = new DialogResult();
                result.mPath = mSelectedDir.getAbsolutePath();
                if(mShowNeverAskAgain){
                    result.mAskAgain = mShowAgainCHB.isChecked();
                }
                mListener.onDirectoryPicked(result);
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
        mFolderNameTV.setText(buildTitleText());
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