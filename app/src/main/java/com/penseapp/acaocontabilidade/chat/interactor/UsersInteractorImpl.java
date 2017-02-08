package com.penseapp.acaocontabilidade.chat.interactor;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.penseapp.acaocontabilidade.chat.presenter.UsersPresenter;
import com.penseapp.acaocontabilidade.domain.FirebaseHelper;
import com.penseapp.acaocontabilidade.login.model.User;

/**
 * Created by unity on 08/02/17.
 */

public class UsersInteractorImpl implements UsersInteractor {

    private UsersPresenter usersPresenter;

    public UsersInteractorImpl(UsersPresenter usersPresenter) {
        this.usersPresenter = usersPresenter;
    }

    @Override
    public void getCurrentUserDetails() {
        String currentUserId = FirebaseHelper.getInstance().getAuthUserId();
        FirebaseHelper.getInstance().getUsersReference().child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                usersPresenter.onReceiveCurrentUserDetails(currentUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
