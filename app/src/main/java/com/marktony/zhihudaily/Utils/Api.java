package com.marktony.zhihudaily.Utils;

/**
 * Created by lizhaotailang on 2016/3/18.
 * 包含了主要的api接口
 */
public class Api {

    // 获取界面启动图像
    // start_image后面为图像分辨率
    public static final String START_IMAGE = "http://news-at.zhihu.com/api/4/start-image/1080*1776";

    // 最新消息
    public static final String LATEST = "http://news-at.zhihu.com/api/4/news/latest";

    // 消息内容获取与离线下载
    // 在最新消息中获取到的id，拼接到这个NEWS之后，可以获得对应的JSON格式的内容
    public static final String NEWS = "http://news-at.zhihu.com/api/4/news/";

    // 过往消息
    // 若要查询的11月18日的消息，before后面的数字应该为20161118
    // 知乎日报的生日为2013 年 5 月 19 日，如果before后面的数字小于20130520，那么只能获取到空消息
    public static final String HISTORY = "http://news.at.zhihu.com/api/4/news/before/";

    // 新闻额外消息
    // 输入新闻的ID，获取对应新闻的额外信息，如评论数量，所获的『赞』的数量。
    // http://news-at.zhihu.com/api/4/story-extra/#{id}
    public static final String STORY_EXTRA = "http://news-at.zhihu.com/api/4/story-extra/";

    // 新闻对应长评论查看
    // 使用在 最新消息 中获得的 id
    // 在 http://news-at.zhihu.com/api/4/story/#{id}/long-comments 中将 id 替换为对应的 id
    // 得到长评论 JSON 格式的内容
    // 新闻对应短评论查看
    // http://news-at.zhihu.com/api/4/story/4232852/short-comments
    // 使用在 最新消息 中获得的 id
    // 在 http://news-at.zhihu.com/api/4/story/#{id}/short-comments 中将 id 替换为对应的 id
    // 得到短评论 JSON 格式的内容
    public static final String COMMENTS = "http://news-at.zhihu.com/api/4/story/";

    // 主题日报列表查看
    public static final String THEMES = "http://news-at.zhihu.com/api/4/themes";

    // 主题日报内容查看
    // http://news-at.zhihu.com/api/4/theme/11
    // 使用在 主题日报列表查看 中获得需要查看的主题日报的 id
    // 拼接在 http://news-at.zhihu.com/api/4/theme/ 后
    // 得到对应主题日报 JSON 格式的内容

    // 热门消息
    // 请注意！ 此 API 仍可访问，但是其内容未出现在最新的『知乎日报』 App 中。
    public static final String HOT = "http://news-at.zhihu.com/api/3/news/hot";

    // 查看新闻的推荐者
    // "http://news-at.zhihu.com/api/4/story/#{id}/recommenders"
    // 将新闻id填入到#{id}的位置

    // 获取某个专栏之前的新闻
    // http://news-at.zhihu.com/api/4/theme/#{theme id}/before/#{id}
    // 将专栏id填入到 #{theme id}, 将新闻id填入到#{id}
    // 如 http://news-at.zhihu.com/api/4/theme/11/before/7119483
    // 注：新闻id要是属于该专栏，否则，返回结果为空

    // 查看editor的主页
    // http://news-at.zhihu.com/api/4/editor/#{id}/profile-page/android

}
