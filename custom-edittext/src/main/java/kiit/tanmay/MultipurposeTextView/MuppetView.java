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
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import com.example.custom_edittext.R;
import kiit.tanmay.MultipurposeTextView.Util.Constants;

@SuppressLint("AppCompatCustomView")
public class MuppetView extends EditText {
    private boolean editTextMode, numberMode, phoneNumberMode;
    private CharSequence beforeCharSequence ;
    public MuppetView(Context context) {
        super(context);
    }


    public MuppetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.MuppetView, 0, 0);
        setAttributes(typedArray);
        typedArray.recycle();
    }

    public void setEditTextFont(String fontName) {

        if (!TextUtils.isEmpty(fontName)) {
           try{
               Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), fontName);
               setTypeface(typeface);
           }catch (Exception ex) {
               ex.printStackTrace();
           }
        }
    }



    public void setEditTextMode(boolean mode) {
        this.editTextMode = mode;
        setEnabled(mode || phoneNumberMode);
        setFocusable(mode || phoneNumberMode);
    }

    private void setAttributes(TypedArray attributes) {
        setEditTextFont(attributes.hasValue(R.styleable.MuppetView_fontName) ? attributes.getString(R.styleable.MuppetView_fontName) : "");
        setEditTextMode(attributes.getBoolean(R.styleable.MuppetView_editTextMode, false));
        setEditTextNumberMode(attributes.getBoolean(R.styleable.MuppetView_numberMode, false));
        setContactPickerMode(attributes.getBoolean(R.styleable.MuppetView_contactPickerMode, false));
        setPhoneNumberMode(attributes.getBoolean(R.styleable.MuppetView_phoneNumberMode, false));
        setOnTextChangeListener();
    }

    public void setContactPickerMode(boolean mode) {
        //this.contactPickerMode = mode;
        this.numberMode = !mode && numberMode;
        this.editTextMode = !mode && editTextMode;
        this.phoneNumberMode = !mode && phoneNumberMode;
        setPhoneNumberMode(numberMode);
        MuppetUtils.getInstance().setMuppetView(this);
    }

    private void setOnTextChangeListener() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                beforeCharSequence = charSequence;
            }

            @Override
            public void onTextChanged(CharSequence editable, int i, int i1, int i2) {
                HandleDataTypeInterface handleDataTypeInterface = MuppetUtils.getInstance().getHandleDataTypeInterface();
                if(handleDataTypeInterface != null  && ! beforeCharSequence.equals(editable)){
                    handleDataTypeInterface.onTextChanged(editable.toString() , 0, 0,0,MuppetView.this);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

//    private void pickYouContact() {
//      //  MuppetUtils.getInstance().setOnGetPermissionResult(this).setOnGetContact(this);
//        MuppetUtils.getInstance().setMuppetView(this);
//        Intent i = new Intent(getContext() , PermissionActivity.class);
//        i.setAction(Constants.PICK_CONTACT_ACTION);
//        getContext().startActivity(i);
//    }

    @SuppressLint("MissingPermission")
    private  void makePhoneCall() {
        MuppetUtils.getInstance().setMuppetView(this);
        if (permissionForCall()) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getText().toString()));
            getContext().startActivity(intent);
        }else {
            MuppetUtils.getInstance().setMuppetView(this);
            Intent i = new Intent(getContext() , PermissionActivity.class);
            i.setAction(Constants.PERMISSION_PHONE_CALL_ACTION);
            getContext().startActivity(i);
        }
    }

    private  boolean permissionForCall() {
        return ActivityCompat.checkSelfPermission((getContext()), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
    }



    public boolean setEditTextNumberMode(boolean editTextNumberMode) {
        this.editTextMode = editTextNumberMode;
        this.numberMode = editTextNumberMode;
        this.phoneNumberMode = false;
        return editTextNumberMode ? invokeNumberpad() : invokeTextPad();
    }

    private boolean invokeNumberpad(){
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        return true;
    }

    private boolean invokeTextPad(){
        setInputType(InputType.TYPE_CLASS_TEXT);
        return  true;
    }

    public boolean isPhoneNumberMode() {
        return phoneNumberMode;
    }

    public void setPhoneNumberMode(boolean phoneNumberMode) {
//        this.editTextMode = ! phoneNumberMode && editTextMode;
//        this.numberMode = ! phoneNumberMode && numberMode;
//        this.phoneNumberMode = phoneNumberMode;
//        setEditTextMode(! phoneNumberMode);
        this.phoneNumberMode = phoneNumberMode;
    }


    protected void getPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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


    protected void getContactNumber(String number) {
        HandleDataTypeInterface handleDataTypeInterface = MuppetUtils.getInstance().getHandleDataTypeInterface();
        if(handleDataTypeInterface != null) {
            handleDataTypeInterface.onGetContactString(number , this);
        }
    }


}
