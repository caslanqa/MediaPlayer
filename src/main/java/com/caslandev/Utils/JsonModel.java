package com.caslandev.Utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.scene.control.TreeItem;

public class JsonModel {

    public TreeItem getTreeItems() {

        JsonObject jsonObject = ReadJson.getJsonObject();

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
