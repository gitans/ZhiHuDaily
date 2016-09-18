package com.marktony.zhihudaily.detail;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.marktony.zhihudaily.R;
import com.marktony.zhihudaily.app.App;
import com.marktony.zhihudaily.db.DatabaseHelper;

public class ZhihuDetailActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(App.getThemeResources());
        setContentView(R.layout.frame);

        ZhihuDetailFragment fragment = ZhihuDetailFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .commit();

        new ZhihuDetailPresenter(this, fragment);


        /*// 如果当前没有网络连接，则加载缓存中的内容
        if ( !NetworkState.networkConnected(ZhihuDetailActivity.this)){

            ivFirstImg.setImageResource(R.drawable.no_img);
            ivFirstImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

            String content = loadContentFromDB("" + id).replace("<div class=\"img-place-holder\">", "");
            content = content.replace("<div class=\"headline\">", "");

            String theme = "<body className=\"\" onload=\"onLoaded()\">";
            if (App.getThemeValue() == Theme.NIGHT_THEME){
                theme = "<body className=\"\" onload=\"onLoaded()\" class=\"night\">";
            }

            if (loadContentFromDB("" + id) == null || loadContentFromDB("" + id).isEmpty()){
                Snackbar.make(fab, R.string.loaded_failed,Snackbar.LENGTH_SHORT).show();
            } else {
                String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/zhihu_daily.css\" type=\"text/css\">";

                String html = "<!DOCTYPE html>\n"
                        + "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                        + "<head>\n"
                        + "\t<meta charset=\"utf-8\" />"
                        + css
                        + "\n</head>\n"
                        + theme
                        + content
                        + "</body></html>";

                webViewRead.loadDataWithBaseURL("x-data://base",html,"text/html","utf-8",null);
            }

            if (dialog.isShowing()){
                dialog.dismiss();
            }

        } else {

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Api.ZHIHU_NEWS + id, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {

                    try {

                        // 需要注意的是这里有可能没有body。。。 好多坑。。。
                        // attention here, it may not contain 'body'
                        // 如果没有body，则加载share_url中内容
                        // if 'body' is null, load the content of share_url
                        if (jsonObject.isNull("body")){

                            webViewRead.loadUrl(jsonObject.getString("share_url"));
                            ivFirstImg.setImageResource(R.drawable.no_img);
                            ivFirstImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

                            // 在body为null的情况下，share_url的值是依然存在的
                            // though 'body' is null, share_url exists.
                            shareUrl = jsonObject.getString("share_url");

                        } else {  // body不为null  body is not null

                            shareUrl = jsonObject.getString("share_url");

                            if ( !jsonObject.isNull("image")){

                                Glide.with(ZhihuDetailActivity.this).load(jsonObject.getString("image")).centerCrop().into(ivFirstImg);

                                tvCopyRight.setText(jsonObject.getString("image_source"));

                            } else if (image == null){

                                ivFirstImg.setImageResource(R.drawable.no_img);
                                ivFirstImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

                            } else {

                                Glide.with(ZhihuDetailActivity.this).load(image).centerCrop().into(ivFirstImg);

                            }

                            // 在api中，css的地址是以一个数组的形式给出，这里需要设置
                            // in fact,in api,css addresses are given as an array
                            // api中还有js的部分，这里不再解析js
                            // javascript is included,but here I don't use it
                            // 不再选择加载网络css，而是加载本地assets文件夹中的css
                            // use the css file from local assets folder,not from network
                            String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/zhihu_daily.css\" type=\"text/css\">";

                            // body中替换掉img-place-holder div
                            // replace the img-place-holder with an empty value in body
                            // 可以去除网页中div所占的区域
                            // to avoid the div occupy the area
                            // 如果没有去除这个div，那么整个网页的头部将会出现一部分的空白区域
                            // if we don't delete the div, the web page will have a blank area

                            String content = jsonObject.getString("body").replace("<div class=\"img-place-holder\">", "");
                            // div headline占据了一段高度，需要手动去除
                            // delete the headline div
                            content = content.replace("<div class=\"headline\">", "");

                            // 根据主题的不同确定不同的加载内容
                            // load content judging by different theme
                            String theme = "<body className=\"\" onload=\"onLoaded()\">";
                            if (App.getThemeValue() == Theme.NIGHT_THEME){
                                theme = "<body className=\"\" onload=\"onLoaded()\" class=\"night\">";
                            }

                            String html = "<!DOCTYPE html>\n"
                                    + "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                                    + "<head>\n"
                                    + "\t<meta charset=\"utf-8\" />"
                                    + css
                                    + "\n</head>\n"
                                    + theme
                                    + content
                                    + "</body></html>";

                            webViewRead.loadDataWithBaseURL("x-data://base",html,"text/html","utf-8",null);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (dialog.isShowing()){
                        dialog.dismiss();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Snackbar.make(fab, R.string.loaded_failed,Snackbar.LENGTH_SHORT).show();

                    if (dialog.isShowing()){
                        dialog.dismiss();
                    }
                }
            });

            queue.add(request);
        }*/

    }

   /* private String loadContentFromDB(String id){

        String content = null;
        DatabaseHelper dbHelper = new DatabaseHelper(ZhihuDetailActivity.this,"History.db",null,3);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Contents",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                if (cursor.getString(cursor.getColumnIndex("id")).equals(id)){
                    content = cursor.getString(cursor.getColumnIndex("content"));
                }

            } while (cursor.moveToNext());
        }
        cursor.close();

        return content;
    }*/

}