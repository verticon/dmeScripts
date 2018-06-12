package io.verticon.dmescripts.controllers;

import io.verticon.dmescripts.model.Order;
import io.verticon.dmescripts.model.Patient;

public interface ICatheterController {

    public Product getSelectedItem();
    public void setSelectedItem(Product item);

    public boolean validateQuantities();
    public String getQuantityWarning();

    public Order getOrder(Patient patient);
}
