package com.lunchpals.app.activities.main.chat;

import android.content.Intent;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.lunchpals.app.R;
import com.lunchpals.app.activities.login.LoginActivity;
import com.lunchpals.app.activities.main.MainFragment;
import com.lunchpals.app.user.User;


public class MessengerFragment extends MainFragment {

    public FirebaseUser user = User.getFirebaseUser();
    private FirebaseListAdapter<ChatMessage> chatAdapter;
    public String userId;
    public TextView messageText;
    public TextView messageUser;
    public TextView messageTime;

    private ListView messageListView;


    @Override
    public void onStart() {

        if(User.isOnline(getContext())) {

            userId = user.getUid();

            messageListView = getActivity().findViewById(R.id.listofmessages);
            Button fab = getActivity().findViewById(R.id.send);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText input = getActivity().findViewById(R.id.message);

                    // Read the input field and push a new instance
                    // of ChatMessage to the Firebase database
                    FirebaseDatabase.getInstance().getReference().child("messages")
                            .push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getDisplayName())
                            );

                    // Clear the input
                    input.setText("");
                }

            });

            chatAdapter = new FirebaseListAdapter<ChatMessage>(this.getActivity(), ChatMessage.class, R.layout.message, FirebaseDatabase.getInstance().getReference().child("messages")) {
                @Override
                protected void populateView(View v, ChatMessage model, int position) {
                    // Get references to the views of message.xml
                    String userEmail = user.getEmail();
                    messageText = v.findViewById(R.id.message_text);
                    messageUser = v.findViewById(R.id.message_user);
                    messageTime = v.findViewById(R.id.message_time);
                    // Set their text
                    messageText.setText(model.getMessageText());
                    messageUser.setText(model.getMessageUser());

                    // Format the date before showing it
                    //DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                    messageTime.setText(DateFormat.format("hh:mm:ss", model.getMessageTime()));
                }

                @Override
                public void notifyDataSetChanged() {
                    scrollListViewToBottom();
                    super.notifyDataSetChanged();
                }
            };

            messageListView.setAdapter(chatAdapter);

        }

        super.onStart();

    }

    private void scrollListViewToBottom() {
        messageListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                messageListView.setSelection(chatAdapter.getCount() - 1);
            }
        });
    }







}
