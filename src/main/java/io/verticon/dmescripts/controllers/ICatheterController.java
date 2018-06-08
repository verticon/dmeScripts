package io.verticon.dmescripts.controllers;

public interface ICatheterController {

    public Product getSelectedItem();
    public void setSelectedItem(Product item);

    public boolean validateQuantities();
    public String getQuantityWarning();
}
