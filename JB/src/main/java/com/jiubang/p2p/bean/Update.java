package com.jiubang.p2p.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @description:
 * @author: Liu wei
 * @mail: i@liuwei.co
 * @date: 2014-3-12
 */
public class Update {

    protected int versionCode;//版本号

    protected String versionName;//版本名称

    protected String downloadURL;//下载地址

    protected String versionDesc;//版本描述

	public Update(JSONObject o) throws JSONException {
        super();
        //产品信息部分
        versionCode = o.getInt("androidVersion");
        versionName = o.getString("androidVersionName");
        downloadURL = o.getString("androidDownLink");
        versionDesc = o.getString("androidVersionDes");
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    @Override
    public String toString() {
        return "Update [versionCode=" + versionCode + ", versionName="
                + versionName + ", downloadURL=" + downloadURL
                + ", versionDesc=" + versionDesc + "]";
    }


}
