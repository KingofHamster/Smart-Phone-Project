package cs.hku.hkutreehole;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.smailnet.emailkit.Draft;
import com.smailnet.emailkit.EmailKit;

import java.util.Properties;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class EmailService extends IntentService {

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_SEND_EMAIL = "cs.hku.myapplication.action.SEND_EMAIL";
    private static final String ACTION_BAZ = "cs.hku.myapplication.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "cs.hku.myapplication.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "cs.hku.myapplication.extra.PARAM2";
    private static final String TAG = "Hamster";

    public EmailService() {
        super("EmailService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionSendEmail(Context context, String param1, String param2) {
        Intent intent = new Intent(context, EmailService.class);
        intent.setAction(ACTION_SEND_EMAIL);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, EmailService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SEND_EMAIL.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionSendEmail(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSendEmail(String emailAdress, String emailVerificationCode) {
        // TODO: Handle action Foo
        // ?????????????????????
        //???????????????
        EmailKit.initialize(this);

//????????????????????????????????????
        EmailKit.Config config = new EmailKit.Config()
                .setMailType(EmailKit.MailType.$163)     //????????????????????????????????????????????????
                .setAccount("hku_treehole@163.com")             //???????????????
                .setPassword("UAGVCMBMJRYDKNMD");                  //??????????????????

//????????????????????????
        Draft draft = new Draft()
                .setNickname("Hku Tree Hole")                      //???????????????
                .setTo(emailAdress)                        //???????????????
                .setSubject("Verification Code - HKU Tree Hole")             //????????????
                .setText("Your Verification Code is "+emailVerificationCode);                //????????????

//??????SMTP??????????????????
        EmailKit.useSMTPService(config)
                .send(draft, new EmailKit.GetSendCallback() {
                    @Override
                    public void onSuccess() {
                        Log.i(TAG, "???????????????");
                    }

                    @Override
                    public void onFailure(String errMsg) {
                        Log.i(TAG, "????????????????????????" + errMsg);
                    }
                });
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
