/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android.encode;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.google.zxing.client.android.FinishListener;
import com.google.zxing.client.android.R;

/**
 * 生成二维码的界面
 */
public final class EncodeActivity extends Activity {

  private static final String TAG = EncodeActivity.class.getSimpleName();
  private QRCodeEncoder qrCodeEncoder;
  private int mScreenWidth;
  private int mScreenHeight;
  private ImageView mQRCodeIv;
  private EditText mContentsEt;

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.encode);

    initsCreenWH();
    initUI();
  }

  private void initUI() {
    mQRCodeIv = (ImageView) findViewById(R.id.image_view);
    mContentsEt = (EditText) findViewById(R.id.contents_et);
  }

  /**
   * 获取屏幕宽高
   * */
  private void initsCreenWH() {
    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
    Display display = manager.getDefaultDisplay();
    Point displaySize = new Point();
    display.getSize(displaySize);
    mScreenWidth = displaySize.x;
    mScreenHeight = displaySize.y;
  }

  /**
   * 点击按钮取出输入框内容 生成二维码
   * */
  public void buildQRCode(View v){
    String contents = mContentsEt.getText().toString();
    if (TextUtils.isEmpty(contents)){
      showErrorMessage(R.string.input_qrcode_tip);
      return;
    }
    //根据屏幕大小计算生成二维码位图的宽高
    int smallerDimension = mScreenWidth < mScreenHeight ? mScreenWidth : mScreenHeight;
    smallerDimension = smallerDimension * 6 / 8;

    try {
      //生成二维码对象 该对象封装了内容信息如二维码位图和内容字符串
      qrCodeEncoder = new QRCodeEncoder(this, contents, smallerDimension, false);
      Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
      if (bitmap == null) {
        Log.w(TAG, "Could not encode barcode");
        showErrorMessage(R.string.msg_encode_contents_failed);
        qrCodeEncoder = null;
        return;
      }
      //获取编码后的位图 展示界面
      mQRCodeIv.setImageBitmap(bitmap);
      mContentsEt.setText(qrCodeEncoder.getDisplayContents());

    } catch (WriterException e) {
      showErrorMessage(R.string.msg_encode_contents_failed);
      qrCodeEncoder = null;
    }
  }

  private void showErrorMessage(int message) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(message);
    builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
    builder.setOnCancelListener(new FinishListener(this));
    builder.show();
  }

}
