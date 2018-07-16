package com.dev.lishaboramobile.Admin.Views;

import android.content.Context;

import com.dev.lishaboramobile.Admin.Models.ChartModel;
import com.dev.lishaboramobile.Admin.Models.LVModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {
    private static DashboardController sSoleInstance;
    private Context context;

    public static int TRADERS=12;
    public static int FARMERS=13;
    public static int MILK=14;
    public static int LOANS=15;
    public static int PRODUCTS=16;


    private DashboardController(){
        if (sSoleInstance != null){
        throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
    }
    }  //private constructor.

    public synchronized static DashboardController getInstance(){
        //if there is no instance available... create new one

        if (sSoleInstance == null) {
            synchronized (DashboardController.class) {
                if (sSoleInstance == null) {
                    sSoleInstance = new DashboardController();
                }
            }
        }

        return sSoleInstance;
    }

    //Make singleton from serialize and deserialize operation.

    protected DashboardController readResolve() {
        return getInstance();
    }

    public DashboardController(Context context) {
        this.context = context;
    }

    private ChartModel jsonToProduct(String fileName, int type) {
        List<ChartModel> productModels = new ArrayList<>();

        Gson gson = new Gson();
        String js = loadJSONFromAsset(context, fileName);

        try {

            JSONObject jsonObject = new JSONObject(js);
            JSONArray jsonArray = jsonObject.optJSONArray("data");

            for (int a = 0; a < jsonArray.length(); a++) {
                JSONObject jsonObject1 = jsonArray.optJSONObject(a);
                ChartModel chartModel = new ChartModel();
                chartModel.setId(jsonObject1.optInt("id"));
                chartModel.setTitle(jsonObject1.optString("title"));
                chartModel.setDescription(jsonObject1.optString("description"));
                chartModel.setTotal(jsonObject1.optString("total"));
                JSONArray jsonArray1 = jsonObject1.optJSONArray("lvModels");
                List<LVModel> lvModels = new ArrayList<>();
                for (int b = 0; b < jsonArray1.length(); b++) {
                    JSONObject jsonObject2 = jsonArray1.getJSONObject(b);
                    LVModel lvModel = new LVModel();
                    lvModel.setColor(jsonObject2.optString("color"));
                    lvModel.setLabel(jsonObject2.optString("label"));
                    lvModel.setValue(jsonObject2.optInt("value"));
                    lvModels.add(lvModel);

                }
                chartModel.setLvModels(lvModels);

                if(chartModel.getId()==type) {
                    return chartModel;
                    //productModels.add(chartModel);
                }
            }
            //String data=jsonObject.getJSONArray("data").getString(0);
            //Log.d("dada",data);
            // productModels = gson.fromJson(js, new TypeToken<List<ChartModel>>(){}.getType());


        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
    private static String loadJSONFromAsset(Context activity, String fileName) {
        String json = null;
        try {
            InputStream is = activity.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public ChartModel getChartData(int type) {

      return   jsonToProduct("analyticsdata.json",type);
    }
}
