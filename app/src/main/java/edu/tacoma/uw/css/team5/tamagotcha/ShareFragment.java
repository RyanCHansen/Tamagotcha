package edu.tacoma.uw.css.team5.tamagotcha;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ShareActionProvider;

import java.io.File;
import java.io.FileOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShareFragment extends Fragment {
    private static final String BITMAP_KEY = "image";

    private Bitmap mScreenshot;
    private Button mSubmitButton;
    private EditText mEditPhone;
    private EditText mEditMessage;
    private ShareActionProvider mShareActionProvider;


    public ShareFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mScreenshot = getArguments().getParcelable(BITMAP_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        mSubmitButton = view.findViewById(R.id.share_submit_button);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditPhone = (EditText) getView().findViewById(R.id.share_phone_number);
                mEditMessage = (EditText) getView().findViewById(R.id.share_message);
                shareBitmap();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //ImageView screenshotView = (ImageView) getView().findViewById(R.id.share_image);
        ImageView screenshotView1 = (ImageView) view.findViewById(R.id.share_image);
        screenshotView1.setImageBitmap(mScreenshot);

    }

    private void shareBitmap() {
        try {
            File file = new File(getContext().getCacheDir(), "screenshot.png");
            FileOutputStream fOut = new FileOutputStream(file);
            mScreenshot.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.putExtra("address",mEditPhone.getText());
            intent.putExtra("sms_body", mEditMessage.getText());
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("vnd.android-dir/mms-sms");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        //getMenuInflater().inflate(R.menu.share_menu, menu);
//
//        // Locate MenuItem with ShareActionProvider
//        MenuItem item = menu.findItem(R.id.menu_item_share);
//
//        // Fetch and store ShareActionProvider
//        mShareActionProvider = (ShareActionProvider) item.getActionProvider();
//
//        // Return true to display menu
//        return true;
//    }
//
//    // Call to update the share intent
//    private void setShareIntent(Intent shareIntent) {
//        if (mShareActionProvider != null) {
//            mShareActionProvider.setShareIntent(shareIntent);
//        }
//    }


}
