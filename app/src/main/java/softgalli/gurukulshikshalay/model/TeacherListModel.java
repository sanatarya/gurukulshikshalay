package softgalli.gurukulshikshalay.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TeacherListModel {

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<TeacherListDataModel> getData() {
        return data;
    }

    @SerializedName("status")
    String status;
    @SerializedName("msg")
    String msg;
    @SerializedName("data")
    ArrayList<TeacherListDataModel> data;

}
