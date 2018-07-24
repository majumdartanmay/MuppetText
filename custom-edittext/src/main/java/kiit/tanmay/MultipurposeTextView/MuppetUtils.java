package kiit.tanmay.MultipurposeTextView;


import android.support.annotation.NonNull;
import android.util.Log;

public class MuppetUtils {
    private OnGetPermissionResult onGetPermissionResult;
    private OnGetContact onGetContact;
    private static MuppetUtils instance;
    private HandleDataTypeInterface handleDataTypeInterface;

    private MuppetUtils(){ }

    public static MuppetUtils getInstance() {
        if(instance == null) {
            synchronized (MuppetUtils.class) {
                instance = new MuppetUtils() ;
            }
        }
        return instance;
    }

    public static void DisplayLog(String s ) {
        Log.i(MuppetUtils.class.getSimpleName() , s == null ? "null input in log" : s);
    }

    public MuppetUtils setOnGetPermissionResult(OnGetPermissionResult onGetPermissionResult) {
        this.onGetPermissionResult = onGetPermissionResult;
        return instance;
    }

    public OnGetPermissionResult getOnGetPermissionResult() {
        return onGetPermissionResult;
    }

    public OnGetContact getOnGetContact() {
        return onGetContact;
    }

    public MuppetUtils setOnGetContact(OnGetContact onGetContact) {
        this.onGetContact = onGetContact;
        return instance;
    }

    public MuppetUtils setHandleDataTypeInterface(HandleDataTypeInterface handleDataTypeInterface) {
        this.handleDataTypeInterface = handleDataTypeInterface;
        return instance;
    }

    public HandleDataTypeInterface getHandleDataTypeInterface() {
        return handleDataTypeInterface;
    }

    public interface OnGetPermissionResult {
        void getPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
    }

    public interface OnGetContact{
        void getContactNumber(String number) ;
    }
}
