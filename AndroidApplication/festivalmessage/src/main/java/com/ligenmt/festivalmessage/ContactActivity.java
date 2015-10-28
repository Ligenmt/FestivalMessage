package com.ligenmt.festivalmessage;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.ligenmt.festivalmessage.bean.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView lvContact;
    private List<Contact> contacts;
    private ArrayList<Contact> selectedContacts;
    private ArrayAdapter<Contact> mAdapter;
    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        mInflater = LayoutInflater.from(this);

        initViews();
        initList();
    }

    public void initViews() {
        lvContact = (ListView) findViewById(R.id.lv_contact);
        lvContact.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        findViewById(R.id.btn_ok).setOnClickListener(this);
        findViewById(R.id.btn_select_all).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);

    }

    public void initList() {

        contacts = getContacts();
        lvContact.setAdapter(mAdapter = new ArrayAdapter<Contact>(this, -1, contacts) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_contact, parent, false);
                }
                TextView tvName = (TextView) convertView.findViewById(R.id.tv_contact_name);
                TextView tvNumber = (TextView) convertView.findViewById(R.id.tv_contact_number);
                tvName.setText(getItem(position).getName());
                tvNumber.setText(getItem(position).getNumber());

                CheckBox cb = (CheckBox) convertView.findViewById(R.id.cb_contact);
                cb.setChecked(getItem(position).isSelected());
                return convertView;
            }
        });

        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CheckBox cbContact = (CheckBox) view.findViewById(R.id.cb_contact);
                cbContact.setChecked(!cbContact.isChecked());
                contacts.get(position).setIsSelected(cbContact.isChecked());
            }
        });

    }
    public List<Contact> getContacts() {
    //        Uri uri = ContactsContract.Contacts.CONTENT_URI;
    //        String[] projection = new String[]{
    //                ContactsContract.Contacts._ID,
    //                ContactsContract.Contacts.DISPLAY_NAME,
    //                ContactsContract.Contacts.PHOTO_ID
    //        };

        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        List<Contact> contacts = new ArrayList<>();
        while (cursor.moveToNext()) {
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace("-","").replace(" ","");
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            contacts.add(new Contact(name, phoneNumber));
        }
        cursor.close();
        return contacts;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                selectedContacts = new ArrayList<>();
                for (Contact contact : contacts) {
                    if(contact.isSelected()) {
                        selectedContacts.add(contact);
                    }
                }
                Intent data = new Intent();
                data.putParcelableArrayListExtra("contacts", selectedContacts);
                setResult(Activity.RESULT_OK, data);
                finish();
                break;
            case R.id.btn_select_all:
                for(Contact contact : contacts) {
                    contact.setIsSelected(true);
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_back:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
        }
    }
}
