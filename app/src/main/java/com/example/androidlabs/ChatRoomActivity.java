package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    ArrayList<Message> chats = new ArrayList<>();

    BaseAdapter adapter;

    private ListView chatList;
    private Button sendButton;
    private Button receiveButton;
    private EditText typeChat;

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        // Get fields from the screen
        chatList = (ListView) findViewById(R.id.chatList);
        sendButton = (Button) findViewById(R.id.btnSend);
        receiveButton = (Button) findViewById(R.id.btnReceive);
        typeChat = (EditText) findViewById(R.id.typeChat);

        // Get a database
        MessageDatabaseOpenHelper dbOpener = new MessageDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();

        // Query all the results from the database
        String [] columns = {MessageDatabaseOpenHelper.COL_ID, MessageDatabaseOpenHelper.COL_CHAT,
                                    MessageDatabaseOpenHelper.COL_CHAT_TYPE};
        Cursor results = db.query(false, MessageDatabaseOpenHelper.TABLE_NAME, columns,
                null, null, null, null, null, null);

        // Output query information to LogCat
        printCursor(results);

        // Find the column indices
        int idColIndex = results.getColumnIndex(MessageDatabaseOpenHelper.COL_ID);
        int chatColIndex = results.getColumnIndex(MessageDatabaseOpenHelper.COL_CHAT);
        int chatTypeColIndex = results.getColumnIndex(MessageDatabaseOpenHelper.COL_CHAT_TYPE);

        // Iterate over the res
        // resultults, return true if there is a next item:
        results.moveToPosition(-1);
        while(results.moveToNext())
        {
            String message = results.getString(chatColIndex);
            Boolean messageType = results.getInt(chatTypeColIndex) == 1;
            long id = results.getLong(idColIndex);

            // Add the new chat to the array list
            chats.add(new Message(message, messageType, id));
        }
        

        // Create an adapter and send it to the list view
        adapter = new ChatAdapter(chats, this);
        chatList.setAdapter(adapter);



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // what happens when I click the send button
                if(typeChat.getText().toString().trim().equals("")){
                    Toast.makeText(ChatRoomActivity.this, "Please input some text...", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Get input
                    String input = typeChat.getText().toString();

                    // Add to the database and get new id
                    ContentValues cv = new ContentValues();
                    // Put string input in the COL_CHAT columnn
                    cv.put(MessageDatabaseOpenHelper.COL_CHAT, input);
                    // Put 1 to stand for true in the COL_CHAT_TYPE column
                    cv.put(MessageDatabaseOpenHelper.COL_CHAT_TYPE, 1);
                    // Insert in the database
                    long newId = db.insert(MessageDatabaseOpenHelper.TABLE_NAME, null, cv);

                    // With the new id you can create the Message object
                    Message msg = new Message(input, true, newId);

                    // Add the new chat message to the list
                    chats.add(msg);
                    // Update the list view
                    adapter.notifyDataSetChanged();

                    // Clear the editText field
                    typeChat.setText("");
                }
            }
        });

        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // what happens when I click the receive button
                if(typeChat.getText().toString().trim().equals("")){
                    Toast.makeText(ChatRoomActivity.this,"Please input some text...",Toast.LENGTH_SHORT).show();
                }
                else{
                    // Get input
                    String input = typeChat.getText().toString();

                    // Add to the database and get new id
                    ContentValues cv = new ContentValues();
                    // Put string input in the COL_CHAT columnn
                    cv.put(MessageDatabaseOpenHelper.COL_CHAT, input);
                    // Put 1 to stand for true in the COL_CHAT_TYPE column
                    cv.put(MessageDatabaseOpenHelper.COL_CHAT_TYPE, 0);
                    // Insert in the database
                    long newId = db.insert(MessageDatabaseOpenHelper.TABLE_NAME, null, cv);

                    // With the new id you can create the Message object
                    Message msg = new Message(input, false, newId);

                    // Add the new chat message to the list
                    chats.add(msg);
                    // Update the list view
                    adapter.notifyDataSetChanged();

                    // Clear the editText field
                    typeChat.setText("");
                }
            }
        });

    }

    public void printCursor(Cursor c){
        // Database Version
        Log.i("DB Version:", String.valueOf(MessageDatabaseOpenHelper.VERSION_NUM));

        // The number of columns in the cursor.
        Log.i("No of Columns:", String.valueOf(c.getColumnCount()));

        // The name of the columns in the cursor.
        for(int i=0; i<c.getColumnCount(); i++){
            Log.i("Column " + i, c.getColumnName(i));
        }


        // The number of results in the cursor
        Log.i("Result count:", String.valueOf(c.getCount()));

        // Each row of results in the cursor.
        int idColIndex = c.getColumnIndex("_id");
        int chatColIndex = c.getColumnIndex( "CHAT" );
        int chatTypeColIndex = c.getColumnIndex("CHAT_TYPE");
        c.moveToFirst();
        while(!c.isAfterLast() ){
            Long id = c.getLong(idColIndex);
            String chat = c.getString( chatColIndex );
            String chatType = c.getString(chatTypeColIndex);

            Log.i("ID: ", String.valueOf(id));
            Log.i("Message: ", chat);
            Log.i("isSent: ", chatType);

            c.moveToNext();
        }

    }

}
