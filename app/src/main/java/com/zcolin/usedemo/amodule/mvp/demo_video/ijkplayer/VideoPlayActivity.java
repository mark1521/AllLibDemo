/*
 * *********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     17-5-22 下午3:33
 * ********************************************************
 */

package com.zcolin.usedemo.amodule.mvp.demo_video.ijkplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.zcolin.usedemo.R;
import com.zcolin.usedemo.amodule.mvp.demo_video.ijkplayer.base.BaseVideoPlayActivity;
import com.zplayer.library.ZPlayer;

/**
 * 类描述：视频详情页
 * <p>
 * Super南仔
 * <p>
 * update by colin on 2016-10-18
 */
public class VideoPlayActivity extends BaseVideoPlayActivity implements View.OnClickListener, ZPlayer.OnNetChangeListener {
    private boolean isLive;
    private String  url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ijk_videoplay);
        setToolbarTitle("单视频播放");

        isLive = getIntent().getBooleanExtra("isLive", false);
        url = getIntent().getStringExtra("url");

        player.setTitle(url)//设置视频的titleName
              .play(url);//开始播放视频

        findViewById(R.id.tv_replay).setOnClickListener(this);
        findViewById(R.id.tv_play_location).setOnClickListener(this);
        findViewById(R.id.tv_play_switch).setOnClickListener(this);
    }

    @Override
    protected ZPlayer initPlayer() {
        ZPlayer player = (ZPlayer) findViewById(R.id.view_super_player);
        player.setLive(isLive)//设置该地址是直播的地址
              .setNetChangeListener(true)//设置监听手机网络的变化,这个参数是内部是否处理网络监听，和setOnNetChangeListener没有关系
              .setOnNetChangeListener(this)//实现网络变化的回调
              .onPrepared(new ZPlayer.OnPreparedListener() {
                  @Override
                  public void onPrepared() {
                      //TODO 监听视频是否已经准备完成开始播放。（可以在这里处理视频封面的显示跟隐藏）
                  }
              })
              .onComplete(new Runnable() {
                  @Override
                  public void run() {
                      //TODO 监听视频是否已经播放完成了。（可以在这里处理视频播放完成进行的操作）
                  }
              })
              .onInfo(new ZPlayer.OnInfoListener() {
                  @Override
                  public void onInfo(int what, int extra) {
                      //TODO 监听视频的相关信息。
                  }
              })
              .onError(new ZPlayer.OnErrorListener() {
                  @Override
                  public void onError(int what, int extra) {
                      //TODO 监听视频播放失败的回调
                  }
              });
        player.setScaleType(ZPlayer.SCALETYPE_FITXY);
        player.setPlayerWH(0, player.getMeasuredHeight());//设置竖屏的时候屏幕的高度，如果不设置会切换后按照16:9的高度重置
        return player;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_replay) {
            if (player != null) {
                player.play(url);
            }
        } else if (view.getId() == R.id.tv_play_location) {
            if (isLive) {
                Toast.makeText(this, "直播不支持指定播放", Toast.LENGTH_SHORT)
                     .show();
                return;
            }
            if (player != null) {
                //这个节点是根据视频的大小来获取的。不同的视频源节点也会不一致（一般用在退出视频播放后保存对应视频的节点从而来达到记录播放）
                player.play(url, 89528);
            }
        } else if (view.getId() == R.id.tv_play_switch) {
            //切换视频播放源（一般是标清，高清的切换ps：由于我没有找到高清，标清的视频源，所以也是换相同的地址）
            if (isLive) {
                player.playSwitch(url);
            } else {
                player.playSwitch("http://baobab.wandoujia.com/api/v1/playUrl?vid=2614&editionType=high");
            }
        }
    }

    /**
     * 网络链接监听类
     */
    @Override
    public void onWifi() {
        Toast.makeText(this, "当前网络环境是WIFI", Toast.LENGTH_SHORT)
             .show();
    }

    @Override
    public void onMobile() {
        Toast.makeText(this, "当前网络环境是手机网络", Toast.LENGTH_SHORT)
             .show();
    }

    @Override
    public void onDisConnect() {
        Toast.makeText(this, "网络链接断开", Toast.LENGTH_SHORT)
             .show();
    }

    @Override
    public void onNoAvailable() {
        Toast.makeText(this, "无网络链接", Toast.LENGTH_SHORT)
             .show();
    }
}
