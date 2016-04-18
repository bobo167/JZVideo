package fm.jiecao.jcvideoplayer_lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * <p>全屏的activity</p>
 * <p>fullscreen activity</p>
 * Created by Nathen
 * On 2015/12/01 11:17
 */
public class JCFullScreenActivity extends Activity {

    static void toActivityFromNormal(Context context, int state, String url) {
        CURRENT_STATE = state;
        DIRECT_FULLSCREEN = false;
        URL = url;
        Intent intent = new Intent(context, JCFullScreenActivity.class);
        context.startActivity(intent);
    }

    /**
     * <p>直接进入全屏播放</p>
     * <p>Full screen play video derictly</p>
     *
     * @param context context
     * @param url     video url
     */
    public static void toActivity(Context context, String url) {
        CURRENT_STATE = JCAbstractVideoPlayer.CURRENT_STATE_NORMAL;
        URL = url;
        DIRECT_FULLSCREEN = true;
        Intent intent = new Intent(context, JCFullScreenActivity.class);
        context.startActivity(intent);
    }

    JCDemoVideoPlayer jcVideoPlayer;
    /**
     * 刚启动全屏时的播放状态
     */
    static int CURRENT_STATE = -1;
    public static String URL;
    public static boolean manualQuit = false;
    static boolean DIRECT_FULLSCREEN = false;//this is should be in videoplayer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_fullscreen);//or here setJcVideoPlayer derictly

        jcVideoPlayer = (JCDemoVideoPlayer) findViewById(R.id.jcvideoplayer);
        jcVideoPlayer.IF_CURRENT_IS_FULLSCREEN = true;
        jcVideoPlayer.IF_FULLSCREEN_IS_DIRECTLY = DIRECT_FULLSCREEN;
        jcVideoPlayer.addSurfaceView();
        jcVideoPlayer.setUp(URL);
        jcVideoPlayer.setStateAndUi(CURRENT_STATE);


        if (jcVideoPlayer.IF_FULLSCREEN_IS_DIRECTLY) {
            jcVideoPlayer.ivStart.performClick();
        } else {
            manualQuit = false;
            JCMediaManager.intance().listener = jcVideoPlayer;
        }
    }

    @Override
    public void onBackPressed() {
        if (jcVideoPlayer.IF_FULLSCREEN_IS_DIRECTLY) {
            JCMediaManager.intance().mediaPlayer.release();
        } else {
            jcVideoPlayer.quitFullcreenGoToNormal();
        }
        super.onBackPressed();
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (!manualQuit) {
//            JCVideoPlayer.isClickFullscreen = false;
//            JCVideoPlayer.releaseAllVideos();
//            finish();
//        }
//    }
}
