//package com.moutimid.vellarentapp.Fragment;
//
//import android.content.ActivityNotFoundException;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.fragment.app.Fragment;
//
//import com.airbnb.lottie.BuildConfig;
//import com.fxn.stash.Stash;
//import com.moutamid.halalfoodfinder.Model.Villa;
//import com.moutamid.halalfoodfinder.R;
//import com.moutamid.halalfoodfinder.helper.Config;
//
//import java.util.ArrayList;
//
//public class SettingFragment extends Fragment {
//
//    TextView email, facebook, website, privacy_policy, share, rate, about, rest;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_setting, container, false);
//
//        email = view.findViewById(R.id.email_edt);
//        facebook = view.findViewById(R.id.facebook_edt);
//        website = view.findViewById(R.id.web_edt);
//        rate = view.findViewById(R.id.rate);
//        share = view.findViewById(R.id.share);
//        rest = view.findViewById(R.id.rest);
//         facebook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/426253597411506"));
//                    startActivity(intent);
//                } catch (Exception e) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/appetizerandroid")));
//                }
//            }
//        });
//
//
//        email.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent email = new Intent(Intent.ACTION_SEND);
//                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"dummy@gmail.com"});
//                email.putExtra(Intent.EXTRA_SUBJECT, "");
//                email.putExtra(Intent.EXTRA_TEXT, "");
//
//                //need this to prompts email client only
//                email.setType("message/rfc822");
//
//                startActivity(Intent.createChooser(email, "Choose an Email client :"));
//            }
//        });
//
//        website.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Uri uri = Uri.parse("https://bard.google.com/"); // missing 'http://' will cause crashed
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//            }
//        });
//
//        rate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Uri uri = Uri.parse("market://details?id=" + getContext().getPackageName());
//                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
//                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
//                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//                try {
//                    getContext().startActivity(goToMarket);
//                } catch (ActivityNotFoundException e) {
//                    getContext().startActivity(new Intent(Intent.ACTION_VIEW,
//                            Uri.parse("http://play.google.com/store/apps/details?id=" + getContext().getPackageName())));
//                }
//            }
//        });
//        share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("text/plain");
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
//                String shareMessage;
//                shareMessage = "This app is helpful for finding halal items, Here is the link to download\n\n";
//                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
//                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
//                startActivity(Intent.createChooser(shareIntent, "Choose one"));
//            }
//        });
//        return view;
//    }
//}