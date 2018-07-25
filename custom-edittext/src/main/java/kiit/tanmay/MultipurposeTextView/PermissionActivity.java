
package kiit.tanmay.MultipurposeTextView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import kiit.tanmay.MultipurposeTextView.MuppetUtils;
import kiit.tanmay.MultipurposeTextView.Util.Constants;

import static kiit.tanmay.MultipurposeTextView.Util.Constants.PICK_CONTACT;

public class PermissionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        if(i !=null) {
            if(i.getAction().equals(Constants.PERMISSION_PHONE_CALL_ACTION))
                askForPermission();
            else if(i.getAction().equals(Constants.PICK_CONTACT_ACTION)){
                pickContact();
            }
        }
    }

    private void pickContact() {
        Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(i, PICK_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            cursor.moveToFirst();
            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String phoneNumber = cursor.getString(column) ;
            MuppetUtils.getInstance().getMuppetView().getContactNumber(phoneNumber);
        }
        finish();
    }

    public void askForPermission () {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE} , Constants.PERMISSION_PHONE_CALL);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MuppetUtils.getInstance().getMuppetView().getPermissionResult(requestCode , permissions , grantResults);
        finish();
    }
}
