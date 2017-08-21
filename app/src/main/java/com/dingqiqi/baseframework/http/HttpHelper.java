package com.dingqiqi.baseframework.http;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.dingqiqi.baseframework.manager.ExecutorManager;
import com.dingqiqi.baseframework.util.DialogUtil;
import com.dingqiqi.baseframework.util.LogUtil;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * 网络请求库
 * Created by dingqiqi on 2016/12/28.
 */
public class HttpHelper {

    private final static int TIME_OUT = 10000;
    /**
     * HttpHelper对象
     */
    private static HttpHelper mHttpHelper;

    private Handler mHandler = new Handler();

    /**
     * 获取HttpHelper对象
     *
     * @return
     */
    public static HttpHelper getInstance() {
        if (mHttpHelper == null) {
            mHttpHelper = new HttpHelper();
        }
        return mHttpHelper;
    }

    /**
     * 使用GET方式请求服务器数据(不带证书)
     *
     * @param context   上下文
     * @param url       请求url
     * @param params    请求参数
     * @param headerMap 请求头
     * @param mBack     回调接口
     */
    public void invokeGet(FragmentActivity context, String url, Map<String, String> params,
                          Map<String, String> headerMap, final RequestBack mBack, boolean isProgress) {
        invokeGet(context, url, params, headerMap, mBack, false, "", isProgress);
    }

    /**
     * 使用GET方式请求服务器数据(带证书)
     *
     * @param context   上下文
     * @param url       请求url
     * @param params    请求参数
     * @param headerMap 请求头
     * @param mBack     回调接口
     * @param isCer     是否是证书请求
     * @param cerName   证书名称
     */
    public void invokeGet(FragmentActivity context, String url, Map<String, String> params,
                          Map<String, String> headerMap, final RequestBack mBack, boolean isCer, String cerName, boolean isProgress) {
        try {
            if (isProgress) {
                DialogUtil.showWaitingDialog(context);
            }

            URL mUrl = new URL(url + "?" + getParam(params));
            final HttpURLConnection mConnection = initRequest(context, isCer, cerName, mUrl, HttpUtil.GET);

            if (mConnection == null) {
                doBack(mBack, "网络异常", false);
                return;
            }

            setParam(headerMap, mConnection);

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        mConnection.connect();
                        InputStream mInputStream = mConnection.getInputStream();
                        BufferedReader mReader = new BufferedReader(
                                new InputStreamReader(mInputStream));
                        String str;
                        final StringBuffer buffer = new StringBuffer();
                        while ((str = mReader.readLine()) != null) {
                            buffer.append(str);
                        }

                        if (200 == mConnection.getResponseCode()) {
                            doBack(mBack, TextUtils.isEmpty(buffer) ? "null" : buffer.toString(), true);
                        } else {
                            doBack(mBack, TextUtils.isEmpty(buffer) ? "null" : buffer.toString(), false);
                        }

                        mInputStream.close();
                        mConnection.disconnect();

                        LogUtil.i("result:" + (TextUtils.isEmpty(buffer) ? "null" : buffer.toString()));
                    } catch (IOException e) {
                        doBack(mBack, "网络异常", false);
                        e.printStackTrace();
                    }
                }
            };

            ExecutorManager.getInstance().request(runnable);

        } catch (Exception e) {
            doBack(mBack, "网络异常", false);
            e.printStackTrace();
        }
    }

    /**
     * 使用POST方式请求服务器数据（不带证书）
     *
     * @param context   上下文
     * @param url       请求url
     * @param params    请求参数
     * @param headerMap 请求头
     * @param mBack     回调接口
     */
    public void invokePost(FragmentActivity context, String url, Map<String, String> params,
                           Map<String, String> headerMap, final RequestBack mBack, boolean isProgress) {
        invokePost(context, url, params, headerMap, mBack, false, "", isProgress);
    }

    /**
     * 使用POST方式请求服务器数据（带证书）
     *
     * @param context   上下文
     * @param url       请求url
     * @param params    请求参数
     * @param headerMap 请求头
     * @param mBack     回调接口
     * @param isCer     是否是证书请求
     * @param cerName   证书名称
     */
    public void invokePost(FragmentActivity context, String url, Map<String, String> params,
                           Map<String, String> headerMap, final RequestBack mBack, boolean isCer, String cerName, boolean isProgress) {
        try {
            if (isProgress) {
                DialogUtil.showWaitingDialog(context);
            }

            URL mUrl = new URL(url);
            final HttpURLConnection mConnection = initRequest(context, isCer, cerName, mUrl, HttpUtil.POST);
            if (mConnection == null) {
                doBack(mBack, "网络异常", false);
                return;
            }

            setParam(headerMap, mConnection);

            OutputStream outputStream = mConnection.getOutputStream();
            outputStream.write(new Gson().toJson(params).getBytes());
            outputStream.flush();
            outputStream.close();

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        mConnection.connect();
                        InputStream mInputStream = mConnection.getInputStream();
                        BufferedReader mReader = new BufferedReader(
                                new InputStreamReader(mInputStream));
                        String str;
                        final StringBuffer buffer = new StringBuffer();
                        while ((str = mReader.readLine()) != null) {
                            buffer.append(str);
                        }

                        if (200 == mConnection.getResponseCode()) {
                            doBack(mBack, TextUtils.isEmpty(buffer) ? "null" : buffer.toString(), true);
                        } else {
                            doBack(mBack, TextUtils.isEmpty(buffer) ? "null" : buffer.toString(), false);
                        }

                        mInputStream.close();
                        mConnection.disconnect();

                        LogUtil.i("result:" + (TextUtils.isEmpty(buffer) ? "null" : buffer.toString()));
                    } catch (IOException e) {
                        doBack(mBack, "网络异常", false);
                        e.printStackTrace();
                    }
                }
            };

            ExecutorManager.getInstance().request(runnable);
        } catch (Exception e) {
            doBack(mBack, "网络异常", false);
            e.printStackTrace();
        }
    }

    /**
     * 初始化HttpURLConnection
     *
     * @param context     上下文
     * @param isCer       是否有证书
     * @param cerName     证书名称
     * @param mUrl
     * @param requestType 请求类型
     * @return
     */
    private HttpURLConnection initRequest(FragmentActivity context, boolean isCer, String cerName, URL mUrl, String requestType) {
        HttpURLConnection mConnection = null;
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("TLS");
            if (isCer) {
                TrustManagerFactory tmf = initCer(context, cerName);
                if (tmf != null) {
                    sc.init(null, tmf.getTrustManagers(), null);
                } else {
                    sc.init(null, new TrustManager[]{new TrustAllManager()},
                            new SecureRandom());
                }
            } else {
                sc.init(null, new TrustManager[]{new TrustAllManager()},
                        new SecureRandom());
            }

            mConnection = (HttpURLConnection) mUrl.openConnection();
            mConnection.setDoOutput(true);
            mConnection.setDoInput(true);
            mConnection.setUseCaches(false);
            mConnection.setInstanceFollowRedirects(true);
            mConnection.setRequestMethod(requestType);
            mConnection.setConnectTimeout(TIME_OUT);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mConnection;
    }

    /**
     * 设置请求头
     *
     * @param param
     * @param mConnection
     */
    private void setParam(Map<String, String> param, HttpURLConnection mConnection) throws UnsupportedEncodingException {
        if (param == null) {
            return;
        }

        for (Map.Entry<String, String> map : param.entrySet()) {
            if (map.getKey() != null && map.getValue() != null) {
                mConnection.setRequestProperty(map.getKey(), map.getValue());
                //LogUtil.i("HttpHelper", "请求头参数：" + map.getKey() + "=" + map.getValue());
            }
        }
    }

    /**
     * 获取请求参数
     *
     * @param param
     */
    private String getParam(Map<String, String> param) throws UnsupportedEncodingException {
        if (param == null) {
            return "";
        }
        String url = "";

        for (Map.Entry<String, String> map : param.entrySet()) {
            if (map.getKey() != null && map.getValue() != null) {
                url = url + map.getKey() + "=" + URLEncoder.encode(map.getValue(), "utf-8") + "&";
                //  LogUtil.i( "参数：" + map.getKey() + "=" + URLEncoder.encode(map.getValue(), "utf-8"));
            }
        }

        if ("".equals(url)) {
            return "";
        }
        //取出最后一个&
        return url.substring(0, url.length() - 1);
    }

    /**
     * 请求回调
     *
     * @param mBack
     * @param content
     * @param mIsSuccess
     */
    private void doBack(final RequestBack mBack, final String content, boolean mIsSuccess) {
        //关闭弹窗
        DialogUtil.dismissWaitingDialog();
        if (mIsSuccess) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mBack.onSuccess(content);
                }
            });
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mBack.onFail(content);
                }
            });
        }
    }

    /**
     * 初始化https证书
     *
     * @param context 上下文
     * @param creName 证书名称
     * @return
     */
    private TrustManagerFactory initCer(Context context, String creName) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream in = context.getAssets().open(creName);
            Certificate ca = cf.generateCertificate(in);

            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(null, null);
            keystore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keystore);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 忽略所有证书
     */
    private class TrustAllManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    public interface RequestBack {
        /**
         * 成功
         */
        public void onSuccess(String result);

        /**
         * 失败
         */
        public void onFail(String result);
    }

}
