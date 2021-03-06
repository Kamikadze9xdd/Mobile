package com.nazar.kulyk_lab.localstorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nazar.kulyk_lab.models.artObjects.ArtObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ItemsStorage {

    private Gson gson = new Gson();

    public Boolean saveOrRemove(ArtObject artObject, View view){
        List<ArtObject> artObjects = getAlreadySaved(view);
        if(artObjects.size() != 0){
            Boolean result = checkAlreadySaved(artObjects, artObject);
            if(result){
                removeFromAlreadySaved(artObject, view);
                Toast.makeText(view.getContext(),"Remove from favourites", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                saveToSaved(artObject, view);
                Toast.makeText(view.getContext(),"Add to favourites", Toast.LENGTH_SHORT).show();
                return true;
            }
        } else{
            saveToSaved(artObject, view);
            Toast.makeText(view.getContext(),"Add to favourites", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    private List<ArtObject> getAlreadySaved(View view){
        SharedPreferences sharedPref = view.getContext().getApplicationContext().getSharedPreferences(
                "fav_list", Context.MODE_PRIVATE);
        String jsonPreferences = sharedPref.getString("fav_list", "");
        Log.i("favs", jsonPreferences);
        Type type = new TypeToken<List<ArtObject>>() {}.getType();
        return gson.fromJson(jsonPreferences, type);
    }

    private Boolean checkAlreadySaved(List<ArtObject> artObjects, ArtObject artObject){
        for(int i=0;i<artObjects.size();i++){
            if(artObjects.get(i).getId().equals(artObject.getId())){
                return true;
            }
        }
        return false;
    }

    private void saveToSaved(ArtObject artObject, View view){
        List<ArtObject> artObjects = getAlreadySaved(view);
        artObjects.add(artObject);
        saveListObject(artObjects, view);
    }

    private void removeFromAlreadySaved(ArtObject artObject, View view){
        Gson gson = new Gson();
        SharedPreferences sharedPref = view.getContext().getApplicationContext().getSharedPreferences(
                "fav_list", Context.MODE_PRIVATE);
        String jsonPreferences = sharedPref.getString("fav_list", "");
        Log.i("favs", jsonPreferences);

        if(!jsonPreferences.equals("")){
            ArrayList<ArtObject> alreadySaved;
            Type type = new TypeToken<List<ArtObject>>() {}.getType();
            alreadySaved = gson.fromJson(jsonPreferences, type);
            for(int i = 0; i < alreadySaved.size(); i++){
                if(alreadySaved.get(i).getId().equals(artObject.getId())){
                    alreadySaved.remove(i);
                    break;
                }
            }
            saveListObject(alreadySaved, view);
        }
    }

    public Boolean checkThatObjectAlreadySaved(ArtObject artObject, View view){
        List<ArtObject> artObjects = getAlreadySaved(view);
        return checkAlreadySaved(artObjects, artObject);
    }

    private void saveListObject(List<ArtObject> artObjects, View view){
        String json = gson.toJson(artObjects);
        SharedPreferences sharedPreferences = view.getContext().getApplicationContext().getSharedPreferences(
                "fav_list",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fav_list",json);
        editor.apply();
    }
}
