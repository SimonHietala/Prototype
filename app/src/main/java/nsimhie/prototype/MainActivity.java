package nsimhie.prototype;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import nsimhie.prototype.Fragments.AboutFragment;
import nsimhie.prototype.Fragments.CreateTagFragment;
import nsimhie.prototype.Fragments.CurrentTaskFragment;
import nsimhie.prototype.Fragments.EraseTagFragment;
import nsimhie.prototype.Fragments.HelpFragment;
import nsimhie.prototype.Fragments.HistoryFragment;
import nsimhie.prototype.Fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String USER = "Jürgen";
    private NfcAdapter nfcAdapter;
    private static CurrentTaskFragment currentTaskFragment = new CurrentTaskFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Menu stuff.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Nfc related
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "onResume()", Toast.LENGTH_SHORT).show();

        enableForegroundDispatchSystem();

    }

    @Override
    protected void onPause() {
        super.onPause();
        disableForegroundDispatchSystem();
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }

        //Pops the backstack
        else if (getFragmentManager().getBackStackEntryCount() > 0)
        {
            //Gets the current fragment and calls setTitle with the fragment
            if(getFragmentManager().getBackStackEntryCount() > 1)
            {
                FragmentManager.BackStackEntry backEntry = getFragmentManager().getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 2);
                String str=backEntry.getName();
                Fragment fragment = getFragmentManager().findFragmentByTag(str);
                setTitle(fragment);
            }

            else
            {
                setTitle(getString(R.string.app_name));
            }

            getFragmentManager().popBackStack();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Fragment fragment = new SettingsFragment();
            replaceFragment(fragment);
            setTitle(getString(R.string.menu_settings));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_create_tag)
        {
            setTitle(getString(R.string.menu_create_tag));
            replaceFragment(new CreateTagFragment());
        }

        else if (id == R.id.nav_erase_tag)
        {
            setTitle(getString(R.string.menu_erase_tag));
            replaceFragment(new EraseTagFragment());
        }

        else if (id == R.id.nav_settings)
        {
            setTitle(getString(R.string.menu_settings));
            replaceFragment(new SettingsFragment());
        }

        else if (id == R.id.nav_current_task)
        {
            if(currentTaskFragment.isCounting()) {
                setTitle(getString(R.string.menu_task));
                replaceFragment(currentTaskFragment);
            }
        }

        else if (id == R.id.nav_history)
        {
            setTitle(getString(R.string.menu_history));
            replaceFragment(new HistoryFragment(currentTaskFragment));
        }

        else if (id == R.id.nav_statistics)
        {
            setTitle(getString(R.string.menu_statistics));
        }

        else if (id == R.id.nav_about)
        {
            setTitle(getString(R.string.menu_about));
            replaceFragment(new AboutFragment());
        }

        else if (id == R.id.nav_help)
        {
            setTitle(getString(R.string.menu_help));
            replaceFragment(new HelpFragment());
        }

        //Closes the menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    //Function that changes the fragment. And adds to the backstack.
    public void replaceFragment (Fragment fragment){
        String backStateName =  fragment.getClass().getName();
        String fragmentTag = backStateName;
        FragmentManager manager = getFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        //fragment not in back stack, create it.
        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null){
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.frame_container, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    private void setTitle(Fragment fragment)
    {
        String backStateName =  fragment.getClass().getName();
        if(backStateName.equals(new CreateTagFragment().getClass().getName()))
        {
            setTitle(getString(R.string.menu_create_tag));
        }

        else if(backStateName.equals(new EraseTagFragment().getClass().getName()))
        {
            setTitle(getString(R.string.menu_erase_tag));
        }

        else if(backStateName.equals(new HelpFragment().getClass().getName()))
        {
            setTitle(getString(R.string.menu_help));
        }

        else if(backStateName.equals(new HistoryFragment(currentTaskFragment).getClass().getName()))
        {
            setTitle(getString(R.string.menu_history));
        }

        else if(backStateName.equals(new CurrentTaskFragment().getClass().getName()))
        {
            setTitle(getString(R.string.menu_current_task));
        }

        else if(backStateName.equals(new SettingsFragment().getClass().getName()))
        {
            setTitle(getString(R.string.menu_settings));
        }

        else if(backStateName.equals(new AboutFragment().getClass().getName()))
        {
            setTitle(getString(R.string.menu_about));
        }
    }

     /*
        functions that handles the nfc-stuff.
     */

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        if(intent.hasExtra(NfcAdapter.EXTRA_TAG))
        {
            //Toast.makeText(this, "NfcIntent!", Toast.LENGTH_SHORT).show();
            String createTag = CreateTagFragment.class.getName();
            String eraseTag = EraseTagFragment.class.getName();

            CreateTagFragment createTagFragment = (CreateTagFragment)getFragmentManager().findFragmentByTag(createTag);
            EraseTagFragment eraseTagFragment = (EraseTagFragment)getFragmentManager().findFragmentByTag(eraseTag);

            //Write to the tags
            if(createTagFragment != null && createTagFragment.isVisible() && createTagFragment.getButtonState())
            {
                //Tag related
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                NdefMessage ndefMessage = createNdefMessage(writeJson().toString() + "");

                writeNdefMessage(tag, ndefMessage);

                //Toast.makeText(this, "Tag written!", Toast.LENGTH_SHORT).show();
            }

            //Erase the tag
            else if (eraseTagFragment != null && eraseTagFragment.isVisible() && eraseTagFragment.getButtonState())
            {
                // continue with delete
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                NdefMessage ndefMessage = createNdefMessage("");
                writeNdefMessage(tag, ndefMessage);
            }

            //Read the tags
            else
            {

                Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

                if(parcelables != null && parcelables.length > 0)
                {
                    String json = readTextFromMessage((NdefMessage) parcelables[0]);
                    setTitle(getString(R.string.menu_task));

                    currentTaskFragment.setArgumentsOwn(json);
                    replaceFragment(currentTaskFragment);
                }

                else
                {
                    Toast.makeText(this, "No NDEF message found!", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    private String readTextFromMessage(NdefMessage ndefMessage)
    {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if(ndefRecords != null && ndefRecords.length > 0)
        {
            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            if(tagContent != null)
            {
                return tagContent;
            }
        }

        else
        {
            Toast.makeText(this, "No NDEF records found!", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    public String getTextFromNdefRecord (NdefRecord ndefRecord)
    {
        String tagContent = null;
        try
        {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            tagContent = new String(payload, languageSize + 1, payload.length - languageSize - 1, textEncoding);

        }
        catch (UnsupportedEncodingException e)
        {
            Log.e("getTextFromNdefRecord", e.getMessage());
        }

        catch (IndexOutOfBoundsException e)
        {
            Toast.makeText(this, "No payload on tag.", Toast.LENGTH_SHORT).show();
            Log.e("getTextFromNdefRecord", e.getMessage());
            return null;
        }

        return tagContent;
    }

    private void enableForegroundDispatchSystem()
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,0);
        IntentFilter[] intentFilters = new IntentFilter[]{};

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    private void disableForegroundDispatchSystem()
    {
        nfcAdapter.disableForegroundDispatch(this);
    }

    public void formatTag(Tag tag, NdefMessage ndefMessage)
    {
        try {
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if(ndefFormatable == null)
            {
                Toast.makeText(this, "Tag is not ndef formatable!", Toast.LENGTH_LONG).show();
                return;
            }

            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();

            Toast.makeText(this, "Tag is formated!", Toast.LENGTH_SHORT).show();
        }

        catch(Exception e)
        {
            Log.e("formTag", e.getMessage());
        }
    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage)
    {
        try
        {
            if(tag == null)
            {
                Toast.makeText(this, "Tag object can not be null.", Toast.LENGTH_LONG).show();
                return;
            }

            Ndef ndef = Ndef.get(tag);

            if(ndef == null)
            {
                //Format tag with the ndef format and writes the message
                formatTag(tag, ndefMessage);
            }

            else
            {
                ndef.connect();

                if(!ndef.isWritable())
                {
                    Toast.makeText(this, "Tag is not writable.", Toast.LENGTH_LONG).show();
                    ndef.close();
                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
            }


        }

        catch (Exception e)
        {
            Log.e("WritheNdefMessage", e.getMessage());
        }
    }

    private NdefMessage createNdefMessage(String content)
    {
        //Adds the content to a record on the tag.
        NdefRecord ndefRecord = NdefRecord.createTextRecord(null, content);

        //Creates a record of what app to start when the tag is read.
        //NdefRecord appRecord =  NdefRecord.createApplicationRecord("nsimhie.nfctest");
        //NdefMessage ndefMessage = new NdefMessage(new NdefRecord[] {appRecord, ndefRecord});

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[] {ndefRecord});
        return ndefMessage;
    }

    private JSONObject writeJson()
    {
        EditText txtTask = (EditText) findViewById(R.id.etTask);
        EditText txtLocation = (EditText) findViewById(R.id.etLocation);
        EditText txtGps = (EditText) findViewById(R.id.etGps);
        EditText txtNotes = (EditText) findViewById(R.id.etNotes);
        CheckBox cbInmotion = (CheckBox) findViewById(R.id.cbMotion);

        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("task", txtTask.getText());
            jsonObject.put("location", txtLocation.getText());
            jsonObject.put("gps", txtGps.getText());
            jsonObject.put("notes", txtNotes.getText());
            jsonObject.put("inmotion",cbInmotion.isChecked());
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }


        return jsonObject;
    }
}
