package com.example.fablescrolls;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // Set up back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("About Us");
        }

        // Set version info
        setVersionInfo();

        // Load team members
        loadTeamMembers();
    }

    private void setVersionInfo() {
        TextView versionInfo = findViewById(R.id.version_info);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            versionInfo.setText("Version " + version);
        } catch (PackageManager.NameNotFoundException e) {
            versionInfo.setText("Version 1.0");
        }
    }

    private void loadTeamMembers() {
        LinearLayout teamContainer = findViewById(R.id.team_members_container);
        LayoutInflater inflater = LayoutInflater.from(this);

        // Sample team data (replace with real data)
        List<TeamMember> teamMembers = new ArrayList<>();
        teamMembers.add(new TeamMember(
                "Rizve Reza",
                "CEO & Founder",
                "5+ years of experience in app development.",
                R.drawable.team_rizve)); // Add image to res/drawable

        teamMembers.add(new TeamMember(
                "Miss Luna",
                "UI/UX Designer",
                "Creates beautiful and intuitive interfaces.",
                R.drawable.team_luna));

        // Inflate and add each team member
        for (TeamMember member : teamMembers) {
            View memberView = inflater.inflate(R.layout.item_team_member, teamContainer, false);

            ImageView photo = memberView.findViewById(R.id.team_member_photo);
            TextView name = memberView.findViewById(R.id.team_member_name);
            TextView role = memberView.findViewById(R.id.team_member_role);
            TextView bio = memberView.findViewById(R.id.team_member_bio);

            photo.setImageResource(member.getPhotoResId());
            name.setText(member.getName());
            role.setText(member.getRole());
            bio.setText(member.getBio());

            teamContainer.addView(memberView);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Close activity when back button is pressed
        return true;
    }
}