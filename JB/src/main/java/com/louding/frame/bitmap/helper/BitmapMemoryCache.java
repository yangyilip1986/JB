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
package com.louding.frame.bitmap.helper;

import com.louding.frame.utils.SystemTool;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

/**
 * 使用lru算法的Bitmap内存缓存池<br>
 * 
 * <b>创建时间</b> 2014-7-11
 * 
 * @version 1.0
 * @author kymjs(kymjs123@gmail.com)
 */
public final class BitmapMemoryCache {

    private MemoryLruCache<String, Bitmap> cache;

    public BitmapMemoryCache() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        init(maxMemory / 8);
    }

    /**
     * @param maxSize
     *            使用内存缓存的内存大小，单位：kb
     */
    public BitmapMemoryCache(int maxSize) {
        init(maxSize);
    }

    /**
     * @param maxSize
     *            使用内存缓存的内存大小，单位：kb
     */
    @SuppressLint("NewApi")
    private void init(int maxSize) {
        cache = new MemoryLruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                super.sizeOf(key, value);
                if (SystemTool.getSDKVersion() >= 12) {
                    return value.getByteCount() / 1024;
                } else {
                    return value.getRowBytes() * value.getHeight() / 1024;
                }
            }
        };
    }

    public void put(String key, Bitmap bitmap) {
        if (this.get(key) == null) {
            cache.put(key, bitmap);
        }
    }

    public Bitmap get(String key) {
        return cache.get(key);
    }
}
