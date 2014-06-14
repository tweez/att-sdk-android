package com.att.aabsampleapp;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.att.api.aab.manager.AabManager;
import com.att.api.aab.service.Contact;
import com.att.api.aab.service.ContactResultSet;
import com.att.api.aab.service.Group;
import com.att.api.aab.service.GroupResultSet;
import com.att.api.aab.service.PageParams;
import com.att.api.aab.service.SearchParams;
import com.att.api.error.AttSdkError;
import com.att.sdk.listener.AttSdkListener;



public class ContactList extends Activity implements OnClickListener{
	
	private AabManager aabManager;
	private PageParams pageParams;
	private SearchParams searchParams;
	private ContactResultSet contactResultSet;
	private ContactsAdapter adapter;
	private ListView ContactsListView;
	private Contact[] contactsList;
	private Group[] groupList;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_contacts);
				
		ContactsListView = (ListView) findViewById(R.id.contactsListViewItem);
		aabManager = new AabManager(Config.fqdn, Config.authToken, new getContactsListener());		
		aabManager.GetContacts("", pageParams, searchParams);
		
		setupContactListListener();
		
		ContactsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Contact ctcResult = ((Contact)ContactsListView.getItemAtPosition(position));
				
				CharSequence popUpList[] = new CharSequence[] {"Delete Contact", "Update Contact", "Add to group", "Show Groups of the Contact"};
				popUpActionList(popUpList, ctcResult, position);
				return true;
			}
		});
	}
	
	public void popUpActionList(final CharSequence popUpList[],
			final Contact contact, int position) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Contact Options");
		builder.setItems(popUpList, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int options) {
				switch (options) {
				case 0: //Delete Contact
						deleteContact(contact);
						break;
				case 1: //Update Contact
						updateContact(contact);
						break;
				case 2 : //Add Contact to a Group
						addContactToGroup(contact);
						break;
				case 3 : //Get Contact Groups
						getContactGroups(contact);
						break;
				
			default:
					break;
				}
			}
		});
		builder.show();
	}
	
	public void getContactGroups(Contact contact) {
		Intent i = new Intent(ContactList.this, ContactGroupList.class);
		i.putExtra("contactId", contact.getContactId());
		startActivity(i);
	}
	
	public void getGroups() {
		
	
	}
	
	public void  addContactToGroup(Contact contact) {
		
		aabManager = new AabManager(Config.fqdn,  Config.authToken,new getGroupsListener());
		pageParams = new PageParams("ASC", "groupName", "25", "0");

		aabManager.GetGroups(pageParams, null);
		
		
		 
		/*Intent i = new Intent(AllContacts.this, GroupList.class);
		i.putExtra("groupId", "-1");
		i.putExtra("contactId", contact.getContactId());
		startActivity(i);*/
	}
	
	
	public void updateContact(Contact contact) {
		String contactId ;
		contactId = contact.getContactId();
		Intent i = new Intent(ContactList.this, ContactDetails.class);
		i.putExtra("contactId", contactId);
		startActivity(i);
	}
	
	public void deleteContact(final Contact delcontact) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Delete Contact");
		builder.setMessage("Do you want to delete the Contact :  " + delcontact.getFirstName() + "?" );
		
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String deleteContactID;
				deleteContactID = delcontact.getContactId();
				aabManager = new AabManager(Config.fqdn, Config.authToken, new deleteContactListener());
				aabManager.DeleteContact(deleteContactID);			
			}
			
		});
		
		builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
      	  public void onClick(DialogInterface dialog, int whichButton) {
      		  dialog.cancel();
      	  }
      }); 
		builder.create();	
		builder.show();
		
	}

	private class deleteContactListener implements AttSdkListener {

		@Override
		public void onSuccess(Object response) {
			String result = (String) response;
		
			aabManager = new AabManager(Config.fqdn, Config.authToken,new getContactsListener());
			pageParams = new PageParams("ASC", "firstName", "10", "0");
			aabManager.GetContacts("shallow", pageParams, searchParams);
		
			Log.i("deleteContactAPI onSuccess", result);
		}

		@Override
		public void onError(AttSdkError error) {
			Log.i("deleteContactAPI on error", "onError");
		}
	
	}

	
	public void refreshContactList(ListView ContactsListView) {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.all_contacts, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		
	}
	
	private class getContactsListener implements AttSdkListener {

		@Override
		public void onSuccess(Object response) {
			contactResultSet = (ContactResultSet) response;
			if (null != contactResultSet && null != contactResultSet.getContacts()
				&& contactResultSet.getContacts().length > 0) {
				
				contactsList = contactResultSet.getContacts();		
				adapter = new ContactsAdapter(getApplicationContext(),contactsList);
				ContactsListView.setAdapter(adapter);
				adapter.notifyDataSetChanged();

			}

		}

		@Override
		public void onError(AttSdkError error) {
			Log.i("getContactsAPI on error", "onError");

		}
	}
	
	public void setupContactListListener() {
		ContactsListView.setOnItemClickListener(new OnItemClickListener() {
			private String contactId;
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				contactId = ((Contact)ContactsListView.getItemAtPosition(position)).getContactId().toString();
				
				Intent intent = new Intent(ContactList.this, ContactDetails.class);
				intent.putExtra("contactId", contactId);
				startActivity(intent);				
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		aabManager = new AabManager(Config.fqdn, Config.authToken,new getContactsListener());
		pageParams = new PageParams("ASC", "firstName", "25", "0");
		//aabManager.GetContacts("shallow", pageParams, searchParams);
		aabManager.GetContacts("", pageParams, searchParams);
	
	}
	
	private class getGroupsListener implements AttSdkListener {
		public GroupResultSet groupResultSet;
		

		@Override
		public void onSuccess(Object response) {
			groupResultSet = (GroupResultSet) response;
			if (null != groupResultSet) {
				 groupList = groupResultSet.getGroups();
				 
				 CharSequence[] groupNameArray = new CharSequence [groupList.length];
				 final ArrayList<Integer>  mSelectedItems = new ArrayList<Integer>(); 
				 for(int i = 0; i < groupList.length; ++i) {
					 groupNameArray[i] = groupList[i].getGroupName();
				 }
				 AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
				 builder.setTitle("Available Groups");
				 builder.setMultiChoiceItems(groupNameArray, null, new DialogInterface.OnMultiChoiceClickListener() {
					 @Override
		             public void onClick(DialogInterface dialog, int which,
		                     boolean isChecked) {
						 if (isChecked) {
							 mSelectedItems.add(which);
						 }
						 else if (mSelectedItems.contains(which)) {
		                     mSelectedItems.remove(Integer.valueOf(which));
		                 }
					 }
				 }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
		             @Override
		             public void onClick(DialogInterface dialog, int id) {
		                 // User clicked OK, so save the mSelectedItems results somewhere
		                 // or return them to the component that opened the dialog
		                 
		             }
		         })
		         .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		             @Override
		             public void onClick(DialogInterface dialog, int id) {
		             }
		         });
				 
				  builder.create();
			}
		}

		@Override
		public void onError(AttSdkError error) {
			Log.i("getGroupsAPI on error", "onError");

		}
	}
}
