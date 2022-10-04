package dio.me.simulator.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Team implements Parcelable {

    @SerializedName("nome")
    private String name;
    @SerializedName("forca")
    private int stars;
    @SerializedName("imagem")
    private String image;
    private Integer score;


    public Team() {
    }

    public Team(String name, int stars, String image, Integer score) {
        this.name = name;
        this.stars = stars;
        this.image = image;
        this.score = score;
    }

    protected Team(Parcel in) {
        name = in.readString();
        stars = in.readInt();
        image = in.readString();
        if (in.readByte() == 0) {
            score = null;
        } else {
            score = in.readInt();
        }
    }

    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(stars);
        parcel.writeString(image);
        if (score == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(score);
        }
    }
}
