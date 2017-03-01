/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.gogou.gogou.fcm;

import android.os.Handler;
import android.os.Message;

import com.google.firebase.iid.FirebaseInstanceIdService;

import info.gogou.gogou.constants.GoGouConstants;

public class GoGouInstanceIDListenerService extends FirebaseInstanceIdService {

    private static final String TAG = "GoGouInstanceIDListenerService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Handler mMessageHandler = MessageHandlerService.getInstance().getHandler();
        Message msg = mMessageHandler.obtainMessage();
        msg.what = GoGouConstants.MSG_REGISTRATION;
        mMessageHandler.sendMessage(msg);
    }
    // [END refresh_token]
}
