/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.penseapp.acaocontabilidade.backend;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.servlet.http.HttpServlet;

public class MyServlet extends HttpServlet {

    private static DatabaseReference ref;

    public static void main(String[] args) {
        // Initialize Firebase
        try {
            // [START initialize]
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setDatabaseUrl("https://acao-f519d.firebaseio.com")
//                    .setServiceAccount(new FileInputStream("/home/unity/Dropbox/AndroidStudioProjects/AcaoContabilidade/backend/src/main/webapp/WEB-INF/Acao-8de3199d9fb8.json"))
                    .setServiceAccount(new FileInputStream("./Acao-8de3199d9fb8.json"))
                    .build();
            FirebaseApp.initializeApp(options);
            // [END initialize]
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: invalid service account credentials. See README.");
            System.out.println(e.getMessage());

            System.exit(1);
        }

        // As an admin, the app has access to read and write all data, regardless of Security Rules
        ref = FirebaseDatabase.getInstance().getReference();
        startListeners();
    }

    public static void startListeners() {
        ref.child("news-notifications").addChildEventListener(new ChildEventListener() {

            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildName) {
                final String newsNotificationId = dataSnapshot.getKey();
                System.out.print(newsNotificationId + " added");
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildName) {}

            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildName) {}

            public void onCancelled(DatabaseError databaseError) {
                System.out.println("startListeners: unable to attach listener to posts");
                System.out.println("startListeners: " + databaseError.getMessage());
            }
        });
    }

//    @Override
//    public void doGet(HttpServletRequest req, HttpServletResponse resp)
//            throws IOException {
//        resp.setContentType("text/plain");
//        resp.getWriter().println("Please use the form to POST to this url");
//    }
//
//    @Override
//    public void doPost(HttpServletRequest req, HttpServletResponse resp)
//            throws IOException {
//        String name = req.getParameter("name");
//        resp.setContentType("text/plain");
//        if (name == null) {
//            resp.getWriter().println("Please enter a name");
//        }
//        resp.getWriter().println("Hello " + name);
//    }

    // Note: Ensure that the [PRIVATE_KEY_FILENAME].json has read
    // permissions set.
//    FirebaseOptions options = new FirebaseOptions.Builder()
//            .setServiceAccount(getServletContext().getResourceAsStream("/WEB-INF/[PRIVATE_KEY_FILE]"))
//            .setDatabaseUrl("https://[FIREBASE_PROJECT_ID].firebaseio.com/")
//            .build();
//
//    try {
//        FirebaseApp.getInstance();
//    }
//    catch (Exception error){
//        Log.info("doesn't exist...");
//    }
//
//    try {
//        FirebaseApp.initializeApp(options);
//    }
//    catch(Exception error){
//        Log.info("already exists...");
//    }
//
//    // As an admin, the app has access to read and write all data, regardless of Security Rules
//    DatabaseReference ref = FirebaseDatabase
//            .getInstance()
//            .getReference("todoItems");
//
//    // This fires when the servlet first runs, returning all the existing values
//    // only runs once, until the servlet starts up again.
//    ref.addListenerForSingleValueEvent(new ValueEventListener() {
//
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            Object document = dataSnapshot.getValue();
//            Log.info("new value: "+ document);
//
}
