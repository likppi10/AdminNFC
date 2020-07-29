package com.example.sns_project_nfc.listener;

import com.example.sns_project_nfc.PostInfo;

public interface OnPostListener {
    void onDelete(PostInfo postInfo);
    void onModify();
}
