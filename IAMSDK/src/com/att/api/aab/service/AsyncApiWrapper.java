package com.att.api.aab.service;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.att.api.error.InAppMessagingError;
import com.att.api.error.Utils;
import com.att.api.immn.listener.ATTIAMListener;
import com.att.api.rest.RESTException;

public class AsyncApiWrapper {
	
	private ATTIAMListener iamListener;
	private AABService aabSrvc;
	
	public AsyncApiWrapper(AABService aabService, ATTIAMListener iamListener) {
		this.iamListener = iamListener;
		this.aabSrvc = aabService;
	}
	
	public void GetContacts(GetContactParams contactParams) {
		GetContactsTask getContactsTask = new GetContactsTask();
		getContactsTask.execute(contactParams);
	}
	
	public class  GetContactsTask extends AsyncTask<GetContactParams, Void, ContactResultSet> {
		@Override
		protected ContactResultSet doInBackground(GetContactParams... params) {
			ContactResultSet contactResultSet = null;
			InAppMessagingError errorObj = new InAppMessagingError();

			try {
				contactResultSet = aabSrvc.getContacts(
								params[0].getxFields(), //xFields
							    params[0].getPageParams(), //PageParams
							    params[0].getSearchParams() //SearchParams 
							    );
			} catch (RESTException e) {
				errorObj = Utils.CreateErrorObjectFromException( e );
				if (null != iamListener) {
					iamListener.onError(errorObj);
				}
			} catch (ParseException e) {
				errorObj = new InAppMessagingError(e.getMessage());
				if (null != iamListener) {
					iamListener.onError(errorObj);
				}
			}
			
			return contactResultSet;
		}

		@Override
		protected void onPostExecute(ContactResultSet contactResultSet) {
			super.onPostExecute(contactResultSet);
			if( null != contactResultSet ) {
				if (null != iamListener) {
					iamListener.onSuccess(contactResultSet);
				}
			}			
		}		
	}
	
	public void GetContact(String contactId, String xFields) {
		GetContactTask getContactTask = new GetContactTask();
		getContactTask.execute(contactId, xFields);
	}
	
	public class  GetContactTask extends AsyncTask<String, Void, ContactWrapper> {
		@Override
		protected ContactWrapper doInBackground(String... params) {
			ContactWrapper result = null;
			InAppMessagingError errorObj = new InAppMessagingError();

			try {
				result = aabSrvc.getContact(
								params[0], //contactId
							    params[1] //xFields 
							    );
			} catch (RESTException e) {
				errorObj = Utils.CreateErrorObjectFromException( e );
				if (null != iamListener) {
					iamListener.onError(errorObj);
				}
			} catch (ParseException e) {
				errorObj = new InAppMessagingError(e.getMessage());
				if (null != iamListener) {
					iamListener.onError(errorObj);
				}
			}
			
			return result;
		}

		@Override
		protected void onPostExecute(ContactWrapper result) {
			super.onPostExecute(result);
			if( null != result ) {
				if (null != iamListener) {
					iamListener.onSuccess(result);
				}
			}			
		}		
	}
	
	public void GetContactGroups(String contactId, String pageOrder, String pageOrderBy, String pageLimit, String pageOffset) {
		GetContactGroupsTask getContactGroupsTask = new GetContactGroupsTask();
		getContactGroupsTask.execute(contactId, pageOrder, pageOrderBy, pageLimit, pageOffset);
	}
	
	public class  GetContactGroupsTask extends AsyncTask<String, Void, GroupResultSet> {
		@Override
		protected GroupResultSet doInBackground(String... params) {
			GroupResultSet result = null;
			InAppMessagingError errorObj = new InAppMessagingError();

			try {
				PageParams pageParams = new PageParams(params[1], params[2], params[3], params[4]);
				result = aabSrvc.getContactGroups(
								params[0], //contactId
								pageParams //pageParams 
							    );
			} catch (RESTException e) {
				errorObj = Utils.CreateErrorObjectFromException( e );
				if (null != iamListener) {
					iamListener.onError(errorObj);
				}
			} catch (ParseException e) {
				errorObj = new InAppMessagingError(e.getMessage());
				if (null != iamListener) {
					iamListener.onError(errorObj);
				}
			}
			
			return result;
		}

		@Override
		protected void onPostExecute(GroupResultSet result) {
			super.onPostExecute(result);
			if( null != result ) {
				if (null != iamListener) {
					iamListener.onSuccess(result);
				}
			}			
		}		
	}
}
