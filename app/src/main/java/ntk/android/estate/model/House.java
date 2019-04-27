package ntk.android.estate.model;

import com.google.gson.annotations.SerializedName;

public class House {

    @SerializedName("Id")
    public int Id;

    @SerializedName("Title")
    public String Title;

    @SerializedName("Photo")
    public String Photo;

    @SerializedName("Price")
    public String Price;

    @SerializedName("Address")
    public String Address;

    @SerializedName("Rate")
    public int Rate;

    public House(int id, String title, String photo, String price, String address, int rate) {
        Id = id;
        Title = title;
        Photo = photo;
        Price = price;
        Address = address;
        Rate = rate;
    }
}
