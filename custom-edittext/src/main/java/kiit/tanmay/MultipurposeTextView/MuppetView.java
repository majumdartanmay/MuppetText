package kiit.tanmay.MultipurposeTextView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.example.custom_edittext.R;
import kiit.tanmay.MultipurposeTextView.Util.Constants;
import kiit.tanmay.MultipurposeTextView.Util.PermissionActivity;

@SuppressLint("AppCompatCustomView")
public class MuppetView extends EditText implements MuppetUtils.OnGetPermissionResult , MuppetUtils.OnGetContact  {
    private boolean editTextMode, numberMode, phoneNumberMode, contactPickerMode;
    public MuppetView(Context context) {
        super(context);
    }


    public MuppetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.MuppetView, 0, 0);
        setAttributes(typedArray);
        typedArray.recycle();
        // more stuff
    }

    public void setEditTextFont(String fontName) {

        if (!TextUtils.isEmpty(fontName)) {
           try{
               Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), fontName);
               setTypeface(typeface);
           }catch (Exception ex) {
               MuppetUtils.DisplayLog(ex.toString());
           }
        }
    }


    public void showKeyboard() {
        Log.i(MuppetView.class.getSimpleName(), numberMode + "");
        final InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                requestFocus();
                setInputType(numberMode ? InputType.TYPE_CLASS_NUMBER : (phoneNumberMode
                        ? InputType.TYPE_CLASS_PHONE : InputType.TYPE_CLASS_TEXT));
              if(imm != null) {
                  imm.showSoftInput(MuppetView.this, 0);
              }
            }
        }, 100);
    }


    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
        }
    }


    public void setEditTextMode(boolean mode) {
        this.editTextMode = mode;
        setCursorVisible(editTextMode);
        setLongClickable(editTextMode);
        setClickable(phoneNumberMode || editTextMode);
        setFocusable(editTextMode);
        setSelected(phoneNumberMode || editTextMode);
        //setKeyListener(null);
        setBackgroundResource(android.R.color.transparent);
    }

    public void setAttributes(TypedArray attributes) {
        setEditTextFont(attributes.hasValue(R.styleable.MuppetView_fontName) ? attributes.getString(R.styleable.MuppetView_fontName) : "");
        setEditTextMode(attributes.getBoolean(R.styleable.MuppetView_editTextMode, false));
        setEditTextNumberMode(attributes.getBoolean(R.styleable.MuppetView_numberMode, false));
        setContactPickerMode(attributes.getBoolean(R.styleable.MuppetView_contactPickerMode, false));
        setPhoneNumberMode(attributes.getBoolean(R.styleable.MuppetView_phoneNumberMode, false));
        setClickListener();
    }

    public void setContactPickerMode(boolean mode) {
        this.contactPickerMode = mode;
        this.numberMode = !mode && numberMode;
        this.editTextMode = !mode && editTextMode;
        this.phoneNumberMode = !mode && phoneNumberMode;
        MuppetUtils.DisplayLog(editTextMode+" "+mode);
        setEditTextMode(editTextMode);
        setPhoneNumberMode(numberMode);
        MuppetUtils.getInstance().setOnGetContact(this) ;
    }

    private void setClickListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(MuppetView.class.getSimpleName(), "called " + editTextMode);
                if (editTextMode && !contactPickerMode) {
                    if (!phoneNumberMode) {
                        showKeyboard();
                    } else {
                        makePhoneCall();
                    }
                } else {
                   if(phoneNumberMode && ! contactPickerMode) {
                       hideKeyboard();
                       makePhoneCall();
                   }else if(contactPickerMode) {
                       pickYouContact();
                   }
                }
            }
        });
    }

    private void pickYouContact() {
        MuppetUtils.getInstance().setOnGetPermissionResult(this).setOnGetContact(this);
        Intent i = new Intent(getContext() , PermissionActivity.class);
        i.setAction(Constants.PICK_CONTACT_ACTION);
        getContext().startActivity(i);
    }

    @SuppressLint("MissingPermission")
    private  void makePhoneCall() {
        MuppetUtils.getInstance().setOnGetPermissionResult(this);
        if (permissionForCall()) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getText().toString()));
            getContext().startActivity(intent);
        }else {
            MuppetUtils.getInstance().setOnGetPermissionResult(this);
            Intent i = new Intent(getContext() , PermissionActivity.class);
            i.setAction(Constants.PERMISSION_PHONE_CALL_ACTION);
            getContext().startActivity(i);
        }
    }

    private  boolean permissionForCall() {
        return ActivityCompat.checkSelfPermission((getContext()), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
    }



    public void setEditTextNumberMode(boolean editTextNumberMode) {
        this.editTextMode = editTextNumberMode;
        this.numberMode = editTextNumberMode;
        this.phoneNumberMode = false;
    }

    public boolean isPhoneNumberMode() {
        return phoneNumberMode;
    }

    public void setPhoneNumberMode(boolean phoneNumberMode) {
        this.editTextMode = ! phoneNumberMode && editTextMode;
        this.numberMode = ! phoneNumberMode && numberMode;
        this.phoneNumberMode = phoneNumberMode;
        setEditTextMode(! phoneNumberMode);
    }

    @Override
    public  void getPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int index = 0 ;
        switch (requestCode) {
            case Constants.PERMISSION_PHONE_CALL :
                for (String permission : permissions ) {
                    if(permission.equals(Manifest.permission.CALL_PHONE) && grantResults[index] == PackageManager.PERMISSION_GRANTED)
                        makePhoneCall();
                    index ++;
                }
        }
    }

    @Override
    public void getContactNumber(String number) {
        MuppetUtils.DisplayLog(number);
        HandleDataTypeInterface handleDataTypeInterface = MuppetUtils.getInstance().getHandleDataTypeInterface();
        if(handleDataTypeInterface != null) {
            handleDataTypeInterface.onGetContactString(number , this);
        }
    }


}
