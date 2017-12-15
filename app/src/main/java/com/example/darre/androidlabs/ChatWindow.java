package com.example.darre.androidlabs;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.darre.androidlabs.ChatDatabaseHelper.name;

public class ChatWindow extends Activity {
    protected static final String ACTIVITY_NAME = "ChatWindow";
    ListView lv;
    EditText et;
    Button bt;
    final ArrayList<String> data = new ArrayList<String>();

    SQLiteDatabase db;

    Cursor cursor;
    private Boolean isLandscape;
    private FrameLayout landscapeFrameLayout;
    private ChatAdapter chatAdapter;

    private ChatDatabaseHelper chatDatabaseHelper;
    int messageIndex;
    private int requestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        chatDatabaseHelper = new ChatDatabaseHelper(this);
        db = chatDatabaseHelper.getWritableDatabase();


        lv = (ListView) findViewById(R.id.thelist);
        et = (EditText) findViewById(R.id.massage);
        bt = (Button) findViewById(R.id.sendbuttom);
        landscapeFrameLayout = (FrameLayout) findViewById(R.id.landscapeFrameLayout);

        if (landscapeFrameLayout == null) {
            isLandscape = false;
            Log.i(ACTIVITY_NAME, "The phone is on portrait layout.");

        } else {
            isLandscape = true;
            Log.i(ACTIVITY_NAME, "The phone is on landscape layout.");
        }


        chatAdapter = new ChatAdapter(this);

        lv.setAdapter(chatAdapter);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = et.getText().toString();
                data.add(input);
                chatAdapter.notifyDataSetChanged();
                et.setText("");

                ContentValues newData = new ContentValues();
                newData.put(ChatDatabaseHelper.KEY_MESSAGE, input);
                db.insert(name, "", newData);
                refreshActivity();
            }
        });

        cursor = db.rawQuery("select * from MyTable", null);
        final Intent intent = new Intent(this, MessageDetails.class);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String message = chatAdapter.getItem(position);
                long idInDb = chatAdapter.getItemId(position);

                Bundle bundle = new Bundle();
                bundle.putLong("id", idInDb);
                bundle.putString("message", message);
                bundle.putBoolean("isLandscape", isLandscape);

                if (isLandscape == true) {
                    MessageFragment messageFragment = new MessageFragment();

                    messageFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    //remove previous fragment
                    if (fragmentManager.getBackStackEntryCount() > 0) {
                        FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
                        fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.landscapeFrameLayout, messageFragment).addToBackStack(null).commit();
                } else {
                    intent.putExtra("bundle", bundle);
                    startActivityForResult(intent, requestCode);
                }
            }
        });

        cursor.moveToFirst();//resets the iteration of results

        while (!cursor.isAfterLast()) {
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:"
                    + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            data.add(cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
            cursor.moveToNext();
        }
        Log.i(ACTIVITY_NAME, "Cursor's  column count =" + cursor.getColumnCount());

        for (int i = 0; i < cursor.getColumnCount(); i++) {
            System.out.println(cursor.getColumnName(i));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode && data != null) {
            Long id = data.getLongExtra("id", -1);
            db.delete(name, ChatDatabaseHelper.KEY_ID + "=" + id, null);
            refreshActivity();
        }
    }

    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {

            return data.size();
        }

        public String getItem(int position) {

            return data.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if (position % 2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText(getItem(position)); // get the string at position
            return result;
        }

        public long getItemId(int position) {
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

    public void refreshActivity() {
        finish();
        Intent intent = getIntent();
        startActivity(intent);
    }
}









