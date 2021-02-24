package com.social.sign.in;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class GoogleUsersDetail implements Parcelable {

    Uri photoUrl;
    String displayName;
    String email;
    String familyName;
    String givenName;
    String googleId;
    String idToken;
    String serverAuthCode;

    public GoogleUsersDetail() {
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getServerAuthCode() {
        return serverAuthCode;
    }

    public void setServerAuthCode(String serverAuthCode) {
        this.serverAuthCode = serverAuthCode;
    }

    public static Creator<GoogleUsersDetail> getCREATOR() {
        return CREATOR;
    }

    protected GoogleUsersDetail(Parcel in) {
        photoUrl = in.readParcelable(Uri.class.getClassLoader());
        displayName = in.readString();
        email = in.readString();
        familyName = in.readString();
        givenName = in.readString();
        googleId = in.readString();
        idToken = in.readString();
        serverAuthCode = in.readString();
    }

    public static final Creator<GoogleUsersDetail> CREATOR = new Creator<GoogleUsersDetail>() {
        @Override
        public GoogleUsersDetail createFromParcel(Parcel in) {
            return new GoogleUsersDetail(in);
        }

        @Override
        public GoogleUsersDetail[] newArray(int size) {
            return new GoogleUsersDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(photoUrl, i);
        parcel.writeString(displayName);
        parcel.writeString(email);
        parcel.writeString(familyName);
        parcel.writeString(givenName);
        parcel.writeString(googleId);
        parcel.writeString(idToken);
        parcel.writeString(serverAuthCode);
    }
}
