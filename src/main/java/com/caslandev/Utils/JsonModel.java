package com.caslandev.Utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.scene.control.TreeItem;

public class JsonModel {

    String userCredentialsJson = "17wdfMQBW4_dgCYfY-f_rEqKL6m1Yz1VB";
    String treeViewJson = "1DUBzokSEa0ioT4ww9n5VzVr17pVICLAv";

    public Boolean checkLogin(String username, String password,String ip) {
        JsonObject jsonObject = ReadJson.getJsonObject(userCredentialsJson);
        boolean flag =false;

        if (jsonObject.has(username)) {
            String user = jsonObject.get(username).getAsString();
            if (user.contains(password)&&user.contains(ip)) {
                flag = true;
            }
        }
        return flag;
    }

    public TreeItem getTreeItems() {
        JsonObject jsonObject = ReadJson.getJsonObject(treeViewJson);

        TreeItem<String> rootItem = new TreeItem<>("COURSES");
        rootItem.setExpanded(true);

        addNodes(jsonObject, rootItem);

        return rootItem;
    }

    private void addNodes(JsonObject jsonObject, TreeItem<String> parent) {
        for (String key : jsonObject.keySet()) {
            TreeItem<String> child = new TreeItem<>(key);
            parent.getChildren().add(child);

            JsonElement element = jsonObject.get(key);
            if (element.isJsonObject()) {
                addNodes(element.getAsJsonObject(), child);
            } else {
                TreeItem<String> valueNode = new TreeItem<>(element.getAsString());
                child.getChildren().add(valueNode);
            }
        }
    }
}
