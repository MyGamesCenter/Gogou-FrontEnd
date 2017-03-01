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

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import info.gogou.gogou.constants.GoGouConstants;

public class GoGouFcmService extends FirebaseMessagingService {

    private static final String TAG = "GoGouFcmService";


    @Override
    public void onMessageReceived(RemoteMessage message) {
        String from = message.getFrom();
        Log.d(TAG, "From: " + from);

        Handler mMessageHandler = MessageHandlerService.getInstance().getHandler();
        Message msg = mMessageHandler.obtainMessage();

        if (from.startsWith("/topics/")) {
            msg.what = GoGouConstants.MSG_TOPICS;
            Bundle bundle = new Bundle();
            bundle.putCharSequence("message", message.getNotification().getBody());
            msg.setData(bundle);
        } else {
            msg.what = GoGouConstants.MSG_MESSAGE;
            Bundle bundle = new Bundle();
            for (Map.Entry<String, String> entry : message.getData().entrySet()) {
                bundle.putCharSequence(entry.getKey(), entry.getValue());
            }
            msg.setData(bundle);
        }

        mMessageHandler.sendMessage(msg);

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        // [END_EXCLUDE]
    }

    @Override
    public void onDeletedMessages() {
        Log.d(TAG, "Messages has been deleted.");
    }

    @Override
    public void onMessageSent(String msgId) {
        Log.d(TAG, "Message: " + msgId + " has been sent!");
    }

    @Override
    public void onSendError(String msgId, Exception exception) {
        // TODO: reflect it on chat activity
        Log.d(TAG, "Message: " + msgId + " can NOT be sent with error: " + exception.getMessage());
    }
    // [END receive_message]
}
