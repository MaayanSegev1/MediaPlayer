package com.homeproject.maayanmediaplayer;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Song implements Parcelable, Serializable {

    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() { // from class: com.homeproject.maayanmediaplayer.Song.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Song createFromParcel(Parcel parcel) {
            return new Song(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override
        public Song[] newArray(int i) {
            return new Song[i];
        }
    };


    private String nameSong;
    private String LinkURL;
    private String imagePath;
    private int numSongID;

    public Song(String name, String URL, int songID) {
        this.imagePath = " ";
        this.nameSong = name;
        this.LinkURL = URL;
        this.numSongID = songID;
    }

    public Song(Parcel parcel) {
        this.imagePath = "";
        this.nameSong = parcel.readString();
        this.LinkURL = parcel.readString();
        this.imagePath = parcel.readString();
        this.numSongID = parcel.readByte();
    }

    public Song() { this.imagePath = ""; }

    public String getName() {
        return nameSong;
    }

    public void setName(String name) {
        this.nameSong = name;
    }

    public String getURL() {
        return LinkURL;
    }

    public void setURL(String URL) {
        this.LinkURL = URL;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String str) {
        this.imagePath = str;
    }

    public int getSongID() {
        return numSongID;
    }

    public void setSongID(int songID) {
        this.numSongID = songID;
    }

    @Override
    public String toString() {
        return "Song{" +
                "name='" + nameSong + '\'' +
                ", URL='" + LinkURL + '\'' +
                ", songID=" + numSongID +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.nameSong);
        parcel.writeString(this.LinkURL);
        parcel.writeString(this.imagePath);
        parcel.writeInt(this.numSongID);
    }
}
