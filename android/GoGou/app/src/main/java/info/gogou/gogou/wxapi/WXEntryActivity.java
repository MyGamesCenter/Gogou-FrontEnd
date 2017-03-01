package info.gogou.gogou.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import info.gogou.gogou.constants.GoGouConstants;
import info.gogou.gogou.ui.UsrLoginActivity;

;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{

    private static final String TAG = "WXEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_weixin);

    	api = WXAPIFactory.createWXAPI(this, GoGouConstants.WEIXIN_APP_ID, false);

        api.handleIntent(getIntent(), this);

    }

	// @Override
	public void onReq(BaseReq req) {

        Log.d(TAG, "Wechat onReq is called.");
        finish();
	}

	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String code = ((SendAuth.Resp) resp).token;

                Log.d(TAG, "Wechat return code:" + code);
                UsrLoginActivity.wechatCode = code;
                finish();

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                break;
            default:
                break;
		}
	}

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }
}