/*
 * Copyright (c) 2014,KJFrameForAndroid Open Source Project,张涛.
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
package com.louding.frame.bitmap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.louding.frame.bitmap.helper.BitmapCreate;
import com.louding.frame.bitmap.helper.DiskCache;
import com.louding.frame.utils.CipherUtils;
import com.louding.frame.utils.FileUtils;
import com.louding.frame.utils.KJLoger;

public class BitmapDownloader implements I_ImageLoader {
    private final DiskCache diskCache;
    private int width, height;
    private final BitmapConfig config;
    private BitmapCallBack callback;

    public BitmapDownloader(BitmapConfig config, int reqW, int reqH) {
        diskCache = new DiskCache(config.cachePath, config.memoryCacheSize * 8,
                config.isDEBUG);
        this.width = reqW;
        this.height = reqH;
        this.config = config;
    }

    /**
     * 默认的宽高为0，也就是加载图片显示默认大小，不处理OOM
     * 
     * @param w
     * @param h
     */
    @Override
    public void setImageWH(int w, int h) {
        this.width = w;
        this.height = h;
    }

    @Override
    public void setImageCallBack(BitmapCallBack callback) {
        this.callback = callback;
    }

    /**
     * 从网络加载图片
     * 
     * @param uri
     * @return
     */
    private byte[] fromNet(String uri) {
        byte[] data = null;
        HttpURLConnection con = null;
        try {
            URL url = new URL(uri);
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.connect();
            data = FileUtils.input2byte(con.getInputStream());
            putBmpToDC(uri, data); // 建立diskLru缓存
        } catch (Exception e) {
            failure(e);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return data;
    }

    /**
     * 从本地载入一张图片
     * 
     * @param imagePath
     *            图片的地址
     * @throws FileNotFoundException
     */
    private byte[] fromFile(String uri) {
        byte[] data = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(uri);
            if (fis != null) {
                data = FileUtils.input2byte(fis);
                putBmpToDC(uri, data); // 建立diskLru缓存
            }
        } catch (Exception e) {
            failure(e);
        } finally {
            FileUtils.closeIO(fis);
        }
        return data;
    }

    /**
     * 加入磁盘缓存
     * 
     * @param imagePath
     *            图片路径
     * @param bmpByteArray
     *            图片二进制数组数据
     */
    private void putBmpToDC(String imagePath, byte[] bmpByteArray) {
        diskCache.put(CipherUtils.md5(imagePath), BitmapCreate
                .bitmapFromByteArray(bmpByteArray, 0, bmpByteArray.length,
                        this.width, this.height));
    }

    /**
     * 如果打开了log显示器，则显示log
     * 
     * @param imageUrl
     */
    private void showLogIfOpen(String log) {
        if (config.isDEBUG) {
            KJLoger.debugLog(getClass().getName(), log);
        }
    }

    private void failure(Exception e) {
        if (callback != null) {
            callback.onFailure(e);
        }
        throw new RuntimeException(e);
    }

    @Override
    public byte[] loadImage(String uri) {
        byte[] data = diskCache.getByteArray(CipherUtils.md5(uri));
        if (data == null) {
            if (uri.trim().toLowerCase().startsWith("http")) {
                // 网络图片：首先从本地缓如果存读取，本地没有，则重新从网络加载
                data = fromNet(uri);
                showLogIfOpen("download image from net");
            } else {
                // 如果是本地图片
                data = fromFile(uri);
                showLogIfOpen("download image from local file");
            }
        } else {
            showLogIfOpen("download image from disk cache");
        }
        return data;
    }
}
