package com.penseapp.acaocontabilidade.chat.messages.view;

import com.penseapp.acaocontabilidade.chat.messages.model.Message;

/**
 * Created by unity on 21/11/16.
 */

public interface MessagesView {
    void onMessageAdded(Message message);
    void onMessageChanged(Message message);
}
