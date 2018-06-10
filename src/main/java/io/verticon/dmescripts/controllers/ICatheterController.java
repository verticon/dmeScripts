package io.verticon.dmescripts.controllers;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public interface ICatheterController {

    public Product getSelectedItem();
    public void setSelectedItem(Product item);

    public boolean validateQuantities();
    public String getQuantityWarning();

    public void getOrder(JsonObjectBuilder builder);
}
