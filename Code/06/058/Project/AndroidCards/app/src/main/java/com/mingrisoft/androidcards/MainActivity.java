package com.mingrisoft.androidcards;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initImageLoader();
        if (savedInstanceState == null) {
        //添加布局
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new CardFragment())
                    .commitAllowingStateLoss();
        }
    }
//在应用中配置ImageLoaderConfiguration参数（只能配置一次，如多次配置，则默认第一次的配置参数）
    @SuppressWarnings("deprecation")
    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this)
                .memoryCacheExtraOptions(480, 800)
                // 默认设备屏幕尺寸
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                // 设置图片加载和显示队列处理的类型 默认为QueueProcessingType.FIFO
                // 注:如果设置了taskExecutor或者taskExecutorForCachedImages 此设置无效
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                //设置拒绝缓存在内存中一个图片多个大小 默认为允许,(同一个图片URL)根据不同大小的imageview保存不同大小图片
                .denyCacheImageMultipleSizesInMemory()
                // 设置内存缓存 默认为一个当前应用可用内存的1/8大小的LruMemoryCache
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                // 设置内存缓存的最大大小 默认为一个当前应用可用内存的1/8
                .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13)
                // 缓冲大小
                .discCacheSize(50 * 1024 * 1024)
                // 缓冲文件数目
                .discCacheFileCount(100)
                // 设置硬盘缓存文件名生成规范
                // 默认为new HashCodeFileNameGenerator()
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                // 设置图片下载器
                // 默认为 DefaultConfigurationFactory.createBitmapDisplayer()
                .imageDownloader(new BaseImageDownloader(this))
                // 设置默认的图片显示选项
                // 默认为DisplayImageOptions.createSimple()
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                // 打印DebugLogs
                .writeDebugLogs().build();

        // 2.单例ImageLoader类的初始化 全局初始化此配置
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }
}
